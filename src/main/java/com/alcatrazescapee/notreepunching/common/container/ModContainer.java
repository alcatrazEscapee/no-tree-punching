/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.container;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public abstract class ModContainer extends AbstractContainerMenu
{
    protected ModContainer(@Nullable MenuType<?> type, int id)
    {
        super(type, id);
    }

    @Override
    @Nonnull
    public ItemStack quickMoveStack(Player player, int index)
    {
        ItemStack stackCopy = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem())
        {
            ItemStack stack = slot.getItem();
            stackCopy = stack.copy();

            if (index < 27)
            {
                if (!this.moveItemStackTo(stack, 27, 36, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else
            {
                if (!this.moveItemStackTo(stack, 0, 27, false))
                {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty())
            {
                slot.set(ItemStack.EMPTY);
            }
            else
            {
                slot.setChanged();
            }

            if (stack.getCount() == stackCopy.getCount())
            {
                return ItemStack.EMPTY;
            }

            ItemStack stackTake = slot.onTake(player, stack);
            if (index == 0)
            {
                player.drop(stackTake, false);
            }
        }

        return stackCopy;
    }

    @Override
    public boolean stillValid(Player playerIn)
    {
        return true;
    }

    protected void addContainerSlots() {}

    protected void addPlayerInventorySlots(Inventory playerInv)
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