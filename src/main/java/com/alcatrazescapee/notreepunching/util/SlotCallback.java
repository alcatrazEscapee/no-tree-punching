/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Copyright (c) 2019. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

/**
 * A Slot that delegates to the callback for validity and stack size checks
 */
public class SlotCallback extends SlotItemHandler
{
    private final ISlotCallback callback;

    public SlotCallback(ISlotCallback callback, IItemHandler inventory, int index, int x, int y)
    {
        super(inventory, index, x, y);

        this.callback = callback;
    }

    @Override
    public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack)
    {
        callback.onSlotTake(thePlayer, getSlotIndex(), stack);
        return super.onTake(thePlayer, stack);
    }

    @Override
    public void onSlotChanged()
    {
        callback.setAndUpdateSlots(getSlotIndex());
        super.onSlotChanged();
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return callback.isItemValid(getSlotIndex(), stack) && super.isItemValid(stack);
    }

    @Override
    public int getSlotStackLimit()
    {
        return Math.min(callback.getSlotStackLimit(getSlotIndex()), super.getSlotStackLimit());
    }
}
