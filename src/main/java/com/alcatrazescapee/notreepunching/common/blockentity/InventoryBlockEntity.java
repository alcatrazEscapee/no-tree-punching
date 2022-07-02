package com.alcatrazescapee.notreepunching.common.blockentity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import com.alcatrazescapee.notreepunching.util.ISlotCallback;
import com.alcatrazescapee.notreepunching.util.ItemStackHandlerCallback;

/**
 * An extension of {@link ModBlockEntity} which has a custom name
 */
public abstract class InventoryBlockEntity extends ModBlockEntity implements MenuProvider, ISlotCallback
{
    protected final ItemStackHandler inventory;
    protected final LazyOptional<IItemHandler> inventoryCapability;
    @Nullable protected Component customName;
    protected Component defaultName;

    public InventoryBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int inventorySlots, Component defaultName)
    {
        super(type, pos, state);

        this.inventory = new ItemStackHandlerCallback(this, inventorySlots);
        this.inventoryCapability = LazyOptional.of(() -> InventoryBlockEntity.this.inventory);
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
    public void loadAdditional(CompoundTag tag)
    {
        if (tag.contains("CustomName"))
        {
            customName = Component.Serializer.fromJson(tag.getString("CustomName"));
        }
        inventory.deserializeNBT(tag.getCompound("inventory"));
        super.load(tag);
    }

    @Override
    public void saveAdditional(CompoundTag tag)
    {
        if (customName != null)
        {
            tag.putString("CustomName", Component.Serializer.toJson(customName));
        }
        tag.put("inventory", inventory.serializeNBT());
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && side == null)
        {
            return inventoryCapability.cast();
        }
        return super.getCapability(capability, side);
    }

    @Override
    public void setAndUpdateSlots(int slot)
    {
        setChanged();
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