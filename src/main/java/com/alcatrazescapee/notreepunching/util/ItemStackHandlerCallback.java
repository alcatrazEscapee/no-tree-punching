/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import com.alcatrazescapee.notreepunching.common.container.ItemStackContainer;

/**
 * This is an item stack handler which delegates some logic back to the provided callback
 * It can also be used as a pass-through callback itself, for example, when using a slot which needs to call back to the original instance:
 * - {@link ItemStackItemHandler} is the callback for the contained {@link ItemStackHandlerCallback}
 * - In a {@link ItemStackContainer}, the capability is used as the callback to {@link SlotCallback}s, which then delegates all the way back to the capability provider.
 */
public class ItemStackHandlerCallback extends ItemStackHandler implements ISlotCallback
{
    private final ISlotCallback callback;

    public ItemStackHandlerCallback(ISlotCallback callback, int slots)
    {
        super(slots);
        this.callback = callback;
    }

    @Override
    public int getSlotLimit(int slot)
    {
        return callback.getSlotStackLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack)
    {
        return callback.isItemValid(slot, stack);
    }

    @Override
    protected void onContentsChanged(int slot)
    {
        callback.setAndUpdateSlots(slot);
    }

    @Override
    public int getSlotStackLimit(int slot)
    {
        return callback.getSlotStackLimit(slot);
    }

    @Override
    public void setAndUpdateSlots(int slot)
    {
        callback.setAndUpdateSlots(slot);
    }

    @Override
    public void onSlotTake(Player player, int slot, ItemStack stack)
    {
        callback.onSlotTake(player, slot, stack);
    }
}