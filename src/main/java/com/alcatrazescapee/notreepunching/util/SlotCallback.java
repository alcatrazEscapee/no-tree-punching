/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
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
    public void setChanged()
    {
        callback.setAndUpdateSlots(getSlotIndex());
        super.setChanged();
    }

    @Override
    public boolean mayPlace(ItemStack stack)
    {
        return callback.isItemValid(getSlotIndex(), stack) && super.mayPlace(stack);
    }

    @Override
    public int getMaxStackSize()
    {
        return Math.min(callback.getSlotStackLimit(getSlotIndex()), super.getMaxStackSize());
    }
}