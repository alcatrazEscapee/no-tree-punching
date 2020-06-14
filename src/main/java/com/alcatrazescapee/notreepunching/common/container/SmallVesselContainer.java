/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.common.container;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraftforge.items.CapabilityItemHandler;

import com.alcatrazescapee.core.common.container.ItemStackContainer;
import com.alcatrazescapee.core.common.inventory.ISlotCallback;
import com.alcatrazescapee.core.common.inventory.SlotCallback;
import com.alcatrazescapee.core.util.CoreHelpers;
import com.alcatrazescapee.notreepunching.common.items.ModItems;

public class SmallVesselContainer extends ItemStackContainer
{
    private static final Logger LOGGER = LogManager.getLogger();

    public SmallVesselContainer(int windowId, PlayerInventory playerInv)
    {
        super(ModContainers.SMALL_VESSEL.get(), playerInv, playerInv.player.getHeldItemMainhand().getItem() == ModItems.CERAMIC_SMALL_VESSEL.get() ? playerInv.player.getHeldItemMainhand() : playerInv.player.getHeldItemOffhand(), windowId);
    }

    @Override
    protected void addContainerSlots()
    {
        CoreHelpers.ifPresentOrElse(stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).filter(handler -> handler instanceof ISlotCallback), handler -> {
            for (int x = 0; x < 3; x++)
            {
                for (int y = 0; y < 3; y++)
                {
                    addSlot(new SlotCallback((ISlotCallback) handler, handler, x + 5 * y, 62 + x * 18, 20 + y * 18));
                }
            }
        }, () -> LOGGER.warn("Missing item handler or incorrect subclass?"));
    }
}
