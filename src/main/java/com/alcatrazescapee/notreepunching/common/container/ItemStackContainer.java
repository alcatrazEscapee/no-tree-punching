/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
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
        this.itemDragIndex = playerInv.selected;

        if (stack == player.getMainHandItem())
        {
            this.itemIndex = playerInv.selected + 27; // Mainhand opened inventory
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
    public ItemStack quickMoveStack(PlayerEntity player, int index)
    {
        // Slot that was clicked
        Slot slot = slots.get(index);
        if (slot != null && slot.hasItem() && index != itemIndex)
        {
            ItemStack stack = slot.getItem();
            ItemStack stackCopy = stack.copy();

            // Begin custom transfer code here
            int containerSlots = slots.size() - player.inventory.items.size(); // number of slots in the container
            if (index < containerSlots)
            {
                // Transfer out of the container
                if (!this.moveItemStackTo(stack, containerSlots, slots.size(), true))
                {
                    // Don't transfer anything
                    return ItemStack.EMPTY;
                }
            }
            // Transfer into the container
            else
            {
                if (!this.moveItemStackTo(stack, 0, containerSlots, false))
                {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.getCount() == 0)
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
            slot.onTake(player, stack);
            return stackCopy;
        }
        return ItemStack.EMPTY;
    }

    @Override
    @Nonnull
    public ItemStack clicked(int slotID, int dragType, ClickType clickType, PlayerEntity player)
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
            return super.clicked(slotID, dragType, clickType, player);
        }
    }
}