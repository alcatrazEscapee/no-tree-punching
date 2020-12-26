/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.util;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.TieredItem;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
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
 * Invasive modifications:
 * - Modifies the `toolRequired` field of blocks and blockstates to be always true
 * - Adds tool types based on materials to specific blocks if they don't declare one by default
 * - Adds the "sword" tool type to all sword subclasses
 */
public class HarvestBlockHandler
{
    public static final ToolType SWORD = ToolType.get("sword");
    public static final ToolType MATTOCK = ToolType.get("mattock");

    private static final Set<Block> DEFAULT_NO_TOOL_BLOCKS = new HashSet<>();
    private static final Logger LOGGER = LogManager.getLogger();

    private static final Field HARVEST_TOOL_FIELD = Util.make(() -> {
        try
        {
            Field field = Block.class.getDeclaredField("harvestTool");
            field.setAccessible(true);
            return field;
        }
        catch (NoSuchFieldException e)
        {
            throw new RuntimeException("Unable to find Block#harvestTool, please report this to NTP!", e);
        }
    });

    private static final Field HARVEST_LEVEL_FIELD = Util.make(() -> {
        try
        {
            Field field = Block.class.getDeclaredField("harvestLevel");
            field.setAccessible(true);
            return field;
        }
        catch (NoSuchFieldException e)
        {
            throw new RuntimeException("Unable to find Block#harvestLevel, please report this to NTP!", e);
        }
    });

    private static final Field TOOL_CLASSES_FIELD = Util.make(() -> {
        try
        {
            Field field = Item.class.getDeclaredField("toolClasses");
            field.setAccessible(true);
            return field;
        }
        catch (NoSuchFieldException e)
        {
            throw new RuntimeException("Unable to find Item#toolClasses, please report this to NTP!", e);
        }
    });

    private static final Map<Material, ToolType> EXTRA_MATERIAL_TOOLS = Util.make(new ImmutableMap.Builder<Material, ToolType>(), builder -> {
        Stream.of(Material.PLANT, Material.WATER_PLANT, Material.REPLACEABLE_PLANT, Material.REPLACEABLE_WATER_PLANT, Material.BAMBOO, Material.BAMBOO_SAPLING, Material.CACTUS)
            .forEach(material -> builder.put(material, SWORD));
        Stream.of(Material.WOOD, Material.NETHER_WOOD)
            .forEach(material -> builder.put(material, ToolType.AXE));
        Stream.of(Material.STONE, Material.METAL, Material.HEAVY_METAL, Material.PISTON)
            .forEach(material -> builder.put(material, ToolType.PICKAXE));
        Stream.of(Material.DIRT, Material.VEGETABLE, Material.SAND, Material.CLAY, Material.TOP_SNOW, Material.SNOW)
            .forEach(material -> builder.put(material, ToolType.SHOVEL));
        Stream.of(Material.GRASS, Material.REPLACEABLE_FIREPROOF_PLANT, Material.SPONGE, Material.LEAVES)
            .forEach(material -> builder.put(material, ToolType.HOE));
    }).build();

    @SuppressWarnings("unchecked")
    public static void setup()
    {
        ForgeRegistries.BLOCKS.getValues()
            .forEach(block -> {

                AbstractBlockAccess blockAccess = (AbstractBlockAccess) block;
                AbstractBlock.Properties settings = blockAccess.getProperties();
                AbstractBlockPropertiesAccess settingsAccess = (AbstractBlockPropertiesAccess) settings;
                Material material = blockAccess.getMaterial();

                if (!settingsAccess.getRequiresCorrectToolForDrops())
                {
                    // The block by default has no tool. Flag it so we can refer to it later
                    DEFAULT_NO_TOOL_BLOCKS.add(block);
                }

                // Forcefully set everything to require a tool
                // Need to do both the block settings and the block state since the value is copied there for every state
                settings.requiresCorrectToolForDrops();
                block.getStateDefinition().getPossibleStates().forEach(state -> ((AbstractBlockStateAccess) state).setRequiresCorrectToolForDrops(true));

                // Add extra harvest levels and types to specific blocks
                // Apparently we "shouldn't be doing this". But that's kind of the whole point of this mod, so here we go!
                try
                {
                    if (HARVEST_TOOL_FIELD.get(block) == null && HARVEST_LEVEL_FIELD.getInt(block) == -1 && EXTRA_MATERIAL_TOOLS.containsKey(material))
                    {
                        HARVEST_TOOL_FIELD.set(block, EXTRA_MATERIAL_TOOLS.get(material));
                        HARVEST_LEVEL_FIELD.set(block, 0);
                    }
                }
                catch (IllegalAccessException e)
                {
                    LOGGER.warn("Unable to set harvest tool and level for block: {}. Cause: {}. Please report this to NTP!", block, e.getMessage());
                }
            });

        ForgeRegistries.ITEMS.getValues().forEach(item -> {
            // Directly add sword tool classes to sword items
            try
            {
                if (item instanceof SwordItem)
                {
                    Map<ToolType, Integer> toolClasses = (Map<ToolType, Integer>) TOOL_CLASSES_FIELD.get(item);
                    if (!toolClasses.containsKey(SWORD))
                    {
                        toolClasses.put(SWORD, ((SwordItem) item).getTier().getLevel());
                    }
                }
            }
            catch (IllegalAccessException e)
            {
                LOGGER.warn("Unable to add sword tool class for item: {}. Cause: {}. Please report this to NTP!", item, e.getMessage());
            }
        });
    }

    public static boolean doesBlockRequireNoToolByDefault(Block block)
    {
        return DEFAULT_NO_TOOL_BLOCKS.contains(block);
    }

    /**
     * This is a better version of {@link net.minecraftforge.common.ForgeHooks#canHarvestBlock(BlockState, PlayerEntity, IBlockReader, BlockPos)}
     */
    public static boolean canHarvest(BlockState state, PlayerEntity player)
    {
        if ((Config.SERVER.noBlockDropsWithoutCorrectTool.get() || !doesBlockRequireNoToolByDefault(state.getBlock())) && !ModTags.Blocks.ALWAYS_DROPS.contains(state.getBlock()))
        {
            ItemStack stack = player.getMainHandItem();
            ToolType tool = state.getHarvestTool();
            if (tool != null)
            {
                if (!stack.isEmpty())
                {
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
                    if (toolLevel >= harvestLevel)
                    {
                        return true;
                    }
                }
                return player.inventory.getSelected().isCorrectToolForDrops(state);
            }
        }
        return true;
    }

    private static int getExtraHarvestLevel(ItemStack stack, ToolType tool)
    {
        // Extra rules for tools
        if (stack.getToolTypes().contains(ToolType.PICKAXE) && tool == ToolType.SHOVEL)
        {
            // Pickaxes can function as basic shovels
            return 0;
        }
        if (ModTags.Items.KNIVES.contains(stack.getItem()) && tool == SWORD)
        {
            // Knives have the "sword" tool type which is used for plant materials
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