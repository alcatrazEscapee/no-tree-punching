package com.alcatrazescapee.notreepunching.common.container;

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
    protected final Player player;
    protected final InteractionHand hand;
    protected final int hotbarIndex;

    protected int itemIndex;

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
        if (slot == itemIndex || (dragType == hotbarIndex && clickType == ClickType.SWAP))
        {
            return;
        }
        super.clicked(slot, dragType, clickType, player);
    }
}