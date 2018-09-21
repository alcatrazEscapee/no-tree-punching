/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.common.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import com.alcatrazescapee.alcatrazcore.inventory.container.ContainerItemStack;

public class ContainerSmallVessel extends ContainerItemStack
{
    public ContainerSmallVessel(InventoryPlayer playerInv, ItemStack stack)
    {
        super(playerInv, stack, 4);
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
}
