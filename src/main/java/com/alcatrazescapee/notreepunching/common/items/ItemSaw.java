/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.common.items;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;

import com.alcatrazescapee.alcatrazcore.item.tool.ItemAxeCore;

@ParametersAreNonnullByDefault
public class ItemSaw extends ItemAxeCore
{
    public ItemSaw(ToolMaterial material)
    {
        super(material, Math.max(material.getAttackDamage() - 1.0f, 1.0f), -2.8f);

        setContainerItem(this);
    }

    @Override
    @Nonnull
    public ItemStack getContainerItem(ItemStack stack)
    {
        ItemStack copy = stack.copy();
        copy.setItemDamage(copy.getItemDamage() + 1);
        return copy.getItemDamage() >= copy.getMaxDamage() ? ItemStack.EMPTY : copy;
    }

    @Override
    public boolean hasContainerItem(ItemStack stack)
    {
        return true;
    }
}
