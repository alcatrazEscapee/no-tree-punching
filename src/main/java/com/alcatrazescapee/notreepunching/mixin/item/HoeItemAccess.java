/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.mixin.item;

import java.util.Set;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.HoeItem;

import mcp.MethodsReturnNonnullByDefault;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@MethodsReturnNonnullByDefault
@Mixin(HoeItem.class)
public interface HoeItemAccess
{
    /**
     * Used by mattocks to emulate vanilla block breaking checks.
     */
    @Accessor("DIGGABLES")
    static Set<Block> getEffectiveBlocks() { throw new AssertionError("HoeItemAccess mixin not applied"); }
}