package com.alcatrazescapee.notreepunching.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.level.material.Material;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import com.alcatrazescapee.notreepunching.Config;
import com.alcatrazescapee.notreepunching.common.ModTags;
import com.alcatrazescapee.notreepunching.common.blocks.PotteryBlock;
import com.alcatrazescapee.notreepunching.mixin.AbstractBlockAccessor;
import com.alcatrazescapee.notreepunching.mixin.AbstractBlockStateAccessor;
import com.alcatrazescapee.notreepunching.mixin.DiggerItemAccessor;
import com.alcatrazescapee.notreepunching.platform.XPlatform;


public final class HarvestBlockHandler
{
    private static final Logger LOGGER = LogManager.getLogger();

    private static final Map<Material, ToolType> PRIMARY_TOOL_TYPES = new HashMap<>();
    private static final Map<Block, ToolType> BLOCK_TOOL_TYPES = new HashMap<>();
    private static final Map<Item, Set<ToolType>> ITEM_TOOL_TYPES = new HashMap<>();
    private static final Map<TagKey<Block>, ToolType> DEFAULT_TOOL_TYPES = new IdentityHashMap<>();
    private static final Map<TagKey<Block>, Set<ToolType>> UNIQUE_TOOL_TYPES = new IdentityHashMap<>();

    static
    {
        add(ToolType.PICKAXE, Material.DECORATION, Material.BUILDABLE_GLASS, Material.ICE_SOLID, Material.SHULKER_SHELL, Material.GLASS, Material.ICE, Material.STONE, Material.METAL, Material.HEAVY_METAL, Material.PISTON, Material.AMETHYST);
        add(ToolType.AXE, Material.WOOD, Material.NETHER_WOOD, Material.BAMBOO, Material.CACTUS, Material.MOSS, Material.VEGETABLE);
        add(ToolType.SHOVEL, Material.TOP_SNOW, Material.CLAY, Material.DIRT, Material.GRASS, Material.SAND, Material.SNOW, Material.POWDER_SNOW, PotteryBlock.BREAKABLE_CLAY);
        add(ToolType.HOE, Material.PLANT, Material.WATER_PLANT, Material.REPLACEABLE_PLANT, Material.REPLACEABLE_WATER_PLANT, Material.REPLACEABLE_FIREPROOF_PLANT, Material.SCULK, Material.SPONGE, Material.BAMBOO_SAPLING, Material.LEAVES);
        add(ToolType.SHARP, Material.CLOTH_DECORATION, Material.WEB, Material.WOOL, Material.CAKE);
        add(ToolType.NONE, Material.AIR, Material.STRUCTURAL_AIR, Material.PORTAL, Material.WATER, Material.BUBBLE_COLUMN, Material.LAVA, Material.FIRE, Material.EXPLOSIVE, Material.BARRIER, Material.EGG);

        DEFAULT_TOOL_TYPES.put(BlockTags.MINEABLE_WITH_PICKAXE, ToolType.PICKAXE);
        DEFAULT_TOOL_TYPES.put(BlockTags.MINEABLE_WITH_AXE, ToolType.AXE);
        DEFAULT_TOOL_TYPES.put(BlockTags.MINEABLE_WITH_SHOVEL, ToolType.SHOVEL);
        DEFAULT_TOOL_TYPES.put(BlockTags.MINEABLE_WITH_HOE, ToolType.HOE);
    }

    private static void add(ToolType value, Material... keys)
    {
        Stream.of(keys).forEach(key -> PRIMARY_TOOL_TYPES.put(key, value));
    }

    private static void add(Item item, ToolType tool)
    {
        ITEM_TOOL_TYPES.computeIfAbsent(item, key -> new HashSet<>()).add(tool);
    }

    public static void setup()
    {
        BLOCK_TOOL_TYPES.clear();

        final Map<Material, List<Block>> unknownMaterialBlocks = new HashMap<>();
        Registry.BLOCK.stream().forEach(block ->
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

            // Infer a primary tool type for the block.
            final ToolType primary = PRIMARY_TOOL_TYPES.get(blockAccess.getMaterial());
            if (primary != null && primary != ToolType.NONE)
            {
                BLOCK_TOOL_TYPES.put(block, primary);
            }
            if (primary == null)
            {
                // Unknown tool type. Collect and log it later
                unknownMaterialBlocks.computeIfAbsent(blockAccess.getMaterial(), k -> new ArrayList<>()).add(block);
            }
        });

        Registry.ITEM.stream().forEach(item ->
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
        });

        if (!unknownMaterialBlocks.isEmpty())
        {
            LOGGER.error("Unable to infer primary tools for {} blocks with unknown materials. These blocks will not be affected by NTP's modifications!", unknownMaterialBlocks.values().stream().mapToInt(Collection::size).sum());
        }
        for (Map.Entry<Material, List<Block>> entry : unknownMaterialBlocks.entrySet())
        {
            final Material material = entry.getKey();
            final List<Block> blocks = entry.getValue();
            LOGGER.warn("Material: [isLiquid={}, isSolid={}, blocksMotion={}, isFlammable={}, isReplaceable={}, isSolidBlocking={}, getPushReaction={}, getColor=[id={}, col={}]] | Blocks: {}",
                material.isLiquid(), material.isSolid(), material.blocksMotion(), material.isFlammable(), material.isReplaceable(), material.isSolidBlocking(), material.getPushReaction(), material.getColor().id, new Color(material.getColor().col),
                blocks.stream().map(Registry.BLOCK::getKey).map(ResourceLocation::toString).collect(Collectors.joining(", ")));
        }
    }

    public static void inferUniqueToolTags()
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
    }

    private static boolean isTagSupersetOfTag(TagKey<Block> superset, TagKey<Block> set)
    {
        return Registry.BLOCK.getOrCreateTag(set)
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
        return pos != null ? state.getDestroySpeed(player.level, pos) : ((AbstractBlockStateAccessor) state).getDestroySpeed();
    }
}