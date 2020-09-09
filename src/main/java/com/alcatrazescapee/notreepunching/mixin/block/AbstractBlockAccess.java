/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.mixin.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.material.Material;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractBlock.class)
public interface AbstractBlockAccess
{
    /**
     * Gets the properties from the block in order to mutate it
     */
    @Accessor("properties")
    AbstractBlock.Properties getProperties();

    /**
     * This is required as when adding tool types to blocks based on material, we cannot discriminate against the individual block state as the tool type field does not allow for that. So we query the original material rather than each block state.
     */
    @Accessor("material")
    Material getMaterial();
}