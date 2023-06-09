package com.alcatrazescapee.notreepunching.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import com.alcatrazescapee.notreepunching.Config;
import com.alcatrazescapee.notreepunching.common.ModTags;
import com.alcatrazescapee.notreepunching.mixin.AbstractBlockAccessor;
import com.alcatrazescapee.notreepunching.mixin.AbstractBlockStateAccessor;
import com.alcatrazescapee.notreepunching.mixin.DiggerItemAccessor;
import com.alcatrazescapee.notreepunching.platform.XPlatform;


public final class HarvestBlockHandler
{
    private static final Map<Block, ToolType> BLOCK_TOOL_TYPES = new IdentityHashMap<>();
    private static final Map<Item, Set<ToolType>> ITEM_TOOL_TYPES = new IdentityHashMap<>();
    private static final Map<TagKey<Block>, ToolType> DEFAULT_TOOL_TYPES = new IdentityHashMap<>();
    private static final Map<TagKey<Block>, Set<ToolType>> UNIQUE_TOOL_TYPES = new IdentityHashMap<>();

    static
    {
        DEFAULT_TOOL_TYPES.put(BlockTags.MINEABLE_WITH_PICKAXE, ToolType.PICKAXE);
        DEFAULT_TOOL_TYPES.put(BlockTags.MINEABLE_WITH_AXE, ToolType.AXE);
        DEFAULT_TOOL_TYPES.put(BlockTags.MINEABLE_WITH_SHOVEL, ToolType.SHOVEL);
        DEFAULT_TOOL_TYPES.put(BlockTags.MINEABLE_WITH_HOE, ToolType.HOE);
        DEFAULT_TOOL_TYPES.put(BlockTags.SWORD_EFFICIENT, ToolType.SHARP);
    }

    private static void add(Item item, ToolType tool)
    {
        ITEM_TOOL_TYPES.computeIfAbsent(item, key -> new HashSet<>()).add(tool);
    }

    public static void setup()
    {
        for (Block block : BuiltInRegistries.BLOCK)
        {
            final AbstractBlockAccessor blockAccess = (AbstractBlockAccessor) block;
            final BlockBehaviour.Properties settings = blockAccess.getProperties();

            // Forcefully set everything to require a tool
            // Need to do both the block settings and the block state since the value is copied there for every state
            settings.requiresCorrectToolForDrops();
            for (BlockState state : block.getStateDefinition().getPossibleStates())
            {
                ((AbstractBlockStateAccessor) state).setRequiresCorrectToolForDrops(true);
            }
        }

        for (Item item : BuiltInRegistries.ITEM)
        {
            if (item instanceof DiggerItem digger)
            {
                // Infer known tags
                final TagKey<Block> toolTag = ((DiggerItemAccessor) digger).getBlocks();
                final ToolType toolType = toolTypeForMineableTag(toolTag);
                if (toolType != ToolType.NONE)
                {
                    add(item, toolType);
                }
                else
                {
                    // For unknown tags, mark this as a special tool, to be handled later
                    add(item, ToolType.UNIQUE);
                }
            }

            // Infer subclasses of vanilla items
            if (item instanceof AxeItem)
            {
                add(item, ToolType.AXE);
            }
            else if (item instanceof HoeItem)
            {
                add(item, ToolType.HOE);
            }
            else if (item instanceof PickaxeItem)
            {
                add(item, ToolType.PICKAXE);
            }
            else if (item instanceof ShovelItem)
            {
                add(item, ToolType.SHOVEL);
            }
            else if (item instanceof SwordItem || item instanceof ShearsItem)
            {
                add(item, ToolType.SHARP);
            }
        }
    }

    public static void inferToolTypesFromTags()
    {
        // Must be called once tags are loaded
        UNIQUE_TOOL_TYPES.clear();
        ITEM_TOOL_TYPES.forEach((item, toolTypes) -> {
            if (toolTypes.contains(ToolType.UNIQUE) && item instanceof DiggerItem digger)
            {
                final TagKey<Block> toolTag = ((DiggerItemAccessor) digger).getBlocks();

                // Infer if this is a superset of known tags, and if so, record it as of that tool type.
                DEFAULT_TOOL_TYPES.forEach((knownToolTag, toolType) -> {
                    if (isTagSupersetOfTag(toolTag, knownToolTag))
                    {
                        UNIQUE_TOOL_TYPES.computeIfAbsent(toolTag, key -> new HashSet<>()).add(toolType);
                    }
                });
            }
        });

        BLOCK_TOOL_TYPES.clear();
        for (Map.Entry<TagKey<Block>, ToolType> entry : DEFAULT_TOOL_TYPES.entrySet())
        {
            BuiltInRegistries.BLOCK.getOrCreateTag(entry.getKey())
                .forEach(holder -> BLOCK_TOOL_TYPES.put(holder.value(), entry.getValue()));
        }
        for (Map.Entry<TagKey<Block>, Set<ToolType>> entry : UNIQUE_TOOL_TYPES.entrySet())
        {
            BuiltInRegistries.BLOCK.getOrCreateTag(entry.getKey())
                .forEach(holder -> entry.getValue().forEach(tool -> BLOCK_TOOL_TYPES.put(holder.value(), tool)));
        }
    }

    private static boolean isTagSupersetOfTag(TagKey<Block> superset, TagKey<Block> set)
    {
        return BuiltInRegistries.BLOCK.getOrCreateTag(set)
            .stream()
            .allMatch(holder -> holder.is(superset));
    }

    private static ToolType toolTypeForMineableTag(TagKey<Block> tag)
    {
        return DEFAULT_TOOL_TYPES.getOrDefault(tag, ToolType.NONE);
    }

    public static boolean isUsingCorrectToolToMine(BlockState state, @Nullable BlockPos pos, Player player)
    {
        return isUsingCorrectTool(state, pos, player, ModTags.Blocks.ALWAYS_BREAKABLE, Config.INSTANCE.doBlocksMineWithoutCorrectTool, Config.INSTANCE.doInstantBreakBlocksMineWithoutCorrectTool);
    }

    public static boolean isUsingCorrectToolForDrops(BlockState state, @Nullable BlockPos pos, Player player)
    {
        return isUsingCorrectTool(state, pos, player, ModTags.Blocks.ALWAYS_DROPS, Config.INSTANCE.doBlocksDropWithoutCorrectTool, Config.INSTANCE.doInstantBreakBlocksDropWithoutCorrectTool);
    }

    private static boolean isUsingCorrectTool(BlockState state, @Nullable BlockPos pos, Player player, TagKey<Block> alwaysAllowTag, Supplier<Boolean> withoutCorrectTool, BooleanSupplier instantBreakBlocksWithoutCorrectTool)
    {
        if (withoutCorrectTool.get())
        {
            return true; // Feature is disabled, always allow
        }

        if (getDestroySpeed(state, pos, player) == 0 && instantBreakBlocksWithoutCorrectTool.getAsBoolean())
        {
            return true; // Feature is conditionally disabled for instant break blocks, always allow
        }

        if (state.is(alwaysAllowTag))
        {
            return true; // Block is set to always allow
        }

        final ItemStack stack = player.getMainHandItem();
        if (stack.isCorrectToolForDrops(state))
        {
            return true; // Tool has already reported itself as the correct tool. This includes a tier check in vanilla.
        }

        final ToolType expectedToolType = BLOCK_TOOL_TYPES.getOrDefault(state.getBlock(), ToolType.NONE);
        if (expectedToolType == ToolType.NONE)
        {
            return true; // No expected tool type, so we have to return true because we don't know otherwise
        }

        if (!isUsingCorrectTier(state, stack))
        {
            return false; // Not using the correct tier, and the block is tiered. This will only exclude tiered blocks, not those without a tier
        }

        // Now, we need to infer if the current item is of a given tool type.
        final Set<ToolType> toolTypes = ITEM_TOOL_TYPES.getOrDefault(stack.getItem(), Collections.emptySet());

        // If this contains a unique tool type, then we also need to check unique tool types, which are based on tag supersets
        if (toolTypes.contains(ToolType.UNIQUE) && stack.getItem() instanceof DiggerItem digger)
        {
            final TagKey<Block> toolTag = ((DiggerItemAccessor) digger).getBlocks();
            final Set<ToolType> uniqueToolTypes = UNIQUE_TOOL_TYPES.getOrDefault(toolTag, Collections.emptySet());

            if (isUsingAnyOfCorrectTools(expectedToolType, stack, uniqueToolTypes))
            {
                return true; // Found a matching unique tool type
            }
        }

        return isUsingAnyOfCorrectTools(expectedToolType, stack, toolTypes);
    }

    private static boolean isUsingAnyOfCorrectTools(ToolType expectedToolType, ItemStack stack, Set<ToolType> toolTypes)
    {
        for (ToolType inferredToolType : toolTypes)
        {
            if (inferredToolType == ToolType.UNIQUE)
            {
                continue; // Just a marker to check the unique based tool types
            }

            if (inferredToolType == expectedToolType)
            {
                return true; // Correct tool type found!
            }

            // Otherwise, we check if the expected tool type can identify this item as it's tool
            if (expectedToolType.is(stack.getItem()))
            {
                return true;
            }
        }
        return false;
    }

    private static boolean isUsingCorrectTier(BlockState state, ItemStack stack)
    {
        Tier tier = Tiers.WOOD; // Assume this is the lowest tier
        if (stack.getItem() instanceof TieredItem item)
        {
            tier = item.getTier();
        }
        return XPlatform.INSTANCE.isUsingCorrectTier(state, tier);
    }

    private static float getDestroySpeed(BlockState state, @Nullable BlockPos pos, Player player)
    {
        return pos != null ? state.getDestroySpeed(player.level(), pos) : ((AbstractBlockStateAccessor) state).getDestroySpeed();
    }
}