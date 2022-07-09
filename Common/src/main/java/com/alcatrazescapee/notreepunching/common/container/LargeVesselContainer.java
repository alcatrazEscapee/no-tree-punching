package com.alcatrazescapee.notreepunching.common.container;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import com.alcatrazescapee.notreepunching.common.blockentity.LargeVesselBlockEntity;
import com.alcatrazescapee.notreepunching.util.inventory.InventorySlot;

public final class LargeVesselContainer extends ModContainer
{
    private final LargeVesselBlockEntity vessel;

    public LargeVesselContainer(LargeVesselBlockEntity vessel, Inventory playerInventory, int windowId)
    {
        super(ModContainers.LARGE_VESSEL.get(), windowId);
        this.vessel = vessel;
        init(playerInventory);
    }

    @Override
    protected void addContainerSlots()
    {
        for (int x = 0; x < LargeVesselBlockEntity.SLOT_COLUMNS; x++)
        {
            for (int y = 0; y < LargeVesselBlockEntity.SLOT_ROWS; y++)
            {
                addSlot(new InventorySlot(vessel, x + 5 * y, 44 + x * 18, 20 + y * 18));
            }
        }
    }

    @Override
    protected boolean moveStack(ItemStack stack, int slotIndex)
    {
        return switch (typeOf(slotIndex))
            {
                case MAIN_INVENTORY, HOTBAR -> !moveItemStackTo(stack, 0, LargeVesselBlockEntity.SLOTS, false);
                case CONTAINER -> !moveItemStackTo(stack, containerSlots, slots.size(), false);
            };
    }
}