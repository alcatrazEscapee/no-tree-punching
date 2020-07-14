/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.util;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.TieredItem;
import net.minecraft.util.Unit;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.registries.ForgeRegistries;

import com.alcatrazescapee.notreepunching.Config;
import com.alcatrazescapee.notreepunching.common.ModTags;

/**
 * Hack all the materials so every material says it requires a tool
 * We then intercept the break check via typical forge methods (that wouldn't've been reached otherwise)
 */
public class MaterialHacks
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Set<Material> DEFAULT_NO_TOOL_MATERIALS = new HashSet<>();
    private static final ToolType SWORD = ToolType.get("sword");

    private static final Map<Material, ToolType> EXTRA_MATERIAL_TOOLS = Util.make(() -> {
        ImmutableMap.Builder<Material, ToolType> builder = ImmutableMap.builder();
        Stream.of(Material.PLANTS, Material.OCEAN_PLANT, Material.TALL_PLANTS, Material.SEA_GRASS, Material.BAMBOO, Material.BAMBOO_SAPLING, Material.CACTUS).forEach(material -> builder.put(material, SWORD));
        Stream.of(Material.WOOD, Material.LEAVES).forEach(material -> builder.put(material, ToolType.AXE));
        Stream.of(Material.ROCK, Material.IRON, Material.ANVIL, Material.PISTON).forEach(material -> builder.put(material, ToolType.PICKAXE));
        Stream.of(Material.EARTH, Material.GOURD, Material.SAND, Material.CLAY, Material.SNOW, Material.SNOW_BLOCK).forEach(material -> builder.put(material, ToolType.SHOVEL));
        return builder.build();
    });

    public static void setup()
    {
        ForgeRegistries.BLOCKS.getValues()
            .stream()
            .flatMap(block -> block.getStateContainer().getValidStates().stream())
            .forEach(state -> {
                Block block = state.getBlock();
                Material material = state.getMaterial();

                // Save it so we can refer to this later
                if (material.isToolNotRequired())
                {
                    DEFAULT_NO_TOOL_MATERIALS.add(material);
                }

                // The titular feature
                material.requiresNoTool = false;

                // Apparently we "shouldn't be doing this". But that's kind of the whole point of this mod, so here we go!
                runReallySafely(() -> {
                    Field harvestTool = Block.class.getDeclaredField("harvestTool");
                    Field harvestLevel = Block.class.getDeclaredField("harvestLevel");
                    harvestTool.setAccessible(true);
                    harvestLevel.setAccessible(true);
                    if (harvestTool.get(block) == null && harvestLevel.getInt(block) == -1 && EXTRA_MATERIAL_TOOLS.containsKey(material))
                    {
                        harvestTool.set(block, EXTRA_MATERIAL_TOOLS.get(material));
                        harvestLevel.set(block, 0);
                    }
                    return Unit.INSTANCE;
                }, () -> "Failed to add the sword tool type to block: " + block.getRegistryName());
            });

        ForgeRegistries.ITEMS.getValues().forEach(item -> {
            // Directly add sword tool classes to sword items
            runReallySafely(() -> {
                if (item instanceof SwordItem)
                {
                    Field toolClasses = Item.class.getDeclaredField("toolClasses");
                    toolClasses.setAccessible(true);
                    castReallySafely(toolClasses.get(item)).put(SWORD, ((SwordItem) item).getTier().getHarvestLevel());
                }
                return Unit.INSTANCE;
            }, () -> "Failed to add the sword tool class to item: " + item.getRegistryName());
        });
    }

    public static boolean doesMaterialRequireNoToolByDefault(Material material)
    {
        return DEFAULT_NO_TOOL_MATERIALS.contains(material);
    }

    /**
     * This is a better version of {@link net.minecraftforge.common.ForgeHooks#canHarvestBlock(BlockState, PlayerEntity, IBlockReader, BlockPos)}
     */
    public static boolean canHarvest(BlockState state, PlayerEntity player)
    {
        if ((Config.SERVER.noBlockDropsWithoutCorrectTool.get() || !DEFAULT_NO_TOOL_MATERIALS.contains(state.getMaterial())) && !ModTags.Blocks.ALWAYS_DROPS.contains(state.getBlock()))
        {
            ItemStack stack = player.getHeldItemMainhand();
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
                return player.inventory.canHarvestBlock(state);
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
                return ((TieredItem) stack.getItem()).getTier().getHarvestLevel();
            }
            else
            {
                return 0;
            }
        }
        return -1;
    }

    private static void runReallySafely(Callable<?> dangerousThing, Supplier<String> message)
    {
        try
        {
            dangerousThing.call();
        }
        catch (Exception e)
        {
            LOGGER.warn("Oh noes: " + message.get());
            LOGGER.debug("Stacktrace", e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <K, V> Map<K, V> castReallySafely(Object whatever)
    {
        return (Map<K, V>) whatever;
    }
}
