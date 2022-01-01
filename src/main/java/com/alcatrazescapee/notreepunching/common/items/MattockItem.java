/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.items;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.player.Player;
import net.minecraft.item.*;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolType;

import com.alcatrazescapee.notreepunching.mixin.item.AxeItemAccess;
import com.alcatrazescapee.notreepunching.mixin.item.HoeItemAccess;
import com.alcatrazescapee.notreepunching.mixin.item.ShovelItemAccess;
import com.alcatrazescapee.notreepunching.util.HarvestBlockHandler;

import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;

public class MattockItem extends DiggerItem
{
    public static final Set<Block> EFFECTIVE_BLOCKS = Stream.of(
        AxeItemAccess.getEffectiveBlocks(),
        ShovelItemAccess.getEffectiveBlocks(),
        HoeItemAccess.getEffectiveBlocks()
    ).flatMap(Collection::stream).collect(Collectors.toSet());

    public MattockItem(Tier tier, float attackDamageIn, float attackSpeedIn, Properties builder)
    {
        super(attackDamageIn, attackSpeedIn, tier, EFFECTIVE_BLOCKS, builder.addToolType(ToolType.AXE, tier.getLevel()).addToolType(ToolType.SHOVEL, tier.getLevel()).addToolType(ToolType.HOE, tier.getLevel()).addToolType(HarvestBlockHandler.MATTOCK, tier.getLevel()));
    }

    public float getDestroySpeed(ItemStack stack, BlockState state)
    {
        Material material = state.getMaterial();
        return AxeItemAccess.getEffectiveMaterials().contains(material) ? speed : super.getDestroySpeed(stack, state);
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

    public boolean isCorrectToolForDrops(BlockState blockIn)
    {
        return blockIn.is(Blocks.SNOW) || blockIn.is(Blocks.SNOW_BLOCK);
    }

    /**
     * Copy pasta from {@link AxeItem}
     */
    @SuppressWarnings("ALL")
    private InteractionResult onAxeItemUse(UseOnContext context)
    {
        Level world = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        BlockState stateIn = world.getBlockState(blockpos);
        BlockState stateOut = stateIn.getToolModifiedState(world, blockpos, context.getPlayer(), context.getItemInHand(), ToolType.AXE);
        if (stateOut != null)
        {
            Player playerentity = context.getPlayer();
            world.playSound(playerentity, blockpos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
            if (!world.isClientSide)
            {
                world.setBlock(blockpos, stateOut, 11);
                if (playerentity != null)
                {
                    context.getItemInHand().hurtAndBreak(1, playerentity, (p_220040_1_) -> {
                        p_220040_1_.broadcastBreakEvent(context.getHand());
                    });
                }
            }

            return InteractionResult.SUCCESS;
        }
        else
        {
            return InteractionResult.PASS;
        }
    }

    /**
     * Copy pasta from {@link HoeItem}
     */
    @SuppressWarnings("ALL")
    private InteractionResult onHoeItemUse(UseOnContext context)
    {
        Level world = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        int hook = net.minecraftforge.event.ForgeEventFactory.onHoeUse(context);
        if (hook != 0) return hook > 0 ? InteractionResult.SUCCESS : InteractionResult.FAIL;
        if (context.getClickedFace() != Direction.DOWN && world.isEmptyBlock(blockpos.above()))
        {
            BlockState blockstate = world.getBlockState(blockpos).getToolModifiedState(world, blockpos, context.getPlayer(), context.getItemInHand(), ToolType.HOE);
            if (blockstate != null)
            {
                Player playerentity = context.getPlayer();
                world.playSound(playerentity, blockpos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (!world.isClientSide)
                {
                    world.setBlock(blockpos, blockstate, 11);
                    if (playerentity != null)
                    {
                        context.getItemInHand().hurtAndBreak(1, playerentity, (p_220043_1_) -> {
                            p_220043_1_.broadcastBreakEvent(context.getHand());
                        });
                    }
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    /**
     * Copy pasta from {@link ShovelItem}
     */
    @SuppressWarnings("ALL")
    private InteractionResult onShovelItemUse(UseOnContext context)
    {
        Level world = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        BlockState blockstate = world.getBlockState(blockpos);
        if (context.getClickedFace() == Direction.DOWN)
        {
            return InteractionResult.PASS;
        }
        else
        {
            Player playerentity = context.getPlayer();
            BlockState blockstate1 = blockstate.getToolModifiedState(world, context.getClickedPos(), playerentity, context.getItemInHand(), ToolType.SHOVEL);
            BlockState blockstate2 = null;
            if (blockstate1 != null && world.isEmptyBlock(blockpos.above()))
            {
                world.playSound(playerentity, blockpos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
                blockstate2 = blockstate1;
            }
            else if (blockstate.getBlock() instanceof CampfireBlock && blockstate.getValue(CampfireBlock.LIT))
            {
                world.levelEvent((Player) null, 1009, blockpos, 0);
                blockstate2 = blockstate.setValue(CampfireBlock.LIT, Boolean.valueOf(false));
            }
            if (blockstate2 != null)
            {
                if (!world.isClientSide)
                {
                    world.setBlock(blockpos, blockstate2, 11);
                    if (playerentity != null)
                    {
                        context.getItemInHand().hurtAndBreak(1, playerentity, (p_220041_1_) -> {
                            p_220041_1_.broadcastBreakEvent(context.getHand());
                        });
                    }
                }
                return InteractionResult.SUCCESS;
            }
            else
            {
                return InteractionResult.PASS;
            }
        }
    }
}