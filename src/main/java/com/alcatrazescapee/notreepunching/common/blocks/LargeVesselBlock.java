/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.blocks;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import com.alcatrazescapee.notreepunching.Config;
import com.alcatrazescapee.notreepunching.common.blockentity.InventoryBlockEntity;
import com.alcatrazescapee.notreepunching.common.blockentity.LargeVesselBlockEntity;
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
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (!newState.is(state.getBlock()))
        {
            Helpers.getTE(worldIn, pos, InventoryBlockEntity.class).ifPresent(tile -> {
                if (!Config.SERVER.largeVesselKeepsContentsWhenBroken.get())
                {
                    tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).map(i -> (ItemStackHandler) i).ifPresent(inventory -> {
                        for (int i = 0; i < inventory.getSlots(); i++)
                        {
                            final ItemStack stack = inventory.getStackInSlot(i);
                            Containers.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);
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
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit)
    {
        if (player instanceof ServerPlayer && !player.isShiftKeyDown())
        {
            Helpers.getTE(worldIn, pos, LargeVesselBlockEntity.class).ifPresent(tile -> NetworkHooks.openGui((ServerPlayer) player, tile, pos));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        return SHAPE;
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        if (stack.hasCustomHoverName())
        {
            Helpers.getTE(worldIn, pos, LargeVesselBlockEntity.class).ifPresent(tile -> tile.setCustomName(stack.getHoverName()));
        }
    }

    /**
     * Causes the block to drop with contents in creative
     */
    @Override
    public void playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player)
    {
        Helpers.getTE(worldIn, pos, LargeVesselBlockEntity.class).ifPresent(tile -> tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
            if (!worldIn.isClientSide && player.isCreative() && !tile.isEmpty())
            {
                ItemStack stack = new ItemStack(this);
                CompoundTag compoundnbt = tile.save(new CompoundTag());
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
    public BlockEntity createTileEntity(BlockState state, BlockGetter world)
    {
        return new LargeVesselBlockEntity();
    }

    @Override
    public ItemStack getPickBlock(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player)
    {
        ItemStack stack = super.getPickBlock(state, target, world, pos, player);
        Helpers.getTE(world, pos, LargeVesselBlockEntity.class).ifPresent(tile -> {
            CompoundTag nbt = tile.save(new CompoundTag());
            if (!nbt.isEmpty())
            {
                stack.addTagElement("BlockEntityTag", nbt);
            }
        });
        return stack;
    }
}