/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.integration.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass("mods.notreepunching.Knife")
public class CTKnifeRecipe
{
    // todo: ALL OF THIS!!!
    /*@ZenMethod
    public static void add(IItemStack input, IItemStack output)
    {
        KnifeRecipe recipe = new KnifeRecipe(CraftTweakerPlugin.toStack(input), CraftTweakerPlugin.toStack(output));
        CraftTweakerAPI.apply(new Add(recipe));
    }

    @ZenMethod
    public static void remove(IItemStack input)
    {
        ItemStack stack = CraftTweakerPlugin.toStack(input);
        CraftTweakerAPI.apply(new Remove(stack));
    }

    private static class Add implements IAction
    {
        private final KnifeRecipe recipe;

        public Add(KnifeRecipe recipe)
        {
            this.recipe = recipe;
        }

        @Override
        public void apply()
        {
            ModRecipes.KNIFE.apply(recipe, true);
        }

        @Override
        public String describe()
        {
            return "Adding Knife Recipe for " + recipe.getName();
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
            KnifeRecipeManager.apply(new KnifeRecipe(stack, ItemStack.EMPTY), false);
        }

        @Override
        public String describe()
        {
            return "Removing Knife Recipe for" + stack.getDisplayName();
        }
    }*/

}
