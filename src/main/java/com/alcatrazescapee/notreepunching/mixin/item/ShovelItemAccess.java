/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.mixin.item;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.item.ShovelItem;

import mcp.MethodsReturnNonnullByDefault;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@MethodsReturnNonnullByDefault
@Mixin(ShovelItem.class)
public interface ShovelItemAccess
{
    /**
     * Used by mattocks to emulate vanilla block breaking checks
     */
    @Accessor("DIGGABLES")
    static Set<Block> getEffectiveBlocks() { throw new AssertionError("ShovelItemAccess mixin not applied"); }
}