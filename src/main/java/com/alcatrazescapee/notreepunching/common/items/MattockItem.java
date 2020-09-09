/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.items;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import com.alcatrazescapee.notreepunching.mixin.item.AxeItemAccess;
import com.alcatrazescapee.notreepunching.mixin.item.HoeItemAccess;
import com.alcatrazescapee.notreepunching.mixin.item.ShovelItemAccess;
import com.alcatrazescapee.notreepunching.util.HarvestBlockHandler;

public class MattockItem extends ToolItem
{
    public static final Set<Block> EFFECTIVE_BLOCKS = Stream.of(
        AxeItemAccess.getEffectiveBlocks(),
        ShovelItemAccess.getEffectiveBlocks(),
        HoeItemAccess.getEffectiveBlocks()
    ).flatMap(Collection::stream).collect(Collectors.toSet());

    public MattockItem(IItemTier tier, float attackDamageIn, float attackSpeedIn, Properties builder)
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
    public ActionResultType useOn(ItemUseContext context)
    {
        ActionResultType result = onAxeItemUse(context);
        if (result == ActionResultType.PASS)
        {
            if (context.getPlayer() != null && context.getPlayer().isShiftKeyDown())
            {
                result = onHoeItemUse(context);
                if (result == ActionResultType.PASS)
                {
                    result = onShovelItemUse(context);
                }
            }
            else
            {
                result = onShovelItemUse(context);
                if (result == ActionResultType.PASS)
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
    private ActionResultType onAxeItemUse(ItemUseContext context)
    {
        World world = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        BlockState stateIn = world.getBlockState(blockpos);
        BlockState stateOut = stateIn.getToolModifiedState(world, blockpos, context.getPlayer(), context.getItemInHand(), ToolType.AXE);
        if (stateOut != null)
        {
            PlayerEntity playerentity = context.getPlayer();
            world.playSound(playerentity, blockpos, SoundEvents.AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
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

            return ActionResultType.SUCCESS;
        }
        else
        {
            return ActionResultType.PASS;
        }
    }

    /**
     * Copy pasta from {@link HoeItem}
     */
    @SuppressWarnings("ALL")
    private ActionResultType onHoeItemUse(ItemUseContext context)
    {
        World world = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        int hook = net.minecraftforge.event.ForgeEventFactory.onHoeUse(context);
        if (hook != 0) return hook > 0 ? ActionResultType.SUCCESS : ActionResultType.FAIL;
        if (context.getClickedFace() != Direction.DOWN && world.isEmptyBlock(blockpos.above()))
        {
            BlockState blockstate = world.getBlockState(blockpos).getToolModifiedState(world, blockpos, context.getPlayer(), context.getItemInHand(), ToolType.HOE);
            if (blockstate != null)
            {
                PlayerEntity playerentity = context.getPlayer();
                world.playSound(playerentity, blockpos, SoundEvents.HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
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
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

    /**
     * Copy pasta from {@link ShovelItem}
     */
    @SuppressWarnings("ALL")
    private ActionResultType onShovelItemUse(ItemUseContext context)
    {
        World world = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        BlockState blockstate = world.getBlockState(blockpos);
        if (context.getClickedFace() == Direction.DOWN)
        {
            return ActionResultType.PASS;
        }
        else
        {
            PlayerEntity playerentity = context.getPlayer();
            BlockState blockstate1 = blockstate.getToolModifiedState(world, context.getClickedPos(), playerentity, context.getItemInHand(), ToolType.SHOVEL);
            BlockState blockstate2 = null;
            if (blockstate1 != null && world.isEmptyBlock(blockpos.above()))
            {
                world.playSound(playerentity, blockpos, SoundEvents.SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);
                blockstate2 = blockstate1;
            }
            else if (blockstate.getBlock() instanceof CampfireBlock && blockstate.getValue(CampfireBlock.LIT))
            {
                world.levelEvent((PlayerEntity) null, 1009, blockpos, 0);
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
                return ActionResultType.SUCCESS;
            }
            else
            {
                return ActionResultType.PASS;
            }
        }
    }
}