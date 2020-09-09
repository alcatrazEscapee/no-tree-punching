/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.items;

import javax.annotation.Nullable;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;
import net.minecraftforge.registries.ForgeRegistries;

public class CeramicBucketItem extends Item
{
    public CeramicBucketItem(Properties properties)
    {
        super(properties);
    }

    /**
     * Copy pasta from {@link net.minecraft.item.BucketItem} with modifications to use the capability fluid
     */
    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        ItemStack bucketStack = playerIn.getItemInHand(handIn);
        Fluid containedFluid = getFluid(bucketStack);
        BlockRayTraceResult raytraceresult = getPlayerPOVHitResult(worldIn, playerIn, containedFluid == Fluids.EMPTY ? RayTraceContext.FluidMode.SOURCE_ONLY : RayTraceContext.FluidMode.NONE);
        ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(playerIn, worldIn, bucketStack, raytraceresult);
        if (ret != null)
        {
            return ret;
        }
        if (raytraceresult.getType() == RayTraceResult.Type.MISS)
        {
            return ActionResult.pass(bucketStack);
        }
        else if (raytraceresult.getType() != RayTraceResult.Type.BLOCK)
        {
            return ActionResult.pass(bucketStack);
        }
        else
        {
            BlockPos fluidPos = raytraceresult.getBlockPos();
            Direction direction = raytraceresult.getDirection();
            BlockPos pos1 = fluidPos.relative(direction);
            if (worldIn.mayInteract(playerIn, fluidPos) && playerIn.mayUseItemAt(pos1, direction, bucketStack))
            {
                if (containedFluid == Fluids.EMPTY)
                {
                    BlockState blockstate1 = worldIn.getBlockState(fluidPos);
                    if (blockstate1.getBlock() instanceof IBucketPickupHandler)
                    {
                        Fluid fluid = ((IBucketPickupHandler) blockstate1.getBlock()).takeLiquid(worldIn, fluidPos, blockstate1);
                        if (fluid != Fluids.EMPTY)
                        {
                            playerIn.awardStat(Stats.ITEM_USED.get(this));

                            SoundEvent soundevent = containedFluid.getAttributes().getEmptySound();
                            if (soundevent == null)
                            {
                                soundevent = fluid.is(FluidTags.LAVA) ? SoundEvents.BUCKET_FILL_LAVA : SoundEvents.BUCKET_FILL;
                            }
                            playerIn.playSound(soundevent, 1.0F, 1.0F);
                            ItemStack filledBucket = this.fillBucket(bucketStack, playerIn, fluid);
                            if (!worldIn.isClientSide)
                            {
                                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayerEntity) playerIn, new ItemStack(fluid.getBucket()));
                            }

                            return ActionResult.sidedSuccess(filledBucket, worldIn.isClientSide());
                        }
                    }

                    return ActionResult.fail(bucketStack);
                }
                else
                {
                    BlockState blockstate = worldIn.getBlockState(fluidPos);
                    BlockPos pos2 = canBlockContainFluid(worldIn, fluidPos, blockstate, containedFluid) ? fluidPos : pos1;
                    if (this.tryPlaceContainedLiquid(playerIn, worldIn, pos2, raytraceresult, containedFluid))
                    {
                        if (playerIn instanceof ServerPlayerEntity)
                        {
                            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) playerIn, pos2, bucketStack);
                        }

                        playerIn.awardStat(Stats.ITEM_USED.get(this));
                        return ActionResult.sidedSuccess(this.emptyBucket(bucketStack, playerIn), worldIn.isClientSide());
                    }
                    else
                    {
                        return ActionResult.fail(bucketStack);
                    }
                }
            }
            else
            {
                return ActionResult.fail(bucketStack);
            }
        }
    }

    @Override
    public ITextComponent getName(ItemStack stack)
    {
        if (CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY != null)
        {
            return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).map(handler -> {
                if (handler instanceof FluidHandlerItemStackSimple && !((FluidHandlerItemStackSimple) handler).getFluid().isEmpty())
                {
                    IFormattableTextComponent fluidName = ((FluidHandlerItemStackSimple) handler).getFluid().getDisplayName().plainCopy();
                    fluidName.append(" ").append(super.getName(stack));
                    return fluidName;
                }
                return super.getName(stack);
            }).orElseThrow(() -> new IllegalStateException("No fluid handler on ceramic bucket?"));
        }
        return super.getName(stack);
    }

    @Override
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items)
    {
        if (allowdedIn(group))
        {
            items.add(new ItemStack(this));
            if (CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY != null)
            {
                for (Fluid fluid : ForgeRegistries.FLUIDS.getValues())
                {
                    ItemStack stack = new ItemStack(this);
                    stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(handler -> {
                        FluidStack fluidStack = new FluidStack(fluid, 1000);
                        if (handler.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE) > 0)
                        {
                            items.add(stack);
                        }
                    });
                }
            }
        }
    }

    public boolean tryPlaceContainedLiquid(@Nullable PlayerEntity player, World worldIn, BlockPos posIn, @Nullable BlockRayTraceResult rayTrace, Fluid containedFluid)
    {
        if (!(containedFluid instanceof FlowingFluid))
        {
            return false;
        }
        else
        {
            BlockState blockstate = worldIn.getBlockState(posIn);
            Block block = blockstate.getBlock();
            Material material = blockstate.getMaterial();
            if (!(blockstate.isAir(worldIn, posIn) || blockstate.canBeReplaced(containedFluid) || block instanceof ILiquidContainer && ((ILiquidContainer) block).canPlaceLiquid(worldIn, posIn, blockstate, containedFluid)))
            {
                return rayTrace != null && tryPlaceContainedLiquid(player, worldIn, rayTrace.getBlockPos().relative(rayTrace.getDirection()), null, containedFluid);
            }
            else if (worldIn.dimensionType().ultraWarm() && containedFluid.is(FluidTags.WATER))
            {
                worldIn.playSound(player, posIn, SoundEvents.FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (worldIn.random.nextFloat() - worldIn.random.nextFloat()) * 0.8F);
                for (int l = 0; l < 8; ++l)
                {
                    worldIn.addParticle(ParticleTypes.LARGE_SMOKE, (double) posIn.getX() + Math.random(), (double) posIn.getY() + Math.random(), (double) posIn.getZ() + Math.random(), 0.0D, 0.0D, 0.0D);
                }
                return true;
            }
            else if (block instanceof ILiquidContainer && ((ILiquidContainer) block).canPlaceLiquid(worldIn, posIn, blockstate, containedFluid))
            {
                ((ILiquidContainer) block).placeLiquid(worldIn, posIn, blockstate, ((FlowingFluid) containedFluid).getSource(false));
                playEmptySound(player, worldIn, posIn, containedFluid);
                return true;
            }
            else
            {
                if (!worldIn.isClientSide && blockstate.canBeReplaced(containedFluid) && !material.isLiquid())
                {
                    worldIn.destroyBlock(posIn, true);
                }

                if (worldIn.setBlock(posIn, containedFluid.defaultFluidState().createLegacyBlock(), 11) && !blockstate.getFluidState().isSource())
                {
                    return false;
                }
                else
                {
                    playEmptySound(player, worldIn, posIn, containedFluid);
                    return true;
                }
            }
        }
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack)
    {
        return new ItemStack(this);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt)
    {
        return new FluidHandlerItemStackSimple(stack, 1000);
    }

    protected void playEmptySound(@Nullable PlayerEntity player, IWorld worldIn, BlockPos pos, Fluid containedFluid)
    {
        SoundEvent emptySound = containedFluid.getAttributes().getEmptySound();
        if (emptySound == null)
        {
            emptySound = containedFluid.is(FluidTags.LAVA) ? SoundEvents.BUCKET_EMPTY_LAVA : SoundEvents.BUCKET_EMPTY;
        }
        worldIn.playSound(player, pos, emptySound, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    private ItemStack emptyBucket(ItemStack stack, PlayerEntity playerIn)
    {
        return playerIn.abilities.instabuild ? stack : getContainerItem(stack);
    }

    private ItemStack fillBucket(ItemStack emptyBuckets, PlayerEntity player, Fluid fillFluid)
    {
        if (player.abilities.instabuild)
        {
            return emptyBuckets;
        }
        else
        {
            ItemStack filledStack = emptyBuckets.split(1);
            filledStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(handler -> handler.fill(new FluidStack(fillFluid, 1000), IFluidHandler.FluidAction.EXECUTE));
            if (emptyBuckets.isEmpty())
            {
                return filledStack;
            }
            else
            {
                if (!player.inventory.add(filledStack))
                {
                    player.drop(filledStack, false);
                }
                return emptyBuckets;
            }
        }
    }

    private boolean canBlockContainFluid(World worldIn, BlockPos posIn, BlockState blockstate, Fluid fluid)
    {
        return blockstate.getBlock() instanceof ILiquidContainer && ((ILiquidContainer) blockstate.getBlock()).canPlaceLiquid(worldIn, posIn, blockstate, fluid);
    }

    /**
     * Gets the contained fluid in the item stack
     */
    private Fluid getFluid(ItemStack stack)
    {
        return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).map(handler -> handler.getFluidInTank(0).getFluid()).orElse(Fluids.EMPTY);
    }
}