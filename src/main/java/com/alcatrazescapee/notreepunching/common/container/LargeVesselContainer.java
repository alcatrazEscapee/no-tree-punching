/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.common.container;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import com.alcatrazescapee.core.common.container.DeviceContainer;
import com.alcatrazescapee.core.common.inventory.SlotCallback;
import com.alcatrazescapee.core.util.CoreHelpers;
import com.alcatrazescapee.notreepunching.common.tile.LargeVesselTileEntity;

public class LargeVesselContainer extends DeviceContainer<LargeVesselTileEntity>
{
    private static final Logger LOGGER = LogManager.getLogger();

    public LargeVesselContainer(LargeVesselTileEntity tile, PlayerInventory playerInventory, int windowId)
    {
        super(ModContainers.LARGE_VESSEL.get(), tile, playerInventory, windowId);
    }

    @Override
    protected void addContainerSlots()
    {
        CoreHelpers.ifPresentOrElse(tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY), handler -> {
            for (int x = 0; x < 5; x++)
            {
                for (int y = 0; y < 3; y++)
                {
                    addSlot(new SlotCallback(tile, handler, x + 5 * y, 44 + x * 18, 20 + y * 18));
                }
            }
        }, () -> LOGGER.warn("Missing capability on large vessel at {}?", tile.getPos()));
    }
}
