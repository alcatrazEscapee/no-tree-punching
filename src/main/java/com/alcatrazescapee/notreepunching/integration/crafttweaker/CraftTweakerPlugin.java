/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.integration.crafttweaker;

import java.util.function.Consumer;

import net.minecraft.item.ItemStack;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.notreepunching.NoTreePunching")
public class CraftTweakerPlugin
{
    // Helper methods
    public static ItemStack toStack(IItemStack iStack)
    {
        if (iStack == null)
            return ItemStack.EMPTY;
        return (ItemStack) iStack.getInternal();
    }

    public static ItemStack toStack(IIngredient ingredient)
    {
        if (ingredient == null)
            return ItemStack.EMPTY;
        return (ItemStack) ingredient.getInternal();
    }

    // Craft Tweaker Util Methods

    @ZenMethod
    public static void addKnifeGrassDrop(IItemStack stack)
    {
        CraftTweakerAPI.apply(new Add(toStack(stack)));
    }

    private static class Add implements IAction
    {
        private final ItemStack stack;

        public Add(ItemStack stack)
        {
            this.stack = stack;
        }

        @Override
        public void apply()
        {
            //KnifeRecipeHandler.addGrassDrop(stack);
        }

        @Override
        public String describe()
        {
            return "Adding Knife grass drop for " + stack.getDisplayName();
        }

    }

    static class RecipeAction<T> implements IAction
    {
        private Consumer<T> applier;
        private T recipe;

        RecipeAction(T recipe, Consumer<T> applier)
        {
            this.recipe = recipe;
            this.applier = applier;
        }

        @Override
        public void apply()
        {
            applier.accept(recipe);
        }

        @Override
        public String describe()
        {
            return null;
        }
    }
}
