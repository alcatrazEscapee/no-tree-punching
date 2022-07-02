package com.alcatrazescapee.notreepunching.common.container;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
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

    protected ItemStackContainer(MenuType<?> containerType, int windowId, Inventory playerInv, InteractionHand hand)
    {
        super(containerType, windowId);

        this.player = playerInv.player;
        this.hand = hand;

        this.hotbarIndex = playerInv.selected;
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
     * Note: on a server side container, the target stack is never overwritten (in general).
     * However, on a client side container, the target stack can be overwritten due to synchronization, for example, as triggered from a NBT change, which was caused by a capability instance. As a result, we cannot cache the stack in a way visible to the client side container, or screen. So we need to re-query it.
     *
     * <strong>Do not cache the result of this function!</strong> It may not be valid!
     *
     * @return the target {@link ItemStack} of this container.
     */
    public ItemStack getTargetStack()
    {
        return hand == InteractionHand.MAIN_HAND ? slots.get(itemIndex).getItem() : player.getOffhandItem();
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