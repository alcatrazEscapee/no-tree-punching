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

import com.alcatrazescapee.alcatrazcore.inventory.container.ContainerTileInventory;
import com.alcatrazescapee.alcatrazcore.inventory.slot.SlotOutput;
import com.alcatrazescapee.alcatrazcore.inventory.slot.SlotTileCore;
import com.alcatrazescapee.notreepunching.common.tile.TileFirePit;

@ParametersAreNonnullByDefault
public class ContainerFirePit extends ContainerTileInventory<TileFirePit>
{
    public ContainerFirePit(InventoryPlayer playerInv, TileFirePit tile)
    {
        super(playerInv, tile);
    }

    @Override
    protected void addContainerSlots()
    {
        IItemHandler inventory = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        if (inventory != null)
        {
            addSlotToContainer(new SlotTileCore(inventory, 0, 80, 59, tile)); // Fuel slot
            addSlotToContainer(new SlotTileCore(inventory, 1, 52, 23, tile)); // Input slot
            addSlotToContainer(new SlotOutput(inventory, 2, 108, 23));
        }
    }
}
