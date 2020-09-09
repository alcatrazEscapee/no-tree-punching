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
    /**
     * Check if the settings requires a correct tool by default. This is then saved for later use if required via config settings, as this field will get set to true regardless (via the public mutator)
     */
    @Accessor("requiresCorrectToolForDrops")
    boolean getRequiresCorrectToolForDrops();
}