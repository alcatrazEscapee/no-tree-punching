/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.IForgeRegistryModifiable;

import com.alcatrazescapee.alcatrazcore.inventory.crafting.InventoryCraftingEmpty;
import com.alcatrazescapee.alcatrazcore.util.CoreHelpers;
import com.alcatrazescapee.notreepunching.ModConfig;
import com.alcatrazescapee.notreepunching.NoTreePunching;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

@ParametersAreNonnullByDefault
public final class WoodRecipeHandler
{
    private static final Map<ItemStack, ItemStack> MAP = new HashMap<>();

    public static void registerRecipes(RegistryEvent.Register<IRecipe> event)
    {
        IForgeRegistryModifiable<IRecipe> registry = (IForgeRegistryModifiable<IRecipe>) event.getRegistry();
        findMatchingRecipes(registry.getValuesCollection(), registry);

        // Remove stick recipe as well
        if (ModConfig.GENERAL.replaceLogRecipes)
        {
            registry.remove(new ResourceLocation("minecraft:stick"));
        }
    }

    /**
     * Try and catch any late recipes registered
     * We can't remove recipes here, but we can still add the chopping + saw recipes
     */
    public static void postInit()
    {
        findMatchingRecipes(ForgeRegistries.RECIPES.getValuesCollection(), ((IForgeRegistryModifiable<IRecipe>) ForgeRegistries.RECIPES));
    }

    static boolean isLog(World world, BlockPos pos, IBlockState state)
    {
        ItemStack stack = state.getBlock().getPickBlock(state, null, world, pos, null);
        return MAP.keySet().stream().anyMatch(x -> CoreHelpers.doStacksMatch(stack, x));
    }

    static boolean isPlank(World world, BlockPos pos, IBlockState state)
    {
        ItemStack stack = state.getBlock().getPickBlock(state, null, world, pos, null);
        return MAP.values().stream().anyMatch(x -> CoreHelpers.doStacksMatch(stack, x));
    }

    @Nullable
    static ItemStack getPlankForLog(World world, BlockPos pos, IBlockState state)
    {
        ItemStack search = state.getBlock().getPickBlock(state, null, world, pos, null);
        return MAP.entrySet().stream().filter(x -> CoreHelpers.doStacksMatch(x.getKey(), search)).map(Map.Entry::getValue).findFirst().orElse(null);
    }

    private static void findMatchingRecipes(Collection<IRecipe> recipes, IForgeRegistryModifiable<IRecipe> registry)
    {
        InventoryCraftingEmpty tempCrafting = new InventoryCraftingEmpty(3, 3);
        NonNullList<ItemStack> logs = OreDictionary.getOres("logWood", false);

        int logsFound = 0;

        for (ItemStack stack : logs)
        {
            Block block = Block.getBlockFromItem(stack.getItem());
            if (block == Blocks.AIR) continue;

            if (stack.getMetadata() == OreDictionary.WILDCARD_VALUE)
            {
                NonNullList<ItemStack> variants = NonNullList.create();
                block.getSubBlocks(block.getCreativeTab(), variants);

                for (ItemStack log : variants)
                {
                    tempCrafting.setInventorySlotContents(0, log);

                    IRecipe recipe = tryFindRecipeSafely(recipes, tempCrafting);
                    if (recipe != null)
                    {
                        ItemStack plank = recipe.getCraftingResult(tempCrafting);
                        if (!plank.isEmpty())
                        {
                            MAP.put(log.copy(), plank.copy());
                            if (ModConfig.GENERAL.replaceLogRecipes)
                            {
                                registry.remove(recipe.getRegistryName());
                            }
                            ResourceLocation loc = new ResourceLocation(MOD_ID, "saw_planks_" + ++logsFound);
                            registry.register(new ShapedOreRecipe(loc, plank, "S", "W", 'S', "toolSaw", 'W', log).setRegistryName(loc));
                        }
                    }
                }
            }
            else
            {
                ItemStack log = stack.copy();
                tempCrafting.setInventorySlotContents(0, log);

                IRecipe recipe = tryFindRecipeSafely(recipes, tempCrafting);
                if (recipe != null)
                {
                    ItemStack plank = recipe.getCraftingResult(tempCrafting);
                    if (!plank.isEmpty())
                    {
                        MAP.put(log.copy(), plank.copy());
                        if (ModConfig.GENERAL.replaceLogRecipes)
                        {
                            registry.remove(recipe.getRegistryName());
                        }

                        ResourceLocation loc = new ResourceLocation(MOD_ID, "saw_planks_" + ++logsFound);
                        registry.register(new ShapedOreRecipe(loc, plank, "S", "W", 'S', "toolSaw", 'W', log).setRegistryName(loc));
                    }
                }
            }
        }
        NoTreePunching.getLog().info("Found and replaced {} Log -> Planks recipes with Saw + Log -> Plank recipes.", logsFound);
    }

    @Nullable
    private static IRecipe tryFindRecipeSafely(Collection<IRecipe> recipes, InventoryCrafting tempCrafting)
    {
        try
        {
            //noinspection ConstantConditions
            return recipes.stream().filter(x -> x.matches(tempCrafting, null)).findFirst().orElse(null);
        }
        catch (Exception e)
        {
            // Ignore, likely cased by world == null (which we don't have access to yet)
            return null;
        }
    }
}
