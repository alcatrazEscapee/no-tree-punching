/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.common.items;

import com.google.common.collect.Sets;
import net.minecraft.block.*;
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

import com.alcatrazescapee.notreepunching.common.ModItemGroups;


public class MattockItem extends ToolItem
{
    public MattockItem(IItemTier tier, float attackDamageIn, float attackSpeedIn)
    {
        super(attackDamageIn, attackSpeedIn, tier, Sets.union(AxeItem.EFFECTIVE_ON, ShovelItem.EFFECTIVE_ON), new Properties().group(ModItemGroups.ITEMS).addToolType(ToolType.AXE, tier.getHarvestLevel()).addToolType(ToolType.SHOVEL, tier.getHarvestLevel()));
    }

    public float getDestroySpeed(ItemStack stack, BlockState state)
    {
        Material material = state.getMaterial();
        return material != Material.WOOD && material != Material.PLANTS && material != Material.TALL_PLANTS && material != Material.BAMBOO ? super.getDestroySpeed(stack, state) : this.efficiency;
    }

    public boolean canHarvestBlock(BlockState blockIn)
    {
        Block block = blockIn.getBlock();
        return block == Blocks.SNOW || block == Blocks.SNOW_BLOCK;
    }

    /**
     * If not sneaking, use in order of Axe -> Shovel -> Hoe
     * Otherwise, use in order of Axe -> Hoe -> Shovel
     * This is done as hoe and shovel have a possibility of conflicting
     */
    @Override
    public ActionResultType onItemUse(ItemUseContext context)
    {
        ActionResultType result = onAxeItemUse(context);
        if (result == ActionResultType.PASS)
        {
            if (context.getPlayer() != null && context.getPlayer().isSneaking())
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

    /**
     * Copy pasta from {@link AxeItem}
     */
    @SuppressWarnings("ALL")
    private ActionResultType onAxeItemUse(ItemUseContext context)
    {
        World world = context.getWorld();
        BlockPos blockpos = context.getPos();
        BlockState blockstate = world.getBlockState(blockpos);
        Block block = AxeItem.BLOCK_STRIPPING_MAP.get(blockstate.getBlock());
        if (block != null) {
            PlayerEntity playerentity = context.getPlayer();
            world.playSound(playerentity, blockpos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
            if (!world.isRemote) {
                world.setBlockState(blockpos, block.getDefaultState().with(RotatedPillarBlock.AXIS, blockstate.get(RotatedPillarBlock.AXIS)), 11);
                if (playerentity != null) {
                    context.getItem().damageItem(1, playerentity, (p_220040_1_) -> {
                        p_220040_1_.sendBreakAnimation(context.getHand());
                    });
                }
            }

            return ActionResultType.SUCCESS;
        } else {
            return ActionResultType.PASS;
        }
    }

    /**
     * Copy pasta from {@link HoeItem}
     */
    @SuppressWarnings("ALL")
    private ActionResultType onHoeItemUse(ItemUseContext context)
    {
        World world = context.getWorld();
        BlockPos blockpos = context.getPos();
        int hook = net.minecraftforge.event.ForgeEventFactory.onHoeUse(context);
        if (hook != 0) return hook > 0 ? ActionResultType.SUCCESS : ActionResultType.FAIL;
        if (context.getFace() != Direction.DOWN && world.isAirBlock(blockpos.up()))
        {
            BlockState blockstate = HoeItem.HOE_LOOKUP.get(world.getBlockState(blockpos).getBlock());
            if (blockstate != null)
            {
                PlayerEntity playerentity = context.getPlayer();
                world.playSound(playerentity, blockpos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                if (!world.isRemote)
                {
                    world.setBlockState(blockpos, blockstate, 11);
                    if (playerentity != null)
                    {
                        context.getItem().damageItem(1, playerentity, (p_220043_1_) -> {
                            p_220043_1_.sendBreakAnimation(context.getHand());
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
        World world = context.getWorld();
        BlockPos blockpos = context.getPos();
        BlockState blockstate = world.getBlockState(blockpos);
        if (context.getFace() == Direction.DOWN)
        {
            return ActionResultType.PASS;
        }
        else
        {
            PlayerEntity playerentity = context.getPlayer();
            BlockState blockstate1 = ShovelItem.SHOVEL_LOOKUP.get(blockstate.getBlock());
            BlockState blockstate2 = null;
            if (blockstate1 != null && world.isAirBlock(blockpos.up()))
            {
                world.playSound(playerentity, blockpos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);
                blockstate2 = blockstate1;
            }
            else if (blockstate.getBlock() instanceof CampfireBlock && blockstate.get(CampfireBlock.LIT))
            {
                world.playEvent((PlayerEntity) null, 1009, blockpos, 0);
                blockstate2 = blockstate.with(CampfireBlock.LIT, Boolean.valueOf(false));
            }
            if (blockstate2 != null)
            {
                if (!world.isRemote)
                {
                    world.setBlockState(blockpos, blockstate2, 11);
                    if (playerentity != null)
                    {
                        context.getItem().damageItem(1, playerentity, (p_220041_1_) -> {
                            p_220041_1_.sendBreakAnimation(context.getHand());
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
