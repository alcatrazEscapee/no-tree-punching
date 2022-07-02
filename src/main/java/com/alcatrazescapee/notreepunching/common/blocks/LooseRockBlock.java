package com.alcatrazescapee.notreepunching.common.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemHandlerHelper;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class LooseRockBlock extends Block
{
    public static final VoxelShape SHAPE = box(6, 0, 6, 10, 1, 10);

    public LooseRockBlock()
    {
        super(Properties.of(Material.DIRT).sound(SoundType.STONE).strength(0.15f).noCollission().noOcclusion());
    }

    @Override
    @SuppressWarnings("deprecation")
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
        if (!state.canSurvive(worldIn, pos) && !worldIn.isClientSide)
        {
            worldIn.destroyBlock(pos, true);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit)
    {
        ItemStack stack = getCloneItemStack(state, hit, worldIn, pos, player);
        ItemHandlerHelper.giveItemToPlayer(player, stack);
        worldIn.removeBlock(pos, false);
        return InteractionResult.SUCCESS;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos)
    {
        BlockState stateUnder = worldIn.getBlockState(pos.below());
        return stateUnder.isFaceSturdy(worldIn, pos.below(), Direction.UP);
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        return SHAPE;
    }

    @Override
    public boolean isPossibleToRespawnInThis()
    {
        return true;
    }
}