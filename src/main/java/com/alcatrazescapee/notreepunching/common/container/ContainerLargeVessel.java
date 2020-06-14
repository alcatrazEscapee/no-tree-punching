/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.common.container;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import com.alcatrazescapee.core.inventory.container.ContainerTileInventory;
import com.alcatrazescapee.core.inventory.slot.SlotTileCore;
import com.alcatrazescapee.notreepunching.common.tile.LargeVesselTileEntity;

@ParametersAreNonnullByDefault
public class ContainerLargeVessel extends ContainerTileInventory<LargeVesselTileEntity>
{
    public ContainerLargeVessel(InventoryPlayer playerInv, LargeVesselTileEntity tile)
    {
        super(playerInv, tile);
    }

    @Override
    protected void addContainerSlots()
    {
        IItemHandler cap = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (cap != null)
        {
            for (int i = 0; i < 3; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    addSlotToContainer(new SlotTileCore(cap, i + 3 * j, 62 + i * 18, 20 + j * 18, tile));
                }
            }
        }
    }
}
