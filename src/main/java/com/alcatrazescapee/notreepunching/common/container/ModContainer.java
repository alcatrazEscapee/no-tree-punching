/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.container;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public abstract class ModContainer extends Container
{
    protected ModContainer(@Nullable ContainerType<?> type, int id)
    {
        super(type, id);
    }

    @Override
    @Nonnull
    public ItemStack transferStackInSlot(PlayerEntity player, int index)
    {
        ItemStack stackCopy = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack stack = slot.getStack();
            stackCopy = stack.copy();

            if (index < 27)
            {
                if (!this.mergeItemStack(stack, 27, 36, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else
            {
                if (!this.mergeItemStack(stack, 0, 27, false))
                {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (stack.getCount() == stackCopy.getCount())
            {
                return ItemStack.EMPTY;
            }

            ItemStack stackTake = slot.onTake(player, stack);
            if (index == 0)
            {
                player.dropItem(stackTake, false);
            }
        }

        return stackCopy;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn)
    {
        return true;
    }

    protected void addContainerSlots() {}

    protected void addPlayerInventorySlots(PlayerInventory playerInv)
    {
        // Add Player Inventory Slots
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; k++)
        {
            addSlot(new Slot(playerInv, k, 8 + k * 18, 142));
        }
    }
}
