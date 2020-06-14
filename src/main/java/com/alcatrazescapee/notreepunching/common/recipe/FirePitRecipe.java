/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.common.recipe;

import net.minecraft.item.ItemStack;

import com.alcatrazescapee.core.inventory.recipe.RecipeCore;

public class FirePitRecipe extends RecipeCore
{
    public FirePitRecipe(ItemStack output, ItemStack inputStack)
    {
        super(output, inputStack);
    }

    public FirePitRecipe(ItemStack output, String inputOre, int inputAmount)
    {
        super(output, inputOre, inputAmount);
    }
}
