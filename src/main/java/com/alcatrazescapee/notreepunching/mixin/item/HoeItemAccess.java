/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.mixin.item;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.item.HoeItem;

import mcp.MethodsReturnNonnullByDefault;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@MethodsReturnNonnullByDefault
@SuppressWarnings("ConstantConditions")
@Mixin(HoeItem.class)
public interface HoeItemAccess
{
    @Accessor("DIGGABLES")
    static Set<Block> getEffectiveBlocks() { return null; }
}