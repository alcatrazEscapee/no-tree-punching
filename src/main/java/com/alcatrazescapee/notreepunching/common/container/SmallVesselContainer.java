/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.container;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;

import com.alcatrazescapee.notreepunching.common.items.ModItems;
import com.alcatrazescapee.notreepunching.common.items.SmallVesselItem;
import com.alcatrazescapee.notreepunching.util.Helpers;
import com.alcatrazescapee.notreepunching.util.ISlotCallback;
import com.alcatrazescapee.notreepunching.util.SlotCallback;

public class SmallVesselContainer extends ItemStackContainer
{
    private static final Logger LOGGER = LogManager.getLogger();

    public SmallVesselContainer(int windowId, PlayerInventory playerInv)
    {
        this(windowId, playerInv, playerInv.player.getMainHandItem().getItem() == ModItems.CERAMIC_SMALL_VESSEL.get() ? playerInv.player.getMainHandItem() : playerInv.player.getOffhandItem());
    }

    public SmallVesselContainer(int windowId, PlayerInventory playerInv, ItemStack stack)
    {
        super(ModContainers.SMALL_VESSEL.get(), playerInv, stack, windowId);
    }

    @Override
    protected void addContainerSlots()
    {
        Helpers.ifPresentOrElse(stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).filter(handler -> handler instanceof ISlotCallback), handler -> {
            for (int x = 0; x < SmallVesselItem.SLOT_COLUMNS; x++)
            {
                for (int y = 0; y < SmallVesselItem.SLOT_ROWS; y++)
                {
                    addSlot(new SlotCallback((ISlotCallback) handler, handler, x + SmallVesselItem.SLOT_COLUMNS * y, 62 + x * 18, 20 + y * 18));
                }
            }
        }, () -> LOGGER.warn("Missing item handler or incorrect subclass?"));
    }
}