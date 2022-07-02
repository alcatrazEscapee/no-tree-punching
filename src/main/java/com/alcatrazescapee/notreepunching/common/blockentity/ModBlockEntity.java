package com.alcatrazescapee.notreepunching.common.blockentity;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class ModBlockEntity extends BlockEntity
{
    protected ModBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
    }

    /**
     * @return The packet to send to the client upon block update. This is returned in client in {@link #onDataPacket(Connection, ClientboundBlockEntityDataPacket)}
     */
    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this, BlockEntity::getUpdateTag);
    }

    /**
     * Handle a packet sent from {@link #getUpdatePacket()}. Delegates to {@link #handleUpdateTag(CompoundTag)}.
     */
    @Override
    public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket packet)
    {
        if (packet.getTag() != null)
        {
            handleUpdateTag(packet.getTag());
        }
    }

    /**
     * @return The tag containing information needed to send to the client, either on block update or on bulk chunk update. This tag is either returned with the packet in {@link #getUpdatePacket()} or {@link #handleUpdateTag(CompoundTag)} based on where it was called from.
     */
    @Override
    public CompoundTag getUpdateTag()
    {
        return saveWithoutMetadata();
    }

    /**
     * Handles an update tag sent from the server.
     */
    @Override
    public void handleUpdateTag(CompoundTag tag)
    {
        load(tag);
    }

    @Override
    public final void load(CompoundTag tag)
    {
        loadAdditional(tag);
        super.load(tag);
    }

    /**
     * Override to save block entity specific data.
     */
    @Override
    protected void saveAdditional(CompoundTag tag) {}

    /**
     * Override to load block entity specific data.
     */
    protected void loadAdditional(CompoundTag tag) {}

    /**
     * Causes a block update, synchronizes the changes to the client, and marks it as changed (to be saved).
     *
     * @see #markForSync() to skip the block update
     */
    public void markForBlockUpdate()
    {
        if (level != null)
        {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            setChanged();
        }
    }

    /**
     * Syncs the block entity to client, and marks it as changed (to be saved).
     */
    public void markForSync()
    {
        sendVanillaUpdatePacket();
        setChanged();
    }

    protected final void sendVanillaUpdatePacket()
    {
        final ClientboundBlockEntityDataPacket packet = getUpdatePacket();
        final BlockPos pos = getBlockPos();
        if (packet != null && level instanceof ServerLevel serverLevel)
        {
            serverLevel.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false).forEach(e -> e.connection.send(packet));
        }
    }
}
