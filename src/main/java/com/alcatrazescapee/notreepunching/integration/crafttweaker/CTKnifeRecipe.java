/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.integration.crafttweaker;

import net.minecraft.item.ItemStack;

import com.alcatrazescapee.alcatrazcore.inventory.ingredient.IRecipeIngredient;
import com.alcatrazescapee.notreepunching.common.recipe.KnifeRecipe;
import com.alcatrazescapee.notreepunching.common.recipe.ModRecipes;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.notreepunching.Knife")
public class CTKnifeRecipe
{
    @ZenMethod
    public static void add(IItemStack input, IItemStack output)
    {
        KnifeRecipe recipe = new KnifeRecipe(CraftTweakerPlugin.toStack(input), CraftTweakerPlugin.toStack(output));
        CraftTweakerAPI.apply(new IAction()
        {
            @Override
            public void apply()
            {
                ModRecipes.KNIFE.add(recipe);
            }

            @Override
            public String describe()
            {
                return "Adding knife recipe for " + recipe.getName();
            }
        });
    }

    @ZenMethod
    public static void remove(IItemStack input)
    {
        ItemStack stack = CraftTweakerPlugin.toStack(input);
        CraftTweakerAPI.apply(new IAction()
        {
            @Override
            public void apply()
            {
                ModRecipes.KNIFE.remove(IRecipeIngredient.of(stack));
            }

            @Override
            public String describe()
            {
                return "Removing knife recipe for " + stack.getDisplayName();
            }
        });
    }
}
