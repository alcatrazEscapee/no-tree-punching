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
@SuppressWarnings("ConstantConditions")
public interface AxeItemAccess
{
    @Accessor("DIGGABLE_MATERIALS")
    static Set<Material> getEffectiveMaterials() { return null; }

    @Accessor("OTHER_DIGGABLE_BLOCKS")
    static Set<Block> getEffectiveBlocks() { return null; }
}