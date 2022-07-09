package com.alcatrazescapee.notreepunching.common.container;

import java.util.EnumSet;
import java.util.Set;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

/**
 * Container for inventories from item stacks
 */
public class ItemStackContainer extends ModContainer
{
    private static final Set<ClickType> ILLEGAL_ITEM_CLICKS = EnumSet.of(ClickType.QUICK_MOVE, ClickType.PICKUP, ClickType.THROW, ClickType.SWAP);

    protected final Player player;
    protected final InteractionHand hand;

    protected final int hotbarIndex; // Index in the hotbar. Between [0, 9)
    protected int itemIndex; // Index into the slot for the hotbar slot. Hotbar is at the end of the inventory.

    protected ItemStackContainer(MenuType<?> containerType, int windowId, Inventory playerInventory, InteractionHand hand)
    {
        super(containerType, windowId);

        this.player = playerInventory.player;
        this.hand = hand;

        this.hotbarIndex = playerInventory.selected;
    }

    @Override
    protected void init(Inventory playerInventory)
    {
        super.init(playerInventory);

        // Must run after slots are initialized
        if (hand == InteractionHand.MAIN_HAND)
        {
            this.itemIndex = containerSlots + playerInventory.selected + 27; // Main hand opened inventory
        }
        else
        {
            this.itemIndex = -100; // Offhand, so ignore this rule
        }
    }

    /**
     * Prevent any movement of the item stack from which this container was opened.
     */
    @Override
    public void clicked(int slot, int dragType, ClickType clickType, Player player)
    {
        // We can't move if:
        // the slot is the item index, and it's an illegal action (like, swapping the items)
        // the hotbar item is being swapped out
        // the action is "pickup all" (this ignores every slot, so we cannot allow it)
        if ((slot == itemIndex && ILLEGAL_ITEM_CLICKS.contains(clickType)) ||
            (dragType == hotbarIndex && clickType == ClickType.SWAP) ||
            (dragType == 40 && clickType == ClickType.SWAP && hand == InteractionHand.OFF_HAND) ||
            clickType == ClickType.PICKUP_ALL)
        {
            return;
        }
        super.clicked(slot, dragType, clickType, player);
    }
}