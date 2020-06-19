/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Copyright (c) 2019. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.client.screen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import com.alcatrazescapee.notreepunching.common.container.DeviceContainer;
import com.alcatrazescapee.notreepunching.common.tileentity.InventoryTileEntity;


/**
 * Screen for a {@link InventoryTileEntity} and {@link DeviceContainer}
 */
public class DeviceScreen<T extends InventoryTileEntity, C extends DeviceContainer<T>> extends ModContainerScreen<C>
{
    protected final T tile;

    public DeviceScreen(C container, PlayerInventory playerInventory, ITextComponent name, ResourceLocation texture)
    {
        super(container, playerInventory, name, texture);
        this.tile = container.getTileEntity();
    }
}
