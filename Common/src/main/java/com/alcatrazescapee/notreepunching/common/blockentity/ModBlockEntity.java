package com.alcatrazescapee.notreepunching.common.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class ModBlockEntity extends BlockEntity
{
    protected ModBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
    }

    /**
     * @return The packet to send to the client upon block update.
     * Forge patches this to go through {@code IForgeBlockEntity#onDataPacket()}, which calls {@link #load(CompoundTag)}. On Fabric, this goes straight to {@link #load(CompoundTag)}
     */
    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this, BlockEntity::getUpdateTag);
    }

    /**
     * @return The tag containing information needed to send to the client, either on block update or on bulk chunk update. This tag gets returned in {@link #load(CompoundTag)}
     */
    @Override
    public CompoundTag getUpdateTag()
    {
        return saveWithoutMetadata();
    }

    @Override
    public final void load(CompoundTag tag)
    {
        super.load(tag);
        loadAdditional(tag);
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
