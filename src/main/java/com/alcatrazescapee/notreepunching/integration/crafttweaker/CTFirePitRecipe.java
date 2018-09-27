/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.integration.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass("mods.notreepunching.Firepit")
public class CTFirePitRecipe
{
    // todo: ALL OF THIS!!!
    /*@ZenMethod
    public static void add(IItemStack input, IItemStack output)
    {
        FirePitRecipe recipe = new FirePitRecipe(CTPluginHelper.toStack(input),
                CTPluginHelper.toStack(output));
        CraftTweakerAPI.apply(new CTFirepitRecipe.Add(recipe));
    }

    @ZenMethod
    public static void remove(IItemStack output)
    {
        ItemStack stack = CTPluginHelper.toStack(output);
        CraftTweakerAPI.apply(new CTFirepitRecipe.Remove(stack));
    }

    private static class Add implements IAction
    {
        private final FirepitRecipe recipe;

        public Add(FirepitRecipe recipe)
        {
            this.recipe = recipe;
        }

        @Override
        public void apply()
        {
            FirepitRecipeHandler.addEntry(recipe, true);
        }

        @Override
        public String describe()
        {
            return "Adding Firepit Recipe for " + recipe.getInput().getDisplayName();
        }

    }

    private static class Remove implements IAction
    {
        private final ItemStack stack;

        public Remove(ItemStack stack)
        {
            this.stack = stack;
        }

        @Override
        public void apply()
        {
            FirepitRecipeHandler.addEntry(new FirepitRecipe(ItemStack.EMPTY, stack), false);
        }

        @Override
        public String describe()
        {
            return "Removing Firepit Recipe for" + stack.getDisplayName();
        }
    }*/
}
