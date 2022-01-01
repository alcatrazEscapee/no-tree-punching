/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.container;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.items.CapabilityItemHandler;

import com.alcatrazescapee.notreepunching.common.blockentity.LargeVesselBlockEntity;
import com.alcatrazescapee.notreepunching.util.Helpers;
import com.alcatrazescapee.notreepunching.util.SlotCallback;

public class LargeVesselContainer extends DeviceContainer<LargeVesselBlockEntity>
{
    private static final Logger LOGGER = LogManager.getLogger();

    public LargeVesselContainer(LargeVesselBlockEntity tile, Inventory playerInventory, int windowId)
    {
        super(ModContainers.LARGE_VESSEL.get(), tile, playerInventory, windowId);
    }

    @Override
    protected void addContainerSlots()
    {
        Helpers.ifPresentOrElse(tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY), handler -> {
            for (int x = 0; x < LargeVesselBlockEntity.SLOT_COLUMNS; x++)
            {
                for (int y = 0; y < LargeVesselBlockEntity.SLOT_ROWS; y++)
                {
                    addSlot(new SlotCallback(tile, handler, x + 5 * y, 44 + x * 18, 20 + y * 18));
                }
            }
        }, () -> LOGGER.warn("Missing capability on large vessel at {}?", tile.getBlockPos()));
    }
}