/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.integration.crafttweaker;

import net.minecraft.item.ItemStack;

import com.alcatrazescapee.notreepunching.common.items.ItemKnife;
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
    @ZenMethod
    public static void addKnifeGrassDrop(IItemStack stack)
    {
        CraftTweakerAPI.apply(new IAction()
        {
            @Override
            public void apply()
            {
                ItemKnife.addGrassDrop(CraftTweakerPlugin.toStack(stack));
            }

            @Override
            public String describe()
            {
                return "Adding Knife grass drop for " + stack.getDisplayName();
            }
        });
    }

    // Helper methods
    static ItemStack toStack(IItemStack iStack)
    {
        if (iStack == null)
            return ItemStack.EMPTY;
        return (ItemStack) iStack.getInternal();
    }

    // Craft Tweaker Util Methods

    static ItemStack toStack(IIngredient ingredient)
    {
        if (ingredient == null)
            return ItemStack.EMPTY;
        return (ItemStack) ingredient.getInternal();
    }
}
