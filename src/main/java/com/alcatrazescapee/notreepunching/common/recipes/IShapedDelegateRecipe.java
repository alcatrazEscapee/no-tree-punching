/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.recipes;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.IShapedRecipe;

public interface IShapedDelegateRecipe<C extends Container> extends Recipe<C>, IShapedRecipe<C>
{
    Recipe<C> getDelegate();

    @Override
    default boolean matches(C inv, Level worldIn)
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
