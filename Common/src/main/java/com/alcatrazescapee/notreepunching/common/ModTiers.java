package com.alcatrazescapee.notreepunching.common;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

import com.alcatrazescapee.notreepunching.platform.XPlatform;

public final class ModTiers
{
    public static final Tier FLINT = XPlatform.INSTANCE.toolTier(60, 2.5f, 0.5f, 1, 0, ModTags.Blocks.NEEDS_FLINT_TOOL, () -> Ingredient.of(Items.FLINT));
}