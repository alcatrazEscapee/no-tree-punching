/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Copyright (c) 2019. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.tileentity;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraft.world.server.ServerWorld;

/**
 * A tile entity that handles saving NBT data and default update packets
 *
 * @since 2.0.0
 */
public abstract class ModTileEntity extends TileEntity
{
    public ModTileEntity(TileEntityType<?> type)
    {
        super(type);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        return new SUpdateTileEntityPacket(getPos(), 1, write(new CompoundNBT()));
    }


    @Override
    public CompoundNBT getUpdateTag()
    {
        return write(super.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
    {
        read(pkt.getNbtCompound());
    }

    @Override
    public void handleUpdateTag(CompoundNBT nbt)
    {
        read(nbt);
    }

    /**
     * Syncs the TE data to client via means of a block update
     * Use for stuff that is updated infrequently, for data that is analogous to changing the state.
     * DO NOT call every tick
     */
    public void markForBlockUpdate()
    {
        if (world != null)
        {
            BlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
            markDirty();
        }
    }

    /**
     * Marks a tile entity for syncing without sending a block update.
     * Use preferentially over {@link InventoryTileEntity#markForBlockUpdate()} if there's no reason to have a block update.
     * For container based integer synchronization, see ITileFields
     * DO NOT call every tick
     */
    public void markForSync()
    {
        sendVanillaUpdatePacket();
        markDirty();
    }

    /**
     * Marks the tile entity dirty without updating comparator output.
     * Useful when called a lot for TE's that don't have a comparator output
     */
    protected void markDirtyFast()
    {
        if (world != null)
        {
            getBlockState();
            world.markChunkDirty(pos, this);
        }
    }

    protected void sendVanillaUpdatePacket()
    {
        SUpdateTileEntityPacket packet = getUpdatePacket();
        BlockPos pos = getPos();

        if (packet != null && world instanceof ServerWorld)
        {
            ((ServerChunkProvider) world.getChunkProvider()).chunkManager.getTrackingPlayers(new ChunkPos(pos), false).forEach(e -> e.connection.sendPacket(packet));
        }
    }
}

