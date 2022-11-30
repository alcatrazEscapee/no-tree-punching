package com.alcatrazescapee.notreepunching.common.items;

import javax.annotation.Nonnull;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.Nullable;

public class ForgeBucketItem extends CeramicBucketItem
{
    public ForgeBucketItem(Fluid fluid, Properties properties)
    {
        super(fluid, properties);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt)
    {
        return new Instance(stack);
    }

    /**
     * Copied from {@link net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper} to maintain parity as much as possible with default bucket behavior
     */
    static class Instance implements IFluidHandlerItem, ICapabilityProvider
    {
        private final LazyOptional<IFluidHandlerItem> capability;
        private ItemStack container;

        Instance(ItemStack container)
        {
            this.capability = LazyOptional.of(() -> this);
            this.container = container;
        }

        @Nonnull
        @Override
        public ItemStack getContainer()
        {
            return container;
        }

        @Override
        public int getTanks()
        {
            return 1;
        }

        @Nonnull
        @Override
        public FluidStack getFluidInTank(int tank)
        {
            return getFluid();
        }

        @Override
        public int getTankCapacity(int tank)
        {
            return FluidType.BUCKET_VOLUME;
        }

        @Override
        public boolean isFluidValid(int tank, @Nonnull FluidStack stack)
        {
            return stack.getFluid() == Fluids.WATER;
        }

        @Override
        public int fill(FluidStack resource, FluidAction action)
        {
            if (container.getCount() != 1 || resource.getAmount() < FluidType.BUCKET_VOLUME || container.getItem() instanceof MilkBucketItem || !getFluid().isEmpty() || resource.getFluid() != Fluids.WATER)
            {
                return 0;
            }

            if (action.execute())
            {
                setFluid(resource.getFluid());
            }

            return FluidType.BUCKET_VOLUME;
        }

        @Nonnull
        @Override
        public FluidStack drain(FluidStack resource, FluidAction action)
        {
            if (container.getCount() != 1 || resource.getAmount() < FluidType.BUCKET_VOLUME)
            {
                return FluidStack.EMPTY;
            }

            final FluidStack fluidStack = getFluid();
            if (!fluidStack.isEmpty() && fluidStack.isFluidEqual(resource))
            {
                if (action.execute())
                {
                    setFluid(Fluids.EMPTY);
                }
                return fluidStack;
            }

            return FluidStack.EMPTY;
        }

        @Nonnull
        @Override
        public FluidStack drain(int maxDrain, FluidAction action)
        {
            if (container.getCount() != 1 || maxDrain < FluidType.BUCKET_VOLUME)
            {
                return FluidStack.EMPTY;
            }

            final FluidStack fluidStack = getFluid();
            if (!fluidStack.isEmpty())
            {
                if (action.execute())
                {
                    setFluid(Fluids.EMPTY);
                }
                return fluidStack;
            }

            return FluidStack.EMPTY;
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
        {
            return CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY.orEmpty(capability, this.capability);
        }

        private FluidStack getFluid()
        {
            return container.getItem() == ModItems.CERAMIC_BUCKET.get() ? FluidStack.EMPTY : new FluidStack(Fluids.WATER, FluidType.BUCKET_VOLUME);
        }

        private void setFluid(Fluid fluid)
        {
            container = CeramicBucketItem.convert(fluid.getBucket());
        }
    }
}
