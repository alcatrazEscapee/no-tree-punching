/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.registries.ForgeRegistries;

import com.alcatrazescapee.notreepunching.Config;
import com.alcatrazescapee.notreepunching.common.ModTags;
import com.alcatrazescapee.notreepunching.mixin.block.AbstractBlockAccess;
import com.alcatrazescapee.notreepunching.mixin.block.AbstractBlockPropertiesAccess;
import com.alcatrazescapee.notreepunching.mixin.block.AbstractBlockStateAccess;

/**
 * Manager for all block / blockstate / material related modifications in order for this mod to function
 *
 * How vanilla harvest handling, and forge's modifications to it, work:
 * - Vanilla tool classes have hardcoded lists of either materials, or blocks, which respect tools
 * - Forge uses these to apply harvest tools and levels to all blocks, including ones that don't require tools
 * - We need to detect these inferences and modify them - this is why vanilla plants have the 'axe' tool type
 *
 * @see ForgeHooks#initTools()
 *
 * Invasive modifications:
 * - Modifies the `toolRequired` field of blocks and blockstates to be always true
 * - Adds tool types based on materials to specific blocks if they don't declare one by default
 * - Adds the "sword" tool type to all sword and shears item (using an instanceof check)
 */
public class HarvestBlockHandler
{
    public static final ToolType SWORD = ToolType.get("sword");
    public static final ToolType MATTOCK = ToolType.get("mattock");

    private static final Set<Block> DEFAULT_NO_TOOL_BLOCKS = new HashSet<>();
    private static final Map<Block, ToolType> ADDITIONAL_TOOL_TYPE_BLOCKS = new HashMap<>();

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Field HARVEST_TOOL_FIELD = Helpers.findUnobfField(Block.class, "harvestTool");
    private static final Field HARVEST_LEVEL_FIELD = Helpers.findUnobfField(Block.class, "harvestLevel");
    private static final Field TOOL_CLASSES_FIELD = Helpers.findUnobfField(Item.class, "toolClasses");

    public static void setup()
    {
        final Map<Material, ToolType> inferredTools = new HashMap<>();

        Helpers.putAll(inferredTools, ToolType.AXE, Material.WOOD, Material.NETHER_WOOD);
        Helpers.putAll(inferredTools, ToolType.PICKAXE, Material.STONE, Material.METAL, Material.HEAVY_METAL, Material.PISTON);
        Helpers.putAll(inferredTools, ToolType.SHOVEL, Material.DIRT, Material.VEGETABLE, Material.SAND, Material.CLAY, Material.TOP_SNOW, Material.SNOW);
        Helpers.putAll(inferredTools, ToolType.HOE, Material.GRASS, Material.REPLACEABLE_FIREPROOF_PLANT, Material.SPONGE, Material.LEAVES);
        Helpers.putAll(inferredTools, SWORD, Material.PLANT, Material.WATER_PLANT, Material.REPLACEABLE_PLANT, Material.REPLACEABLE_WATER_PLANT, Material.BAMBOO, Material.BAMBOO_SAPLING, Material.CACTUS);

        for (Block block : ForgeRegistries.BLOCKS.getValues())
        {
            final AbstractBlockAccess blockAccess = (AbstractBlockAccess) block;
            final AbstractBlock.Properties settings = blockAccess.getProperties();
            final AbstractBlockPropertiesAccess settingsAccess = (AbstractBlockPropertiesAccess) settings;
            final Material material = blockAccess.getMaterial();

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

            // Add extra harvest levels and types to specific blocks
            // Apparently we "shouldn't be doing this". But that's kind of the whole point of this mod, so here we go!
            try
            {
                // We track the 'default' to be the tool type of the block, if it has one, otherwise we default it if possible
                ToolType harvestTool = (ToolType) HARVEST_TOOL_FIELD.get(block);
                int harvestLevel = HARVEST_LEVEL_FIELD.getInt(block);
                if (harvestTool == null)
                {
                    harvestTool = inferredTools.get(material);
                    if (harvestTool != null)
                    {
                        HARVEST_TOOL_FIELD.set(block, harvestTool);
                        HARVEST_LEVEL_FIELD.set(block, Math.max(0, harvestLevel));
                    }
                }
                else
                {
                    // Already has a harvest tool set, but we've encountered another one that it should have
                    // This is most likely caused by bad forge defaulting
                    final ToolType extraTool = inferredTools.get(material);
                    if (extraTool != null)
                    {
                        ADDITIONAL_TOOL_TYPE_BLOCKS.put(block, extraTool);
                    }
                }
            }
            catch (IllegalAccessException e)
            {
                LOGGER.warn("Unable to set harvest tool and level for block: {}. Cause: {}. Please report this to NTP!", block, e.getMessage());
            }
        }

        for (Item item : ForgeRegistries.ITEMS.getValues())
        {
            try
            {
                @SuppressWarnings("unchecked") final Map<ToolType, Integer> toolClasses = (Map<ToolType, Integer>) TOOL_CLASSES_FIELD.get(item);
                if (item instanceof SwordItem && !toolClasses.containsKey(SWORD))
                {
                    toolClasses.put(SWORD, ((SwordItem) item).getTier().getLevel());
                }
                if (item instanceof ShearsItem && !toolClasses.containsKey(SWORD))
                {
                    // Shears have the sword class as well
                    toolClasses.put(SWORD, 0);
                }
            }
            catch (IllegalAccessException | IllegalArgumentException e)
            {
                LOGGER.warn("Unable to add tool class(es) for item: {}. Cause: {}. Please report this to NTP!", item, e.getMessage());
            }
        }
    }

    public static boolean doesBlockRequireNoToolByDefault(Block block)
    {
        return DEFAULT_NO_TOOL_BLOCKS.contains(block);
    }

    /**
     * This is a better version of {@link net.minecraftforge.common.ForgeHooks#canHarvestBlock(BlockState, PlayerEntity, IBlockReader, BlockPos)}
     *
     * @param doDropsChecks If the def'n of harvestability should include if that block will be able to drop items. Otherwise, just use generic harvest checks.
     * @return If the state can be harvested (broken, and obtain drops) by the current player
     */
    public static boolean canHarvest(BlockState state, PlayerEntity player, boolean doDropsChecks)
    {
        if (doDropsChecks)
        {
            if (ModTags.Blocks.ALWAYS_DROPS.contains(state.getBlock()))
            {
                return true; // Whitelist specific blocks, bypasses all restrictions
            }

            if (((AbstractBlockStateAccess) state).getDestroySpeed() == 0 && Config.SERVER.doInstantBreakBlocksDropWithoutCorrectTool.get())
            {
                return true; // Instant break blocks also hardcode to true, if the respective config option is set such that they always drop
            }
        }

        if (Config.SERVER.noBlockDropsWithoutCorrectTool.get())
        {
            final ItemStack stack = player.inventory.getSelected();

            // In order, try the default tool, and extra tool
            // If no tool is present, the block is harvestable
            // If at least one tool is present, it must pass to be harvestable
            ToolType tool = state.getHarvestTool();
            if (tool != null && canHarvestWithTool(state, player, stack, tool))
            {
                return true;
            }
            tool = ADDITIONAL_TOOL_TYPE_BLOCKS.get(state.getBlock());
            return tool == null || canHarvestWithTool(state, player, stack, tool);
        }
        else
        {
            // Delegate to vanilla handling
            return doesBlockRequireNoToolByDefault(state.getBlock());
        }
    }

    private static boolean canHarvestWithTool(BlockState state, PlayerEntity player, ItemStack stack, ToolType tool)
    {
        // Check the default method first, before we go straight to computing harvest levels / tools
        if (stack.isCorrectToolForDrops(state))
        {
            return true;
        }

        // This should catch forge-added harvest tool / level shenanagains
        int toolLevel = stack.getItem().getHarvestLevel(stack, tool, player, state);
        if (toolLevel == -1)
        {
            toolLevel = getExtraHarvestLevel(stack, tool);
        }
        int harvestLevel = state.getHarvestLevel();
        if (harvestLevel == -1)
        {
            harvestLevel = 0; // Assume, since the state has a harvest tool, that it should also have a harvest level. Careless modders may omit this.
        }
        return toolLevel >= harvestLevel;
    }

    private static int getExtraHarvestLevel(ItemStack stack, ToolType tool)
    {
        // Extra rules for tools
        if (tool == ToolType.SHOVEL && stack.getToolTypes().contains(ToolType.PICKAXE))
        {
            // Pickaxes can function as basic shovels
            return 0;
        }
        if (tool == SWORD && (ModTags.Items.KNIVES.contains(stack.getItem()) || Tags.Items.SHEARS.contains(stack.getItem())))
        {
            // Knives and Shears have the "sword" tool type which is used for plant materials
            if (stack.getItem() instanceof TieredItem)
            {
                return ((TieredItem) stack.getItem()).getTier().getLevel();
            }
            else
            {
                return 0;
            }
        }
        return -1;
    }
}