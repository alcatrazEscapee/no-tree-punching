package com.alcatrazescapee.notreepunching.common.items;

import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
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

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        return useBucket(level, player, hand);
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

    /**
     * This is all copy pasta from {@link BucketItem}, but changed to use {@link #getFluid(ItemStack)} rather than the {@code this.content}.
     */
    private InteractionResultHolder<ItemStack> useBucket(Level level, Player player, InteractionHand hand)
    {
        ItemStack itemstack = player.getItemInHand(hand);
        Fluid content = getFluid(itemstack);
        BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, content == Fluids.EMPTY ? ClipContext.Fluid.SOURCE_ONLY : ClipContext.Fluid.NONE);
        InteractionResultHolder<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(player, level, itemstack, blockhitresult);
        if (ret != null) return ret;
        if (blockhitresult.getType() == HitResult.Type.MISS)
        {
            return InteractionResultHolder.pass(itemstack);
        }
        else if (blockhitresult.getType() != HitResult.Type.BLOCK)
        {
            return InteractionResultHolder.pass(itemstack);
        }
        else
        {
            BlockPos blockpos = blockhitresult.getBlockPos();
            Direction direction = blockhitresult.getDirection();
            BlockPos blockpos1 = blockpos.relative(direction);
            if (level.mayInteract(player, blockpos) && player.mayUseItemAt(blockpos1, direction, itemstack))
            {
                if (content == Fluids.EMPTY)
                {
                    BlockState blockstate1 = level.getBlockState(blockpos);
                    if (blockstate1.getBlock() instanceof BucketPickup)
                    {
                        BucketPickup bucketpickup = (BucketPickup) blockstate1.getBlock();
                        ItemStack itemstack1 = bucketpickup.pickupBlock(level, blockpos, blockstate1);
                        if (!itemstack1.isEmpty())
                        {
                            player.awardStat(Stats.ITEM_USED.get(this));
                            bucketpickup.getPickupSound().ifPresent((soundEvent_) -> {
                                player.playSound(soundEvent_, 1.0F, 1.0F);
                            });
                            level.gameEvent(player, GameEvent.FLUID_PICKUP, blockpos);
                            ItemStack itemstack2 = ItemUtils.createFilledResult(itemstack, player, itemstack1);
                            if (!level.isClientSide)
                            {
                                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player, itemstack1);
                            }

                            return InteractionResultHolder.sidedSuccess(itemstack2, level.isClientSide());
                        }
                    }

                    return InteractionResultHolder.fail(itemstack);
                }
                else
                {
                    BlockState blockstate = level.getBlockState(blockpos);
                    BlockPos blockpos2 = canBlockContainFluid(level, blockpos, blockstate, content) ? blockpos : blockpos1;
                    if (this.emptyContents(player, level, blockpos2, blockhitresult, content))
                    {
                        if (player instanceof ServerPlayer)
                        {
                            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, blockpos2, itemstack);
                        }

                        player.awardStat(Stats.ITEM_USED.get(this));
                        return InteractionResultHolder.sidedSuccess(getEmptySuccessItem(itemstack, player), level.isClientSide());
                    }
                    else
                    {
                        return InteractionResultHolder.fail(itemstack);
                    }
                }
            }
            else
            {
                return InteractionResultHolder.fail(itemstack);
            }
        }
    }

    @SuppressWarnings("ALL")
    private ItemStack getEmptySuccessItem(ItemStack bucketStack, Player player)
    {
        return !player.getAbilities().instabuild ? new ItemStack(Items.BUCKET) : bucketStack;
    }

    @SuppressWarnings("ALL")
    private boolean emptyContents(@Nullable Player player, Level level, BlockPos pos, @Nullable BlockHitResult result, Fluid content)
    {
        if (!(content instanceof FlowingFluid))
        {
            return false;
        }
        else
        {
            BlockState blockstate = level.getBlockState(pos);
            Block block = blockstate.getBlock();
            Material material = blockstate.getMaterial();
            boolean flag = blockstate.canBeReplaced(content);
            boolean flag1 = blockstate.isAir() || flag || block instanceof LiquidBlockContainer && ((LiquidBlockContainer) block).canPlaceLiquid(level, pos, blockstate, content);
            if (!flag1)
            {
                return result != null && this.emptyContents(player, level, result.getBlockPos().relative(result.getDirection()), (BlockHitResult) null, content);
            }
            else if (level.dimensionType().ultraWarm() && content.is(FluidTags.WATER))
            {
                int i = pos.getX();
                int j = pos.getY();
                int k = pos.getZ();
                level.playSound(player, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (level.random.nextFloat() - level.random.nextFloat()) * 0.8F);

                for (int l = 0; l < 8; ++l)
                {
                    level.addParticle(ParticleTypes.LARGE_SMOKE, (double) i + Math.random(), (double) j + Math.random(), (double) k + Math.random(), 0.0D, 0.0D, 0.0D);
                }

                return true;
            }
            else if (block instanceof LiquidBlockContainer && ((LiquidBlockContainer) block).canPlaceLiquid(level, pos, blockstate, content))
            {
                ((LiquidBlockContainer) block).placeLiquid(level, pos, blockstate, ((FlowingFluid) content).getSource(false));
                this.playEmptySound(player, level, pos, content);
                return true;
            }
            else
            {
                if (!level.isClientSide && flag && !material.isLiquid())
                {
                    level.destroyBlock(pos, true);
                }

                if (!level.setBlock(pos, content.defaultFluidState().createLegacyBlock(), 11) && !blockstate.getFluidState().isSource())
                {
                    return false;
                }
                else
                {
                    this.playEmptySound(player, level, pos, content);
                    return true;
                }
            }
        }
    }

    @SuppressWarnings("ALL")
    private void playEmptySound(@Nullable Player player, LevelAccessor level, BlockPos pos, Fluid content)
    {
        SoundEvent soundevent = content.getAttributes().getEmptySound();
        if (soundevent == null) soundevent = content.is(FluidTags.LAVA) ? SoundEvents.BUCKET_EMPTY_LAVA : SoundEvents.BUCKET_EMPTY;
        level.playSound(player, pos, soundevent, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.gameEvent(player, GameEvent.FLUID_PLACE, pos);
    }

    @SuppressWarnings("ALL")
    private boolean canBlockContainFluid(Level worldIn, BlockPos posIn, BlockState blockstate, Fluid content)
    {
        return blockstate.getBlock() instanceof LiquidBlockContainer && ((LiquidBlockContainer) blockstate.getBlock()).canPlaceLiquid(worldIn, posIn, blockstate, content);
    }

    /**
     * Gets the contained fluid in the item stack
     */
    private Fluid getFluid(ItemStack stack)
    {
        return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).map(handler -> handler.getFluidInTank(0).getFluid()).orElse(Fluids.EMPTY);
    }
}