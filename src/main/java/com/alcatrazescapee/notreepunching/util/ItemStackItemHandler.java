package com.alcatrazescapee.notreepunching.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Like {@link net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple}, this saves everything to the stack NBT.
 */
public class ItemStackItemHandler implements ICapabilitySerializable<CompoundTag>, ISlotCallback
{
    protected final LazyOptional<IItemHandler> capability;
    protected final ItemStackHandler inventory;
    protected final ItemStack stack;

    public ItemStackItemHandler(@Nullable CompoundTag capNbt, ItemStack stack, int slots)
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
        stack.addTagElement("inventory", inventory.serializeNBT());
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side)
    {
        return Helpers.getCapabilityWithNullChecks(cap, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, capability);
    }

    @Override
    public CompoundTag serializeNBT()
    {
        return inventory.serializeNBT();
    }

    @Override
    public void deserializeNBT(@Nullable CompoundTag nbt)
    {
        if (nbt != null)
        {
            inventory.deserializeNBT(nbt);
            stack.addTagElement("inventory", nbt);
        }
    }
}