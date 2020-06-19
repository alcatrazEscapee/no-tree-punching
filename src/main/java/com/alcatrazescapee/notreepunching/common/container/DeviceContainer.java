/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Copyright (c) 2019. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import com.alcatrazescapee.notreepunching.common.tileentity.InventoryTileEntity;

/**
 * Generic container for use with {@link InventoryTileEntity}
 */
public abstract class DeviceContainer<T extends InventoryTileEntity> extends ModContainer
{
    protected final T tile;

    protected DeviceContainer(ContainerType<?> containerType, T tile, PlayerInventory playerInventory, int windowId)
    {
        super(containerType, windowId);

        this.tile = tile;

        addContainerSlots();
        addPlayerInventorySlots(playerInventory);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index)
    {
        Slot slot = inventorySlots.get(index);
        if (slot != null && slot.getHasStack())
        {
            ItemStack stack = slot.getStack();
            ItemStack stackCopy = stack.copy();

            int containerSlots = inventorySlots.size() - playerIn.inventory.mainInventory.size();
            if (index < containerSlots)
            {
                if (transferStackOutOfContainer(stack, containerSlots))
                {
                    return ItemStack.EMPTY;
                }
            }
            else
            {
                if (transferStackIntoContainer(stack, containerSlots))
                {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.getCount() == 0)
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
            slot.onTake(playerIn, stackCopy);
            return stackCopy;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn)
    {
        return tile.canInteractWith(playerIn);
    }

    public T getTileEntity()
    {
        return tile;
    }

    protected boolean transferStackOutOfContainer(ItemStack stack, int containerSlots)
    {
        return !mergeItemStack(stack, containerSlots, inventorySlots.size(), true);
    }

    protected boolean transferStackIntoContainer(ItemStack stack, int containerSlots)
    {
        return !mergeItemStack(stack, 0, containerSlots, false);
    }

    @FunctionalInterface
    public interface IFactory<T extends InventoryTileEntity, C extends DeviceContainer<T>>
    {
        C create(T tile, PlayerInventory playerInventory, int windowId);
    }
}
