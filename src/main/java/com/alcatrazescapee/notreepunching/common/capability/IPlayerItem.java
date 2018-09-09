/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.common.capability;

import net.minecraft.item.ItemStack;

public interface IPlayerItem
{
    ItemStack getStack();

    void setStack(ItemStack stack);
}
