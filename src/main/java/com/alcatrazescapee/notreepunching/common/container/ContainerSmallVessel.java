/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.common.container;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import com.alcatrazescapee.core.inventory.container.ContainerItemStack;

@ParametersAreNonnullByDefault
public class ContainerSmallVessel extends ContainerItemStack
{
    private final int itemDragIndex;

    public ContainerSmallVessel(InventoryPlayer playerInv, ItemStack stack)
    {
        super(playerInv, stack, 4);

        this.itemDragIndex = playerInv.currentItem;
    }

    @Override
    protected void addContainerSlots()
    {
        IItemHandler cap = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (cap != null)
        {
            addSlotToContainer(new SlotItemHandler(cap, 0, 71, 23));
            addSlotToContainer(new SlotItemHandler(cap, 1, 89, 23));
            addSlotToContainer(new SlotItemHandler(cap, 2, 71, 41));
            addSlotToContainer(new SlotItemHandler(cap, 3, 89, 41));
        }
    }

    @Nonnull
    @Override
    public ItemStack slotClick(int slotID, int dragType, ClickType clickType, EntityPlayer player)
    {
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
