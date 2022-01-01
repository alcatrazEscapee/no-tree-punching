/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.tileentity;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.MenuProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import com.alcatrazescapee.notreepunching.util.ISlotCallback;
import com.alcatrazescapee.notreepunching.util.ItemStackHandlerCallback;

/**
 * An extension of {@link ModTileEntity} which has a custom name
 */
public abstract class InventoryTileEntity extends ModTileEntity implements MenuProvider, ISlotCallback
{
    protected final ItemStackHandler inventory;
    protected final LazyOptional<IItemHandler> inventoryCapability;
    protected Component customName, defaultName;

    public InventoryTileEntity(BlockEntityType<?> type, int inventorySlots, Component defaultName)
    {
        super(type);

        this.inventory = new ItemStackHandlerCallback(this, inventorySlots);
        this.inventoryCapability = LazyOptional.of(() -> InventoryTileEntity.this.inventory);
        this.defaultName = defaultName;
    }

    @Override
    public Component getDisplayName()
    {
        return customName != null ? customName : defaultName;
    }

    @Nullable
    public Component getCustomName()
    {
        return customName;
    }

    public void setCustomName(Component customName)
    {
        this.customName = customName;
    }

    @Override
    public void load(BlockState state, CompoundTag nbt)
    {
        if (nbt.contains("CustomName"))
        {
            customName = Component.Serializer.fromJson(nbt.getString("CustomName"));
        }
        inventory.deserializeNBT(nbt.getCompound("inventory"));
        super.load(state, nbt);
    }

    @Override
    public CompoundTag save(CompoundTag nbt)
    {
        if (customName != null)
        {
            nbt.putString("CustomName", Component.Serializer.toJson(customName));
        }
        nbt.put("inventory", inventory.serializeNBT());
        return super.save(nbt);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side)
    {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && side == null)
        {
            return inventoryCapability.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void setAndUpdateSlots(int slot)
    {
        markDirtyFast();
    }

    public boolean canInteractWith(Player player)
    {
        return true;
    }

    public void onRemove()
    {
        inventoryCapability.invalidate();
    }
}