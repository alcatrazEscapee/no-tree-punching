/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.items;

import javax.annotation.Nullable;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;
import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.Item.Properties;

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
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn)
    {
        ItemStack bucketStack = playerIn.getItemInHand(handIn);
        Fluid containedFluid = getFluid(bucketStack);
        BlockHitResult rayTraceResult = getPlayerPOVHitResult(worldIn, playerIn, containedFluid == Fluids.EMPTY ? ClipContext.Fluid.SOURCE_ONLY : ClipContext.Fluid.NONE);
        InteractionResultHolder<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(playerIn, worldIn, bucketStack, rayTraceResult);
        if (ret != null)
        {
            return ret;
        }
        if (rayTraceResult.getType() == HitResult.Type.MISS)
        {
            return InteractionResultHolder.pass(bucketStack);
        }
        else if (rayTraceResult.getType() != HitResult.Type.BLOCK)
        {
            return InteractionResultHolder.pass(bucketStack);
        }
        else
        {
            BlockPos posHit = rayTraceResult.getBlockPos();
            BlockPos posOffset = posHit.relative(rayTraceResult.getDirection());
            if (worldIn.mayInteract(playerIn, posHit) && playerIn.mayUseItemAt(posOffset, rayTraceResult.getDirection(), bucketStack))
            {
                if (containedFluid == Fluids.EMPTY)
                {
                    BlockState blockstate1 = worldIn.getBlockState(posHit);
                    if (blockstate1.getBlock() instanceof BucketPickup)
                    {
                        Fluid fluid = ((BucketPickup) blockstate1.getBlock()).takeLiquid(worldIn, posHit, blockstate1);
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
                                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) playerIn, new ItemStack(fluid.getBucket()));
                            }

                            return InteractionResultHolder.sidedSuccess(filledBucket, worldIn.isClientSide());
                        }
                    }

                    return InteractionResultHolder.fail(bucketStack);
                }
                else
                {
                    BlockState hitState = worldIn.getBlockState(posHit);
                    BlockPos hitPos = canBlockContainFluid(worldIn, posHit, hitState, containedFluid) ? posHit : posOffset;
                    if (this.emptyBucket(playerIn, worldIn, hitPos, rayTraceResult, containedFluid))
                    {
                        if (playerIn instanceof ServerPlayer)
                        {
                            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) playerIn, hitPos, bucketStack);
                        }

                        playerIn.awardStat(Stats.ITEM_USED.get(this));
                        return InteractionResultHolder.sidedSuccess(this.emptyBucket(bucketStack, playerIn), worldIn.isClientSide());
                    }
                    else
                    {
                        return InteractionResultHolder.fail(bucketStack);
                    }
                }
            }
            else
            {
                return InteractionResultHolder.fail(bucketStack);
            }
        }
    }

    @Override
    public Component getName(ItemStack stack)
    {
        if (CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY != null)
        {
            return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).map(handler -> {
                if (handler instanceof FluidHandlerItemStackSimple && !((FluidHandlerItemStackSimple) handler).getFluid().isEmpty())
                {
                    MutableComponent fluidName = ((FluidHandlerItemStackSimple) handler).getFluid().getDisplayName().plainCopy();
                    fluidName.append(" ").append(super.getName(stack));
                    return fluidName;
                }
                return super.getName(stack);
            }).orElseThrow(() -> new IllegalStateException("No fluid handler on ceramic bucket?"));
        }
        return super.getName(stack);
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items)
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

    public boolean emptyBucket(@Nullable Player player, Level worldIn, BlockPos posIn, @Nullable BlockHitResult rayTrace, Fluid containedFluid)
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
            if (!(blockstate.isAir(worldIn, posIn) || blockstate.canBeReplaced(containedFluid) || block instanceof LiquidBlockContainer && ((LiquidBlockContainer) block).canPlaceLiquid(worldIn, posIn, blockstate, containedFluid)))
            {
                return rayTrace != null && emptyBucket(player, worldIn, rayTrace.getBlockPos().relative(rayTrace.getDirection()), null, containedFluid);
            }
            else if (worldIn.dimensionType().ultraWarm() && containedFluid.is(FluidTags.WATER))
            {
                worldIn.playSound(player, posIn, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (worldIn.random.nextFloat() - worldIn.random.nextFloat()) * 0.8F);
                for (int l = 0; l < 8; ++l)
                {
                    worldIn.addParticle(ParticleTypes.LARGE_SMOKE, (double) posIn.getX() + Math.random(), (double) posIn.getY() + Math.random(), (double) posIn.getZ() + Math.random(), 0.0D, 0.0D, 0.0D);
                }
                return true;
            }
            else if (block instanceof LiquidBlockContainer && ((LiquidBlockContainer) block).canPlaceLiquid(worldIn, posIn, blockstate, containedFluid))
            {
                ((LiquidBlockContainer) block).placeLiquid(worldIn, posIn, blockstate, ((FlowingFluid) containedFluid).getSource(false));
                playEmptySound(player, worldIn, posIn, containedFluid);
                return true;
            }
            else
            {
                if (!worldIn.isClientSide && blockstate.canBeReplaced(containedFluid) && !material.isLiquid())
                {
                    worldIn.destroyBlock(posIn, true);
                }

                if (!worldIn.setBlock(posIn, containedFluid.defaultFluidState().createLegacyBlock(), 11) && !blockstate.getFluidState().isSource())
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
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt)
    {
        return new FluidHandlerItemStackSimple(stack, 1000);
    }

    protected void playEmptySound(@Nullable Player player, LevelAccessor worldIn, BlockPos pos, Fluid containedFluid)
    {
        SoundEvent emptySound = containedFluid.getAttributes().getEmptySound();
        if (emptySound == null)
        {
            emptySound = containedFluid.is(FluidTags.LAVA) ? SoundEvents.BUCKET_EMPTY_LAVA : SoundEvents.BUCKET_EMPTY;
        }
        worldIn.playSound(player, pos, emptySound, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    private ItemStack emptyBucket(ItemStack stack, Player playerIn)
    {
        if (playerIn.abilities.instabuild)
        {
            return stack; // Don't empty in creative
        }
        ItemStack emptyStack = stack.copy();
        emptyStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(handler -> handler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.EXECUTE));
        return emptyStack;
    }

    private ItemStack fillBucket(ItemStack emptyBuckets, Player player, Fluid fillFluid)
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

    private boolean canBlockContainFluid(Level worldIn, BlockPos posIn, BlockState blockstate, Fluid fluid)
    {
        return blockstate.getBlock() instanceof LiquidBlockContainer && ((LiquidBlockContainer) blockstate.getBlock()).canPlaceLiquid(worldIn, posIn, blockstate, fluid);
    }

    /**
     * Gets the contained fluid in the item stack
     */
    private Fluid getFluid(ItemStack stack)
    {
        return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).map(handler -> handler.getFluidInTank(0).getFluid()).orElse(Fluids.EMPTY);
    }
}