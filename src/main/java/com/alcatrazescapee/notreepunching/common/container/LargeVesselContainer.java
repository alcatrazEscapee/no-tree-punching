/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.container;

import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.items.CapabilityItemHandler;

import com.alcatrazescapee.notreepunching.common.blockentity.LargeVesselBlockEntity;
import com.alcatrazescapee.notreepunching.util.SlotCallback;

public final class LargeVesselContainer extends DeviceContainer<LargeVesselBlockEntity>
{
    public LargeVesselContainer(LargeVesselBlockEntity tile, Inventory playerInventory, int windowId)
    {
        super(ModContainers.LARGE_VESSEL.get(), tile, playerInventory, windowId);
        init(playerInventory);
    }

    @Override
    protected void addContainerSlots()
    {
        entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
            for (int x = 0; x < LargeVesselBlockEntity.SLOT_COLUMNS; x++)
            {
                for (int y = 0; y < LargeVesselBlockEntity.SLOT_ROWS; y++)
                {
                    addSlot(new SlotCallback(entity, handler, x + 5 * y, 44 + x * 18, 20 + y * 18));
                }
            }
        });
    }
}