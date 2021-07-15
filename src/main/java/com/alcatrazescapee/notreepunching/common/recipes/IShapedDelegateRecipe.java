/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.recipes;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.IShapedRecipe;

public interface IShapedDelegateRecipe<C extends IInventory> extends IRecipe<C>, IShapedRecipe<C>
{
    IRecipe<C> getDelegate();

    @Override
    default boolean matches(C inv, World worldIn)
    {
        return getDelegate().matches(inv, worldIn);
    }

    @Override
    default ItemStack assemble(C inv)
    {
        return getDelegate().assemble(inv);
    }

    @Override
    default boolean canCraftInDimensions(int width, int height)
    {
        return getDelegate().canCraftInDimensions(width, height);
    }

    @Override
    default ItemStack getResultItem()
    {
        return getDelegate().getResultItem();
    }

    @Override
    default NonNullList<Ingredient> getIngredients()
    {
        return getDelegate().getIngredients();
    }

    @Override
    default boolean isSpecial()
    {
        return getDelegate().isSpecial();
    }

    @Override
    default String getGroup()
    {
        return getDelegate().getGroup();
    }

    @Override
    default ItemStack getToastSymbol()
    {
        return getDelegate().getToastSymbol();
    }

    @Override
    default int getRecipeWidth()
    {
        return ((ShapedRecipe) getDelegate()).getWidth();
    }

    @Override
    default int getRecipeHeight()
    {
        return ((ShapedRecipe) getDelegate()).getHeight();
    }
}
