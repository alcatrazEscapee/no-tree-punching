/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.blocks;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;

import com.alcatrazescapee.notreepunching.common.tileentity.InventoryTileEntity;
import com.alcatrazescapee.notreepunching.common.tileentity.LargeVesselTileEntity;
import com.alcatrazescapee.notreepunching.util.Helpers;

public class LargeVesselBlock extends Block
{
    private static final VoxelShape SHAPE = makeCuboidShape(2, 0, 2, 14, 14, 14);

    public LargeVesselBlock()
    {
        super(Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(0).hardnessAndResistance(1.0f));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new LargeVesselTileEntity();
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return SHAPE;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        Helpers.getTE(worldIn, pos, InventoryTileEntity.class).ifPresent(tile -> {
            tile.onReplaced();
            worldIn.updateComparatorOutputLevel(pos, this);
        });
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResultType onUse(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        if (player instanceof ServerPlayerEntity && !player.isSneaking())
        {
            Helpers.getTE(worldIn, pos, LargeVesselTileEntity.class).ifPresent(tile -> NetworkHooks.openGui((ServerPlayerEntity) player, tile, pos));
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        if (stack.hasDisplayName())
        {
            Helpers.getTE(worldIn, pos, LargeVesselTileEntity.class).ifPresent(tile -> tile.setCustomName(stack.getDisplayName()));
        }
    }

    /**
     * Causes the block to drop with contents in creative
     */
    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player)
    {
        Helpers.getTE(worldIn, pos, LargeVesselTileEntity.class).ifPresent(tile -> tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
            if (!worldIn.isRemote && player.isCreative() && !tile.isEmpty())
            {
                ItemStack stack = new ItemStack(this);
                CompoundNBT compoundnbt = tile.write(new CompoundNBT());
                if (!compoundnbt.isEmpty())
                {
                    stack.setTagInfo("BlockEntityTag", compoundnbt);
                }

                stack.setDisplayName(tile.getDisplayName());
                ItemEntity itemEntity = new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);
                itemEntity.setDefaultPickupDelay();
                worldIn.addEntity(itemEntity);
            }
        }));
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
    {
        ItemStack stack = super.getPickBlock(state, target, world, pos, player);
        Helpers.getTE(world, pos, LargeVesselTileEntity.class).ifPresent(tile -> {
            CompoundNBT nbt = tile.write(new CompoundNBT());
            if (!nbt.isEmpty())
            {
                stack.setTagInfo("BlockEntityTag", nbt);
            }
        });
        return stack;
    }
}
