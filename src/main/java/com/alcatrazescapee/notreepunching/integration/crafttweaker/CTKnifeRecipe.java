/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.integration.crafttweaker;

import java.util.Arrays;

import net.minecraft.item.ItemStack;

import com.alcatrazescapee.alcatrazcore.inventory.ingredient.IRecipeIngredient;
import com.alcatrazescapee.notreepunching.common.recipe.KnifeRecipe;
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
@ZenClass("mods.notreepunching.Knife")
public class CTKnifeRecipe
{
    @ZenMethod
    public static void add(IIngredient input, IItemStack... output)
    {
        KnifeRecipe recipe;
        ItemStack[] outputStack = Arrays.stream(output).map(CraftTweakerPlugin::toStack).toArray(ItemStack[]::new);
        if (input instanceof IOreDictEntry)
        {
            IOreDictEntry ore = (IOreDictEntry) input;
            recipe = new KnifeRecipe(ore.getName(), ore.getAmount(), outputStack);
        }
        else
        {
            recipe = new KnifeRecipe(CraftTweakerPlugin.toStack(input), outputStack);
        }
        CraftTweakerAPI.apply(new IAction()
        {
            @Override
            public void apply()
            {
                ModRecipes.addScheduledAction(() -> ModRecipes.KNIFE.add(recipe));
            }

            @Override
            public String describe()
            {
                return "Adding knife recipe for " + recipe.getName();
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
                ModRecipes.addScheduledAction(() -> ModRecipes.KNIFE.remove(ingredient));
            }

            @Override
            public String describe()
            {
                return "Removing knife recipe for " + ingredient.getName();
            }
        });
    }
}
