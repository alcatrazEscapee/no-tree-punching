/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.container;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import com.alcatrazescapee.notreepunching.common.blockentity.InventoryBlockEntity;

/**
 * Generic container for use with {@link InventoryBlockEntity}
 */
public abstract class DeviceContainer<T extends InventoryBlockEntity> extends ModContainer
{
    protected final T entity;

    protected DeviceContainer(MenuType<?> containerType, T entity, Inventory playerInventory, int windowId)
    {
        super(containerType, windowId);

        this.entity = entity;

        addContainerSlots();
        addPlayerInventorySlots(playerInventory);
    }

    @Override
    public boolean stillValid(Player playerIn)
    {
        return entity.canInteractWith(playerIn);
    }

    public T getBlockEntity()
    {
        return entity;
    }

    @FunctionalInterface
    public interface IFactory<T extends InventoryBlockEntity, C extends DeviceContainer<T>>
    {
        C create(T tile, Inventory playerInventory, int windowId);
    }
}