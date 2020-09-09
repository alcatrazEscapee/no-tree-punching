/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.mixin.block;

import net.minecraft.block.AbstractBlock;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractBlock.Properties.class)
public interface AbstractBlockPropertiesAccess
{
    @Accessor("requiresCorrectToolForDrops")
    boolean getRequiresCorrectToolForDrops();
}