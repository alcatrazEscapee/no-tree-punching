package com.alcatrazescapee.notreepunching.platform.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

@FunctionalInterface
public interface BlockEntityFactory<T extends BlockEntity>
{
    T create(BlockPos pos, BlockState state);
}
