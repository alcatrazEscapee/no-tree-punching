package com.alcatrazescapee.notreepunching.common.items;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class CeramicBucketItem extends BucketItem
{
    public static ItemStack convert(Item bucket)
    {
        return new ItemStack(bucket == Items.BUCKET ? ModItems.CERAMIC_BUCKET.get() : ModItems.CERAMIC_WATER_BUCKET.get());
    }

    private final Fluid content;

    public CeramicBucketItem(Fluid fluid, Properties properties)
    {
        super(fluid, properties);
        this.content = fluid;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        final ItemStack stack = player.getItemInHand(hand);
        final BlockHitResult hit = getPlayerPOVHitResult(level, player, this.content == Fluids.EMPTY ? ClipContext.Fluid.SOURCE_ONLY : ClipContext.Fluid.NONE);
        if (hit.getType() == HitResult.Type.MISS)
        {
            return InteractionResultHolder.pass(stack);
        }
        else if (hit.getType() != HitResult.Type.BLOCK)
        {
            return InteractionResultHolder.pass(stack);
        }
        else
        {
            final BlockPos pos = hit.getBlockPos();
            final Direction direction = hit.getDirection();
            final BlockPos offsetPos = pos.relative(direction);
            if (!level.mayInteract(player, pos) || !player.mayUseItemAt(offsetPos, direction, stack))
            {
                return InteractionResultHolder.fail(stack);
            }
            else if (this.content == Fluids.EMPTY)
            {
                final BlockState state = level.getBlockState(pos);
                if (state.getBlock() instanceof BucketPickup pickup)
                {
                    final ItemStack filled = convert(pickup.pickupBlock(level, pos, state).getItem());
                    if (!filled.isEmpty())
                    {
                        player.awardStat(Stats.ITEM_USED.get(this));
                        pickup.getPickupSound().ifPresent(soundEvent -> player.playSound(soundEvent, 1.0F, 1.0F));
                        level.gameEvent(player, GameEvent.FLUID_PICKUP, pos);

                        final ItemStack filledResult = ItemUtils.createFilledResult(stack, player, filled);
                        if (player instanceof ServerPlayer serverPlayer)
                        {
                            CriteriaTriggers.FILLED_BUCKET.trigger(serverPlayer, filled);
                        }
                        return InteractionResultHolder.sidedSuccess(filledResult, level.isClientSide());
                    }
                }
                return InteractionResultHolder.fail(stack);
            }
            else
            {
                final BlockState state = level.getBlockState(pos);
                final BlockPos targetPos = state.getBlock() instanceof LiquidBlockContainer && this.content == Fluids.WATER ? pos : offsetPos;
                if (emptyContents(player, level, targetPos, hit))
                {
                    checkExtraContent(player, level, stack, targetPos);
                    if (player instanceof ServerPlayer serverPlayer)
                    {
                        CriteriaTriggers.PLACED_BLOCK.trigger(serverPlayer, targetPos, stack);
                    }

                    player.awardStat(Stats.ITEM_USED.get(this));
                    return InteractionResultHolder.sidedSuccess(convert(getEmptySuccessItem(stack, player).getItem()), level.isClientSide());
                }
                else
                {
                    return InteractionResultHolder.fail(stack);
                }
            }
        }
    }
}