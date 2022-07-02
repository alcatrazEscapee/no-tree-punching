package com.alcatrazescapee.notreepunching.util.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public interface ItemStackInventory
{
    /**
     * Can the inventory contain the given stack.
     */
    default boolean canContain(ItemStack stack)
    {
        return true;
    }

    /**
     * Get the current stack in {@code slot}.
     */
    default ItemStack get(int slot)
    {
        return slots().get(slot);
    }

    /**
     * Set the stack in {@code slot}.
     */
    default void set(int slot, ItemStack stack)
    {
        slots().set(slot, stack);
        modified();
    }

    /**
     * Remove {@code amount} from {@code slot}. Return the removed amount.
     */
    default ItemStack remove(int slot, int amount)
    {
        if (amount == 0 || get(slot).isEmpty())
        {
            return ItemStack.EMPTY;
        }
        final ItemStack current = get(slot);
        if (amount < current.getCount())
        {
            final ItemStack removed = current.copy();
            current.shrink(amount);
            removed.setCount(amount);
            modified();
            return removed;
        }
        else
        {
            set(slot, ItemStack.EMPTY);
            modified();
            return current;
        }
    }

    /**
     * @return The number of slots in the inventory.
     */
    default int size()
    {
        return slots().size();
    }

    /**
     * Called when the inventory is modified.
     */
    default void modified() {}

    /**
     * Internal access to the underlying slots.
     */
    NonNullList<ItemStack> slots();
}
