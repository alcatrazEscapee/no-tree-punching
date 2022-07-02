package com.alcatrazescapee.notreepunching.common.blocks;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;

import com.alcatrazescapee.notreepunching.Config;
import com.alcatrazescapee.notreepunching.common.blockentity.LargeVesselBlockEntity;
import com.alcatrazescapee.notreepunching.common.blockentity.ModBlockEntities;

public class LargeVesselBlock extends Block implements EntityBlock
{
    private static final VoxelShape SHAPE = box(2, 0, 2, 14, 14, 14);

    public LargeVesselBlock()
    {
        super(Properties.of(Material.STONE).strength(1.0f));
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (!newState.is(state.getBlock()))
        {
            level.getBlockEntity(pos, ModBlockEntities.LARGE_VESSEL.get()).ifPresent(vessel -> {
                if (!Config.INSTANCE.largeVesselKeepsContentsWhenBroken.get())
                {
                    vessel.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).map(i -> (ItemStackHandler) i).ifPresent(inventory -> {
                        for (int i = 0; i < inventory.getSlots(); i++)
                        {
                            final ItemStack stack = inventory.getStackInSlot(i);
                            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
                            inventory.setStackInSlot(i, ItemStack.EMPTY);
                        }
                    });
                }
                vessel.onRemove();
                level.updateNeighbourForOutputSignal(pos, this);
            });
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit)
    {
        if (player instanceof ServerPlayer serverPlayer && !player.isShiftKeyDown())
        {
            level.getBlockEntity(pos, ModBlockEntities.LARGE_VESSEL.get()).ifPresent(tile -> NetworkHooks.openGui(serverPlayer, tile, pos));
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
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        if (stack.hasCustomHoverName())
        {
            level.getBlockEntity(pos, ModBlockEntities.LARGE_VESSEL.get()).ifPresent(tile -> tile.setCustomName(stack.getHoverName()));
        }
    }

    /**
     * Causes the block to drop with contents in creative
     */
    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player)
    {
        level.getBlockEntity(pos, ModBlockEntities.LARGE_VESSEL.get()).ifPresent(tile -> tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
            if (!level.isClientSide && player.isCreative() && !tile.isEmpty())
            {
                ItemStack stack = new ItemStack(this);
                tile.saveToItem(stack);

                stack.setHoverName(tile.getDisplayName());
                ItemEntity itemEntity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), stack);
                itemEntity.setDefaultPickUpDelay();
                level.addFreshEntity(itemEntity);
            }
        }));
        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player)
    {
        final ItemStack stack = super.getCloneItemStack(state, target, level, pos, player);
        level.getBlockEntity(pos, ModBlockEntities.LARGE_VESSEL.get()).ifPresent(tile -> tile.saveToItem(stack));
        return stack;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new LargeVesselBlockEntity(pos, state);
    }
}