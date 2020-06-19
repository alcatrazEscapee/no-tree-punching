/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Copyright (c) 2019. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.util;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Like {@link net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple}, this saves everything to the stack NBT.
 */
public class ItemStackItemHandler implements ICapabilitySerializable<CompoundNBT>, ISlotCallback
{
    protected final LazyOptional<IItemHandler> capability;
    protected final ItemStackHandler inventory;
    protected final ItemStack stack;

    public ItemStackItemHandler(@Nullable CompoundNBT capNbt, ItemStack stack, int slots)
    {
        this.stack = stack;
        this.inventory = new ItemStackHandlerCallback(this, slots);
        this.capability = LazyOptional.of(() -> inventory);

        deserializeNBT(capNbt);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack)
    {
        return true;
    }

    @Override
    public void setAndUpdateSlots(int slot)
    {
        // Update the item stack tag on any change
        stack.setTagInfo("inventory", inventory.serializeNBT());
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side)
    {
        return Helpers.getCapabilityOrElse(cap, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, capability);
    }

    @Override
    public CompoundNBT serializeNBT()
    {
        return inventory.serializeNBT();
    }

    @Override
    public void deserializeNBT(@Nullable CompoundNBT nbt)
    {
        if (nbt != null)
        {
            inventory.deserializeNBT(nbt);
            stack.setTagInfo("inventory", nbt);
        }
    }
}
