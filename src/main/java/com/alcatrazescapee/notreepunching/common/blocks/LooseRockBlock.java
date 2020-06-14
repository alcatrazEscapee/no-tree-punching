/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.common.blocks;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import net.minecraftforge.items.ItemHandlerHelper;

import com.alcatrazescapee.notreepunching.ModConfig;

public class LooseRockBlock extends Block
{
    public static final VoxelShape SHAPE = makeCuboidShape(6, 0, 6, 10, 1, 10);

    public LooseRockBlock()
    {
        super(Properties.create(Material.EARTH).sound(SoundType.STONE).hardnessAndResistance(0.15f));
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return SHAPE;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
        if (!worldIn.isRemote)
        {
            // Breaks rock if the block under it breaks.
            BlockState stateUnder = worldIn.getBlockState(pos.down());
            if (!stateUnder.isSolidSide(worldIn, pos.down(), Direction.UP))
            {
                worldIn.destroyBlock(pos, true);
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        if (ModConfig.BALANCE.canPickUpRocks)
        {
            ItemStack stack = getPickBlock(state, hit, worldIn, pos, player);
            ItemHandlerHelper.giveItemToPlayer(player, stack);
            worldIn.destroyBlock(pos, false);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    @Override
    public boolean canSpawnInBlock()
    {
        return true;
    }
}
