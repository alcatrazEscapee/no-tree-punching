package com.alcatrazescapee.notreepunching.common.container;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;

import com.alcatrazescapee.notreepunching.common.items.SmallVesselItem;
import com.alcatrazescapee.notreepunching.util.inventory.InventorySlot;
import com.alcatrazescapee.notreepunching.util.inventory.ItemStackAttachedInventory;

public final class SmallVesselContainer extends ItemStackContainer
{
    public SmallVesselContainer(int windowId, Inventory playerInventory, InteractionHand hand)
    {
        super(ModContainers.SMALL_VESSEL.get(), windowId, playerInventory, hand);
        init(playerInventory);
    }

    @Override
    protected void addContainerSlots()
    {
        final ItemStackAttachedInventory inventory = SmallVesselItem.INVENTORY.create(player.getItemInHand(hand));
        for (int x = 0; x < SmallVesselItem.SLOT_COLUMNS; x++)
        {
            for (int y = 0; y < SmallVesselItem.SLOT_ROWS; y++)
            {
                addSlot(new InventorySlot(inventory, x + SmallVesselItem.SLOT_COLUMNS * y, 62 + x * 18, 20 + y * 18));
            }
        }
    }
}