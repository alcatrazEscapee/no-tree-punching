package com.alcatrazescapee.notreepunching.common;

import net.minecraft.item.IItemTier;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;

import com.alcatrazescapee.core.util.ModItemTier;

public final class ModTiers
{
    public static final IItemTier FLINT = new ModItemTier(0, 45, 2.5f, 0.5f, 0, () -> Ingredient.fromItems(Items.FLINT));
}
