/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.integration.crafttweaker;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;

import com.alcatrazescapee.notreepunching.util.HarvestBlockHandler;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@SuppressWarnings("unused")
@ZenClass("mods.notreepunching.NoTreePunching")
public final class CraftTweakerPlugin
{
    @ZenMethod
    public static void addKnifeGrassDrop(IItemStack stack)
    {
        CraftTweakerAPI.apply(new IAction()
        {
            @Override
            public void apply()
            {
                HarvestBlockHandler.addGrassDrop(CraftTweakerPlugin.toStack(stack));
            }

            @Override
            public String describe()
            {
                return "Adding Knife grass drop for " + stack.getDisplayName();
            }
        });
    }

    @Nonnull
    static ItemStack toStack(IIngredient ingredient)
    {
        if (!(ingredient instanceof IItemStack))
            return ItemStack.EMPTY;
        Object obj = ingredient.getInternal();
        return obj instanceof ItemStack ? (ItemStack) obj : ItemStack.EMPTY;
    }
}
