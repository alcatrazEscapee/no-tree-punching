package com.alcatrazescapee.notreepunching.common.blockentity;

import java.util.function.Supplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import com.alcatrazescapee.notreepunching.common.blocks.ModBlocks;
import com.alcatrazescapee.notreepunching.platform.RegistryHolder;
import com.alcatrazescapee.notreepunching.platform.RegistryInterface;
import com.alcatrazescapee.notreepunching.platform.XPlatform;
import com.alcatrazescapee.notreepunching.platform.event.BlockEntityFactory;

public class ModBlockEntities
{
    public static final RegistryInterface<BlockEntityType<?>> BLOCK_ENTITIES = XPlatform.INSTANCE.registryInterface(BuiltInRegistries.BLOCK_ENTITY_TYPE);

    public static final RegistryHolder<BlockEntityType<LargeVesselBlockEntity>> LARGE_VESSEL = register("large_vessel", LargeVesselBlockEntity::new, ModBlocks.CERAMIC_LARGE_VESSEL);

    private static <T extends BlockEntity> RegistryHolder<BlockEntityType<T>> register(String name, BlockEntityFactory<T> factory, Supplier<? extends Block> block)
    {
        return BLOCK_ENTITIES.register(name, () -> XPlatform.INSTANCE.blockEntityType(factory, block));
    }
}