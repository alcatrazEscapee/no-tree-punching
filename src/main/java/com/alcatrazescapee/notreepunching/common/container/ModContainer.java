/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.container;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public abstract class ModContainer extends AbstractContainerMenu
{
    protected int containerSlots; // The number of slots in the container (not including the player inventory)

    protected ModContainer(MenuType<?> type, int windowId)
    {
        super(type, windowId);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index)
    {
        final Slot slot = slots.get(index);
        if (slot.hasItem()) // Only move an item when the index clicked has any contents
        {
            final ItemStack stack = slot.getItem(); // The item in the current slot
            final ItemStack original = stack.copy(); // The original amount in the slot
            if (moveStack(stack, index))
            {
                return ItemStack.EMPTY;
            }

            if (stack.getCount() == original.getCount())
            {
                return ItemStack.EMPTY;
            }

            // Handle updates
            if (stack.isEmpty())
            {
                slot.set(ItemStack.EMPTY);
            }
            else
            {
                slot.setChanged();
            }

            slot.onTake(player, stack);
            return original;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player playerIn)
    {
        return true;
    }

    /**
     * Handles the actual movement of stacks in {@link #quickMoveStack(Player, int)} with as little boilerplate as possible.
     * The default implementation only moves stacks between the main inventory and the hotbar.
     *
     * @return {@code true} if no movement is possible, or the result of {@code !moveItemStackTo(...) || ...}
     */
    protected boolean moveStack(ItemStack stack, int slotIndex)
    {
        return switch (typeOf(slotIndex))
            {
                case CONTAINER -> true;
                case HOTBAR -> !moveItemStackTo(stack, containerSlots, containerSlots + 27, false);
                case MAIN_INVENTORY -> !moveItemStackTo(stack, containerSlots + 27, containerSlots + 36, false);
            };
    }

    protected void init(Inventory playerInventory)
    {
        addContainerSlots();
        containerSlots = slots.size();
        addPlayerInventorySlots(playerInventory);
    }

    /**
     * Adds container slots.
     * These are added before the player inventory (and as such, the player inventory will be shifted upwards by the number of slots added here.
     */
    protected void addContainerSlots() {}

    /**
     * Adds the player inventory slots to the container.
     */
    protected final void addPlayerInventorySlots(Inventory playerInv)
    {
        // Main Inventory. Indexes [0, 27)
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        // Hotbar. Indexes [27, 36)
        for (int k = 0; k < 9; k++)
        {
            addSlot(new Slot(playerInv, k, 8 + k * 18, 142));
        }
    }

    protected final IndexType typeOf(int index)
    {
        if (index < containerSlots)
        {
            return IndexType.CONTAINER;
        }
        else if (index < containerSlots + 27)
        {
            return IndexType.MAIN_INVENTORY;
        }
        return IndexType.HOTBAR;
    }

    public enum IndexType
    {
        CONTAINER,
        MAIN_INVENTORY,
        HOTBAR
    }
}