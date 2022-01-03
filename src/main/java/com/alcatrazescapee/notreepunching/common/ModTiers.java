/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;

/**
 * Mod-usable implementation of {@link Tier}
 */
public final class ModTiers
{
    public static final Tier FLINT = new ForgeTier(1, 60, 2.5f, 0.5f, 0, ModTags.Blocks.NEEDS_FLINT_TOOL, () -> Ingredient.of(Items.FLINT));
}