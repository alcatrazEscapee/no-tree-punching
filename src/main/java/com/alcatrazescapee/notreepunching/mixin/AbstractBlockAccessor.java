package com.alcatrazescapee.notreepunching.mixin;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockBehaviour.class)
public interface AbstractBlockAccessor
{
    /**
     * Gets the properties from the block in order to mutate it
     */
    @Accessor("properties")
    BlockBehaviour.Properties getProperties();

    /**
     * This is required as when adding tool types to block's based on material, we cannot discriminate against the individual block state as the tool type field does not allow for that. So we query the original material rather than each block state.
     */
    @Accessor("material")
    Material getMaterial();
}