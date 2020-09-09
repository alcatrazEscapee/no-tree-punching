/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.mixin.block;

import net.minecraft.block.AbstractBlock;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractBlock.AbstractBlockState.class)
public interface AbstractBlockStateAccess
{
    /**
     * This value, despite being set on the properties, is copied to each block state. The property mutator is public, but we need this in order to mutate the same value on each block state.
     */
    @Accessor("requiresCorrectToolForDrops")
    void setRequiresCorrectToolForDrops(boolean value);
}