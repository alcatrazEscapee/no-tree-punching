package com.alcatrazescapee.notreepunching.util.inventory;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.alcatrazescapee.notreepunching.common.blockentity.LargeVesselBlockEntity;
import com.alcatrazescapee.notreepunching.common.blockentity.ModBlockEntities;
import com.alcatrazescapee.notreepunching.common.items.ModItems;
import com.alcatrazescapee.notreepunching.util.Helpers;

/**
 * Implements Forge {@link net.minecraftforge.items.IItemHandler} capabilities that wrap our otherwise cross-platform inventory handlers.
 * Rather than requiring us to implement things on the class directly, we just attach via events to our own objects.
 * No, I won't properly manage invalidation as I think it's a stupid concept not worth anyone's time and effort implementing support for it.
 */
public final class ForgeInventoryCapabilities
{
    private static final ResourceLocation CERAMIC_SMALL_VESSEL_INVENTORY = Helpers.identifier("ceramic_small_vessel_inventory");
    private static final ResourceLocation CERAMIC_LARGE_VESSEL_INVENTORY = Helpers.identifier("ceramic_large_vessel_inventory");

    public static void attachItemStackCapabilities(AttachCapabilitiesEvent<ItemStack> event)
    {
        if (event.getObject().getItem() == ModItems.CERAMIC_SMALL_VESSEL.get())
        {
            // todo: item handler capability
        }
    }

    public static void attachBlockEntityCapabilities(AttachCapabilitiesEvent<BlockEntity> event)
    {
        if (event.getObject().getType() == ModBlockEntities.LARGE_VESSEL.get())
        {
            event.addCapability(CERAMIC_LARGE_VESSEL_INVENTORY, new LargeVesselCapability((LargeVesselBlockEntity) event.getObject()));
        }
    }

    static class LargeVesselCapability implements ICapabilityProvider, IItemHandlerModifiable
    {
        private final LazyOptional<LargeVesselCapability> capability;
        private final LargeVesselBlockEntity vessel;

        LargeVesselCapability(LargeVesselBlockEntity vessel)
        {
            this.capability = LazyOptional.of(() -> this);
            this.vessel = vessel;
        }

        @Override
        public void setStackInSlot(int slot, ItemStack stack)
        {
            vessel.set(slot, stack);
        }

        @Override
        public int getSlots()
        {
            return vessel.size();
        }

        @NotNull
        @Override
        public ItemStack getStackInSlot(int slot)
        {
            return vessel.get(slot);
        }

        @NotNull
        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
        {
            final ItemStack current = vessel.get(slot);
            if (stack.isEmpty() || !vessel.canContain(stack) || !ItemHandlerHelper.canItemStacksStack(stack, current) || current.getCount() >= 64)
            {
                return stack; // Cannot insert - inserting empty, cannot contain, cannot stack, or currently full.
            }
            if (current.isEmpty())
            {
                // When the current is empty, we set the new stack directly and return zero excess.
                if (!simulate)
                {
                    vessel.set(slot, stack);
                }
                return ItemStack.EMPTY;
            }
            // Merging two stacks - count the total, if it's over the limit we need to split and return remainder
            final int total = current.getCount() + stack.getCount();
            if (total > 64)
            {
                final ItemStack remainder = stack.copy();
                remainder.setCount(total - 64);
                if (!simulate)
                {
                    current.setCount(64);
                    vessel.modified();
                }
                return remainder;
            }
            // No remainder, so just set the total and return empty
            if (!simulate)
            {
                current.setCount(total);
                vessel.modified();
            }
            return ItemStack.EMPTY;
        }

        @NotNull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate)
        {
            final ItemStack current = vessel.get(slot);
            if (current.isEmpty() || amount <= 0)
            {
                return ItemStack.EMPTY; // Nothing to extract - requested <= 0 or the stack is empty.
            }
            if (amount >= current.getCount())
            {
                // Requesting to extract more than we have - extract all, set empty, and return the current stack
                if (!simulate)
                {
                    vessel.set(slot, ItemStack.EMPTY);
                }
                return current.copy();
            }
            // Requesting to only shrink by less than we have - create a new removed stack, set the count, and shrink the current
            final ItemStack removed = current.copy();
            removed.setCount(amount);
            if (!simulate)
            {
                current.shrink(amount);
                vessel.modified();
            }
            return removed;
        }

        @Override
        public int getSlotLimit(int slot)
        {
            return 64;
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack)
        {
            return vessel.canContain(stack);
        }

        @NotNull
        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side)
        {
            return cap == ForgeCapabilities.ITEM_HANDLER ? capability.cast() : LazyOptional.empty();
        }
    }
}
