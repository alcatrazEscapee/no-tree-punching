package com.alcatrazescapee.notreepunching.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * This is a callback for various methods on an ItemStackHandler.
 * Methods are default to support overriding as many or as little as necessary
 *
 * {@link ItemStackHandlerCallback}
 */
public interface ISlotCallback
{
    /**
     * Gets the slot stack size
     */
    default int getSlotStackLimit(int slot)
    {
        return 64;
    }

    /**
     * Checks if an item is valid for a slot
     *
     * @return true if the item can be inserted
     */
    default boolean isItemValid(int slot, ItemStack stack)
    {
        return true;
    }

    /**
     * Called when a slot changed
     *
     * @param slot the slot index, or -1 if the call method had no specific slot
     */
    default void setAndUpdateSlots(int slot) {}

    /**
     * Called when a slot is taken from
     */
    default void onSlotTake(Player player, int slot, ItemStack stack) {}
}
