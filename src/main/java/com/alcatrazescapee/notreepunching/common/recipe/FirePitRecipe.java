/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.common.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import com.alcatrazescapee.alcatrazcore.recipe.RecipeCore;

import static com.alcatrazescapee.notreepunching.ModConstants.MOD_ID;

public class FirePitRecipe extends RecipeCore
{
    public static final ResourceLocation ID = new ResourceLocation(MOD_ID, "recipe/fire_pit");

    public FirePitRecipe(ItemStack output, ItemStack inputStack)
    {
        super(output, inputStack);
    }

    public FirePitRecipe(ItemStack output, String inputOre, int inputAmount)
    {
        super(output, inputOre, inputAmount);
    }

    @Override
    public ResourceLocation getID()
    {
        return ID;
    }
}
