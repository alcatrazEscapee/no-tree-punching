/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.mixin.item;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.AxeItem;

import mcp.MethodsReturnNonnullByDefault;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AxeItem.class)
@MethodsReturnNonnullByDefault
public interface AxeItemAccess
{
    /**
     * Used by mattocks to emulate vanilla block breaking checks.
     */
    @Accessor("DIGGABLE_MATERIALS")
    static Set<Material> getEffectiveMaterials() { throw new AssertionError("AxeItemAccess mixin not applied"); }

    /**
     * Used by mattocks to emulate vanilla block breaking checks.
     */
    @Accessor("OTHER_DIGGABLE_BLOCKS")
    static Set<Block> getEffectiveBlocks() { throw new AssertionError("AxeItemAccess mixin not applied"); }
}