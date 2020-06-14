/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Copyright (c) 2019. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common;

import net.minecraft.item.IItemTier;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;

import com.alcatrazescapee.core.util.ModItemTier;

public final class ModTiers
{
    public static final IItemTier FLINT = new ModItemTier(0, 60, 2.5f, 0.5f, 0, () -> Ingredient.fromItems(Items.FLINT));
}
