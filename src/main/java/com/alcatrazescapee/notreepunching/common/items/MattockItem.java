/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.items;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import com.alcatrazescapee.notreepunching.common.ModTags;
import com.alcatrazescapee.notreepunching.mixin.HoeItemAccessor;
import com.mojang.datafixers.util.Pair;

import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;

public class MattockItem extends DiggerItem
{
    public MattockItem(Tier tier, float attackDamageIn, float attackSpeedIn, Properties builder)
    {
        super(attackDamageIn, attackSpeedIn, tier, ModTags.Blocks.MINEABLE_WITH_MATTOCK, builder);
    }

    /**
     * If not sneaking, use in order of Axe -> Shovel -> Hoe
     * Otherwise, use in order of Axe -> Hoe -> Shovel
     * This is done as hoe and shovel have a possibility of conflicting (within vanilla)
     */
    @Override
    public InteractionResult useOn(UseOnContext context)
    {
        InteractionResult result = onAxeItemUse(context);
        if (result == InteractionResult.PASS)
        {
            if (context.getPlayer() != null && context.getPlayer().isShiftKeyDown())
            {
                result = onHoeItemUse(context);
                if (result == InteractionResult.PASS)
                {
                    result = onShovelItemUse(context);
                }
            }
            else
            {
                result = onShovelItemUse(context);
                if (result == InteractionResult.PASS)
                {
                    result = onHoeItemUse(context);
                }
            }
        }
        return result;
    }

    /**
     * Copy pasta from {@link net.minecraft.world.item.AxeItem}
     */
    @SuppressWarnings("ALL")
    private InteractionResult onAxeItemUse(UseOnContext context)
    {
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        Player player = context.getPlayer();
        BlockState blockstate = level.getBlockState(blockpos);
        Optional<BlockState> optional = Optional.ofNullable(blockstate.getToolModifiedState(level, blockpos, player, context.getItemInHand(), net.minecraftforge.common.ToolActions.AXE_STRIP));
        Optional<BlockState> optional1 = Optional.ofNullable(blockstate.getToolModifiedState(level, blockpos, player, context.getItemInHand(), net.minecraftforge.common.ToolActions.AXE_SCRAPE));
        Optional<BlockState> optional2 = Optional.ofNullable(blockstate.getToolModifiedState(level, blockpos, player, context.getItemInHand(), net.minecraftforge.common.ToolActions.AXE_WAX_OFF));
        ItemStack itemstack = context.getItemInHand();
        Optional<BlockState> optional3 = Optional.empty();
        if (optional.isPresent())
        {
            level.playSound(player, blockpos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
            optional3 = optional;
        }
        else if (optional1.isPresent())
        {
            level.playSound(player, blockpos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.levelEvent(player, 3005, blockpos, 0);
            optional3 = optional1;
        }
        else if (optional2.isPresent())
        {
            level.playSound(player, blockpos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.levelEvent(player, 3004, blockpos, 0);
            optional3 = optional2;
        }

        if (optional3.isPresent())
        {
            if (player instanceof ServerPlayer)
            {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) player, blockpos, itemstack);
            }

            level.setBlock(blockpos, optional3.get(), 11);
            if (player != null)
            {
                itemstack.hurtAndBreak(1, player, (player_) -> {
                    player_.broadcastBreakEvent(context.getHand());
                });
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        else
        {
            return InteractionResult.PASS;
        }
    }

    /**
     * Copy pasta from {@link net.minecraft.world.item.HoeItem}
     */
    @SuppressWarnings("ALL")
    private InteractionResult onHoeItemUse(UseOnContext context)
    {
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        Pair<Predicate<UseOnContext>, Consumer<UseOnContext>> pair = HoeItemAccessor.getTillables().get(level.getBlockState(blockpos).getBlock());
        int hook = net.minecraftforge.event.ForgeEventFactory.onHoeUse(context);
        if (hook != 0) return hook > 0 ? InteractionResult.SUCCESS : InteractionResult.FAIL;
        // Don't include the forge patch (which is a bug), see https://github.com/MinecraftForge/MinecraftForge/issues/8347
        if (pair == null)
        {
            return InteractionResult.PASS;
        }
        else
        {
            Predicate<UseOnContext> predicate = pair.getFirst();
            Consumer<UseOnContext> consumer = pair.getSecond();
            if (predicate.test(context))
            {
                Player player = context.getPlayer();
                level.playSound(player, blockpos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (!level.isClientSide)
                {
                    consumer.accept(context);
                    if (player != null)
                    {
                        context.getItemInHand().hurtAndBreak(1, player, (contextEntity_) -> {
                            contextEntity_.broadcastBreakEvent(context.getHand());
                        });
                    }
                }

                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            else
            {
                return InteractionResult.PASS;
            }
        }
    }

    /**
     * Copy pasta from {@link net.minecraft.world.item.ShovelItem}
     */
    @SuppressWarnings("ALL")
    private InteractionResult onShovelItemUse(UseOnContext context)
    {
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        BlockState blockstate = level.getBlockState(blockpos);
        if (context.getClickedFace() == Direction.DOWN)
        {
            return InteractionResult.PASS;
        }
        else
        {
            Player player = context.getPlayer();
            BlockState blockstate1 = blockstate.getToolModifiedState(level, blockpos, player, context.getItemInHand(), net.minecraftforge.common.ToolActions.SHOVEL_FLATTEN);
            BlockState blockstate2 = null;
            if (blockstate1 != null && level.isEmptyBlock(blockpos.above()))
            {
                level.playSound(player, blockpos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
                blockstate2 = blockstate1;
            }
            else if (blockstate.getBlock() instanceof CampfireBlock && blockstate.getValue(CampfireBlock.LIT))
            {
                if (!level.isClientSide())
                {
                    level.levelEvent((Player) null, 1009, blockpos, 0);
                }

                CampfireBlock.dowse(context.getPlayer(), level, blockpos, blockstate);
                blockstate2 = blockstate.setValue(CampfireBlock.LIT, Boolean.valueOf(false));
            }

            if (blockstate2 != null)
            {
                if (!level.isClientSide)
                {
                    level.setBlock(blockpos, blockstate2, 11);
                    if (player != null)
                    {
                        context.getItemInHand().hurtAndBreak(1, player, (contextEntity_) -> {
                            contextEntity_.broadcastBreakEvent(context.getHand());
                        });
                    }
                }

                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            else
            {
                return InteractionResult.PASS;
            }
        }
    }
}