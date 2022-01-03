/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.container;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.items.CapabilityItemHandler;

import com.alcatrazescapee.notreepunching.common.items.SmallVesselItem;
import com.alcatrazescapee.notreepunching.util.ISlotCallback;
import com.alcatrazescapee.notreepunching.util.SlotCallback;

public final class SmallVesselContainer extends ItemStackContainer
{
    public SmallVesselContainer(int windowId, Inventory playerInv, InteractionHand hand)
    {
        super(ModContainers.SMALL_VESSEL.get(), windowId, playerInv, hand);
        init(playerInv);
    }

    @Override
    protected void addContainerSlots()
    {
        getTargetStack().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
            if (handler instanceof ISlotCallback callback)
            {
                for (int x = 0; x < SmallVesselItem.SLOT_COLUMNS; x++)
                {
                    for (int y = 0; y < SmallVesselItem.SLOT_ROWS; y++)
                    {
                        addSlot(new SlotCallback(callback, handler, x + SmallVesselItem.SLOT_COLUMNS * y, 62 + x * 18, 20 + y * 18));
                    }
                }
            }
        });
    }
}