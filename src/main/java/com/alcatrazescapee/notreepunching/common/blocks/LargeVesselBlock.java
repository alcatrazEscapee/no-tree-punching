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
import net.minecraft.inventory.InventoryHelper;
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
import net.minecraftforge.items.ItemStackHandler;

import com.alcatrazescapee.notreepunching.Config;
import com.alcatrazescapee.notreepunching.common.tileentity.InventoryTileEntity;
import com.alcatrazescapee.notreepunching.common.tileentity.LargeVesselTileEntity;
import com.alcatrazescapee.notreepunching.util.Helpers;

public class LargeVesselBlock extends Block
{
    private static final VoxelShape SHAPE = box(2, 0, 2, 14, 14, 14);

    public LargeVesselBlock()
    {
        super(Properties.of(Material.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(0).strength(1.0f));
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (!newState.is(state.getBlock()))
        {
            Helpers.getTE(worldIn, pos, InventoryTileEntity.class).ifPresent(tile -> {
                if (!Config.SERVER.largeVesselKeepsContentsWhenBroken.get())
                {
                    tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).map(i -> (ItemStackHandler) i).ifPresent(inventory -> {
                        for (int i = 0; i < inventory.getSlots(); i++)
                        {
                            final ItemStack stack = inventory.getStackInSlot(i);
                            InventoryHelper.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);
                            inventory.setStackInSlot(i, ItemStack.EMPTY);
                        }
                    });
                }
                tile.onRemove();
                worldIn.updateNeighbourForOutputSignal(pos, this);
            });
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        if (player instanceof ServerPlayerEntity && !player.isShiftKeyDown())
        {
            Helpers.getTE(worldIn, pos, LargeVesselTileEntity.class).ifPresent(tile -> NetworkHooks.openGui((ServerPlayerEntity) player, tile, pos));
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return SHAPE;
    }

    @Override
    public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        if (stack.hasCustomHoverName())
        {
            Helpers.getTE(worldIn, pos, LargeVesselTileEntity.class).ifPresent(tile -> tile.setCustomName(stack.getHoverName()));
        }
    }

    /**
     * Causes the block to drop with contents in creative
     */
    @Override
    public void playerWillDestroy(World worldIn, BlockPos pos, BlockState state, PlayerEntity player)
    {
        Helpers.getTE(worldIn, pos, LargeVesselTileEntity.class).ifPresent(tile -> tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
            if (!worldIn.isClientSide && player.isCreative() && !tile.isEmpty())
            {
                ItemStack stack = new ItemStack(this);
                CompoundNBT compoundnbt = tile.save(new CompoundNBT());
                if (!compoundnbt.isEmpty())
                {
                    stack.addTagElement("BlockEntityTag", compoundnbt);
                }

                stack.setHoverName(tile.getDisplayName());
                ItemEntity itemEntity = new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);
                itemEntity.setDefaultPickUpDelay();
                worldIn.addFreshEntity(itemEntity);
            }
        }));
        super.playerWillDestroy(worldIn, pos, state, player);
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new LargeVesselTileEntity();
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
    {
        ItemStack stack = super.getPickBlock(state, target, world, pos, player);
        Helpers.getTE(world, pos, LargeVesselTileEntity.class).ifPresent(tile -> {
            CompoundNBT nbt = tile.save(new CompoundNBT());
            if (!nbt.isEmpty())
            {
                stack.addTagElement("BlockEntityTag", nbt);
            }
        });
        return stack;
    }
}