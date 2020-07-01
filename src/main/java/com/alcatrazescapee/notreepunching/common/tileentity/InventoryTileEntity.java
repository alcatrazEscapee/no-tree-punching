/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.tileentity;

import javax.annotation.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import com.alcatrazescapee.notreepunching.util.Helpers;
import com.alcatrazescapee.notreepunching.util.ISlotCallback;
import com.alcatrazescapee.notreepunching.util.ItemStackHandlerCallback;

/**
 * An extension of {@link InventoryTileEntity} which has a custom name
 *
 * @since 2.0.0
 */
public abstract class InventoryTileEntity extends ModTileEntity implements INamedContainerProvider, ISlotCallback
{
    protected final ItemStackHandler inventory;
    protected final LazyOptional<IItemHandler> inventoryCapability;
    protected ITextComponent customName, defaultName;

    public InventoryTileEntity(TileEntityType<?> type, int inventorySlots, ITextComponent defaultName)
    {
        super(type);

        this.inventory = new ItemStackHandlerCallback(this, inventorySlots);
        this.inventoryCapability = LazyOptional.of(() -> InventoryTileEntity.this.inventory);
        this.defaultName = defaultName;
    }

    public InventoryTileEntity(TileEntityType<?> type, ItemStackHandler inventory, ITextComponent defaultName)
    {
        super(type);

        this.inventory = inventory;
        this.inventoryCapability = LazyOptional.of(() -> inventory);
        this.defaultName = defaultName;
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return customName != null ? customName : defaultName;
    }

    @Nullable
    public ITextComponent getCustomName()
    {
        return customName;
    }

    public void setCustomName(ITextComponent customName)
    {
        this.customName = customName;
    }

    @Override
    public void read(CompoundNBT nbt)
    {
        if (nbt.contains("CustomName"))
        {
            customName = ITextComponent.Serializer.fromJson(nbt.getString("CustomName"));
        }
        inventory.deserializeNBT(nbt.getCompound("inventory"));
        super.read(nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        if (customName != null)
        {
            nbt.putString("CustomName", ITextComponent.Serializer.toJson(customName));
        }
        nbt.put("inventory", inventory.serializeNBT());
        return super.write(nbt);
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

    public boolean canInteractWith(PlayerEntity player)
    {
        return true;
    }

    public void onReplaced()
    {
        if (world != null && !world.isRemote)
        {
            Helpers.dropInventoryItems(world, pos, inventory);
        }
    }
}
