/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.recipes;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public interface IDelegateRecipe<C extends IInventory> extends IRecipe<C>
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
}
