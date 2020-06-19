/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Copyright (c) 2019. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.container;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

/**
 * Container for inventories from item stacks
 */
public class ItemStackContainer extends ModContainer
{
    protected final ItemStack stack;
    protected final PlayerEntity player;
    protected int itemIndex;
    protected int itemDragIndex;
    protected boolean isOffhand;

    protected ItemStackContainer(ContainerType<?> containerType, PlayerInventory playerInv, ItemStack stack, int windowId)
    {
        super(containerType, windowId);
        this.player = playerInv.player;
        this.stack = stack;
        this.itemDragIndex = playerInv.currentItem;

        if (stack == player.getHeldItemMainhand())
        {
            this.itemIndex = playerInv.currentItem + 27; // Mainhand opened inventory
            this.isOffhand = false;
        }
        else
        {
            this.itemIndex = -100; // Offhand, so ignore this rule
            this.isOffhand = true;
        }

        addContainerSlots();
        addPlayerInventorySlots(playerInv);
    }

    @Override
    @Nonnull
    public ItemStack transferStackInSlot(PlayerEntity player, int index)
    {
        // Slot that was clicked
        Slot slot = inventorySlots.get(index);
        if (slot != null && slot.getHasStack() && index != itemIndex)
        {
            ItemStack stack = slot.getStack();
            ItemStack stackCopy = stack.copy();

            // Begin custom transfer code here
            int containerSlots = inventorySlots.size() - player.inventory.mainInventory.size(); // number of slots in the container
            if (index < containerSlots)
            {
                // Transfer out of the container
                if (!this.mergeItemStack(stack, containerSlots, inventorySlots.size(), true))
                {
                    // Don't transfer anything
                    return ItemStack.EMPTY;
                }
            }
            // Transfer into the container
            else
            {
                if (!this.mergeItemStack(stack, 0, containerSlots, false))
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
            slot.onTake(player, stack);
            return stackCopy;
        }
        return ItemStack.EMPTY;
    }

    @Override
    @Nonnull
    public ItemStack slotClick(int slotID, int dragType, ClickType clickType, PlayerEntity player)
    {
        // Prevent moving of the item stack that is currently open
        if (slotID == itemIndex && (clickType == ClickType.QUICK_MOVE || clickType == ClickType.PICKUP || clickType == ClickType.THROW || clickType == ClickType.SWAP))
        {
            return ItemStack.EMPTY;
        }
        else if ((dragType == itemDragIndex) && clickType == ClickType.SWAP)
        {
            return ItemStack.EMPTY;
        }
        else
        {
            return super.slotClick(slotID, dragType, clickType, player);
        }
    }
}
