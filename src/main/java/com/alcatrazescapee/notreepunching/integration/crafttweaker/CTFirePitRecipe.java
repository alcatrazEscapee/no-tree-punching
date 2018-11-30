/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.integration.crafttweaker;

import net.minecraft.item.ItemStack;

import com.alcatrazescapee.alcatrazcore.inventory.ingredient.IRecipeIngredient;
import com.alcatrazescapee.notreepunching.common.recipe.FirePitRecipe;
import com.alcatrazescapee.notreepunching.common.recipe.ModRecipes;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.oredict.IOreDictEntry;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@SuppressWarnings("unused")
@ZenClass("mods.notreepunching.FirePit")
public class CTFirePitRecipe
{
    @ZenMethod
    public static void add(IIngredient input, IItemStack output)
    {
        FirePitRecipe recipe;
        ItemStack outputStack = CraftTweakerPlugin.toStack(output);
        if (input instanceof IOreDictEntry)
        {
            IOreDictEntry ore = (IOreDictEntry) input;
            recipe = new FirePitRecipe(outputStack, ore.getName(), ore.getAmount());
        }
        else
        {
            recipe = new FirePitRecipe(outputStack, CraftTweakerPlugin.toStack(input));
        }
        CraftTweakerAPI.apply(new IAction()
        {
            @Override
            public void apply()
            {
                ModRecipes.addScheduledAction(() -> ModRecipes.FIRE_PIT.add(recipe));
            }

            @Override
            public String describe()
            {
                return "Adding Fire pit recipe for " + recipe.getName();
            }
        });
    }

    @ZenMethod
    public static void remove(IIngredient input)
    {
        IRecipeIngredient ingredient;
        if (input instanceof IOreDictEntry)
        {
            IOreDictEntry ore = (IOreDictEntry) input;
            ingredient = IRecipeIngredient.of(ore.getName(), ore.getAmount());
        }
        else
        {
            ingredient = IRecipeIngredient.of(CraftTweakerPlugin.toStack(input));
        }
        CraftTweakerAPI.apply(new IAction()
        {
            @Override
            public void apply()
            {
                ModRecipes.addScheduledAction(() -> ModRecipes.FIRE_PIT.remove(ingredient));
            }

            @Override
            public String describe()
            {
                return "Removing Fire pit recipe for " + ingredient.getName();
            }
        });
    }
}
