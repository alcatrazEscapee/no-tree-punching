/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.util;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

import com.alcatrazescapee.notreepunching.Config;
import com.alcatrazescapee.notreepunching.common.ModTags;
import com.alcatrazescapee.notreepunching.mixin.accessor.AbstractBlockAccess;
import com.alcatrazescapee.notreepunching.mixin.accessor.AbstractBlockPropertiesAccess;
import com.alcatrazescapee.notreepunching.mixin.accessor.AbstractBlockStateAccess;
import com.alcatrazescapee.notreepunching.mixin.accessor.DiggerItemAccessor;


public class HarvestBlockHandler
{
    private static final Logger LOGGER = LogManager.getLogger();

    private static final Map<Material, ToolType> PRIMARY_TOOL_TYPES = ImmutableMap.<Material, ToolType>builder()
        .putAll(all(ToolType.PICKAXE, Material.DECORATION, Material.BUILDABLE_GLASS, Material.ICE_SOLID, Material.SHULKER_SHELL, Material.GLASS, Material.ICE, Material.STONE, Material.METAL, Material.HEAVY_METAL, Material.PISTON, Material.AMETHYST))
        .putAll(all(ToolType.AXE, Material.WOOD, Material.NETHER_WOOD, Material.BAMBOO, Material.CACTUS, Material.MOSS, Material.VEGETABLE))
        .putAll(all(ToolType.SHOVEL, Material.TOP_SNOW, Material.CLAY, Material.DIRT, Material.GRASS, Material.SAND, Material.SNOW, Material.POWDER_SNOW))
        .putAll(all(ToolType.HOE, Material.PLANT, Material.WATER_PLANT, Material.REPLACEABLE_PLANT, Material.REPLACEABLE_WATER_PLANT, Material.REPLACEABLE_FIREPROOF_PLANT, Material.SCULK, Material.SHULKER_SHELL, Material.BAMBOO_SAPLING, Material.LEAVES))
        .putAll(all(ToolType.SHARP, Material.CLOTH_DECORATION, Material.WEB, Material.WOOL, Material.CAKE))
        .putAll(all(ToolType.NONE, Material.AIR, Material.STRUCTURAL_AIR, Material.PORTAL, Material.WATER, Material.BUBBLE_COLUMN, Material.LAVA, Material.FIRE, Material.EXPLOSIVE, Material.BARRIER, Material.EGG))
        .build();

    private static final Map<Block, ToolType> BLOCK_TOOL_TYPES = new HashMap<>();
    private static final Map<Item, ToolType> ITEM_TOOL_TYPES = new HashMap<>();
    private static final Set<Block> DEFAULT_NO_TOOL_BLOCKS = new HashSet<>();

    @SafeVarargs
    private static <K, V> Map<K, V> all(V value, K... keys)
    {
        return Stream.of(keys).collect(Collectors.toMap(k -> k, k -> value));
    }

    public static void setup()
    {
        BLOCK_TOOL_TYPES.clear();

        final Map<Material, List<Block>> unknownMaterialBlocks = new HashMap<>();
        for (Block block : ForgeRegistries.BLOCKS.getValues())
        {
            final AbstractBlockAccess blockAccess = (AbstractBlockAccess) block;
            final BlockBehaviour.Properties settings = blockAccess.getProperties();
            final AbstractBlockPropertiesAccess settingsAccess = (AbstractBlockPropertiesAccess) settings;

            if (!settingsAccess.getRequiresCorrectToolForDrops())
            {
                // The block by default has no tool. Flag it so we can refer to it later
                DEFAULT_NO_TOOL_BLOCKS.add(block);
            }

            // Forcefully set everything to require a tool
            // Need to do both the block settings and the block state since the value is copied there for every state
            settings.requiresCorrectToolForDrops();
            for (BlockState state : block.getStateDefinition().getPossibleStates())
            {
                ((AbstractBlockStateAccess) state).setRequiresCorrectToolForDrops(true);
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
        }

        for (Item item : ForgeRegistries.ITEMS.getValues())
        {
            if (item instanceof DiggerItem digger)
            {
                final ToolType toolType = toolTypeForMineableTag(((DiggerItemAccessor) digger).accessor$getBlocks());
                if (toolType != ToolType.NONE)
                {
                    ITEM_TOOL_TYPES.put(item, toolType);
                }
            }
            else if (item instanceof SwordItem || item instanceof ShearsItem)
            {
                ITEM_TOOL_TYPES.put(item, ToolType.SHARP);
            }
        }

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
                blocks.stream().map(b -> b.getRegistryName().toString()).collect(Collectors.joining(", ")));
        }
    }

    private static ToolType toolTypeForMineableTag(Tag<Block> tag)
    {
        if (tag == BlockTags.MINEABLE_WITH_PICKAXE)
        {
            return ToolType.PICKAXE;
        }
        if (tag == BlockTags.MINEABLE_WITH_AXE)
        {
            return ToolType.AXE;
        }
        if (tag == BlockTags.MINEABLE_WITH_SHOVEL)
        {
            return ToolType.SHOVEL;
        }
        if (tag == BlockTags.MINEABLE_WITH_HOE)
        {
            return ToolType.HOE;
        }
        return ToolType.NONE;
    }

    public static boolean doesBlockRequireNoToolByDefault(Block block)
    {
        return DEFAULT_NO_TOOL_BLOCKS.contains(block);
    }

    public static boolean isUsingCorrectToolToMine(BlockState state, Player player)
    {
        return isUsingCorrectTool(state, player, ModTags.Blocks.ALWAYS_BREAKABLE, Config.SERVER.doBlocksMineWithoutCorrectTool, Config.SERVER.doInstantBreakBlocksMineWithoutCorrectTool);
    }

    public static boolean isUsingCorrectToolForDrops(BlockState state, Player player)
    {
        return isUsingCorrectTool(state, player, ModTags.Blocks.ALWAYS_DROPS, Config.SERVER.doBlocksMineWithoutCorrectTool, Config.SERVER.doInstantBreakBlocksDropWithoutCorrectTool);
    }

    private static boolean isUsingCorrectTool(BlockState state, Player player, Tag.Named<Block> alwaysAllowTag, ForgeConfigSpec.BooleanValue withoutCorrectTool, ForgeConfigSpec.BooleanValue instantBreakBlocksWithoutCorrectTool)
    {
        if (withoutCorrectTool.get())
        {
            return true; // Feature is disabled, always allow
        }

        if (((AbstractBlockStateAccess) state).getDestroySpeed() == 0 && instantBreakBlocksWithoutCorrectTool.get())
        {
            return true; // Feature is conditionally disabled for instant break blocks, always allow
        }

        if (alwaysAllowTag.contains(state.getBlock()))
        {
            return true; // Block is set to always allow
        }

        final ItemStack stack = player.getMainHandItem();
        if (stack.isCorrectToolForDrops(state))
        {
            return true; // Tool has already reported itself as the correct tool
        }

        final ToolType expectedToolType = BLOCK_TOOL_TYPES.getOrDefault(state.getBlock(), ToolType.NONE);
        if (expectedToolType == ToolType.NONE)
        {
            return true; // No expected tool type, so we have to return true because we don't know otherwise
        }

        // Now, we need to infer if the current item is of a given tool type. Try two things:
        final ToolType inferredToolType = ITEM_TOOL_TYPES.getOrDefault(stack.getItem(), ToolType.NONE);
        if (inferredToolType == expectedToolType)
        {
            return true; // Correct tool type found!
        }

        // Otherwise, we check if the expected tool type can identify this item as it's tool
        return expectedToolType.is(stack.getItem());
    }
}