package com.alcatrazescapee.notreepunching.mixin;

import net.minecraft.world.level.block.state.BlockBehaviour;
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
}