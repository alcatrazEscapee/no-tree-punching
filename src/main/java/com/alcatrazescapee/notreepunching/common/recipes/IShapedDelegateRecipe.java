package com.alcatrazescapee.notreepunching.common.recipes;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.IShapedRecipe;

public interface IShapedDelegateRecipe<C extends Container> extends Recipe<C>, IShapedRecipe<C>
{
    Recipe<C> delegate();

    @Override
    default NonNullList<ItemStack> getRemainingItems(C container)
    {
        return delegate().getRemainingItems(container);
    }

    @Override
    default boolean isIncomplete()
    {
        return delegate().isIncomplete();
    }

    @Override
    default boolean matches(C inv, Level level)
    {
        return delegate().matches(inv, level);
    }

    @Override
    default ItemStack assemble(C inv)
    {
        return delegate().assemble(inv);
    }

    @Override
    default boolean canCraftInDimensions(int width, int height)
    {
        return delegate().canCraftInDimensions(width, height);
    }

    @Override
    default ItemStack getResultItem()
    {
        return delegate().getResultItem();
    }

    @Override
    default NonNullList<Ingredient> getIngredients()
    {
        return delegate().getIngredients();
    }

    @Override
    default boolean isSpecial()
    {
        return delegate().isSpecial();
    }

    @Override
    default String getGroup()
    {
        return delegate().getGroup();
    }

    @Override
    default ItemStack getToastSymbol()
    {
        return delegate().getToastSymbol();
    }

    @Override
    default int getRecipeWidth()
    {
        return ((ShapedRecipe) delegate()).getWidth();
    }

    @Override
    default int getRecipeHeight()
    {
        return ((ShapedRecipe) delegate()).getHeight();
    }
}
