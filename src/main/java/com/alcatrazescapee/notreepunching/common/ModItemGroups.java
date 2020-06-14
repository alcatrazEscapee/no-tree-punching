/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Copyright (c) 2019. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import com.alcatrazescapee.core.util.ModItemGroup;
import com.alcatrazescapee.notreepunching.common.items.ModItems;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

public final class ModItemGroups
{
    public static final ItemGroup ITEMS = new ModItemGroup(MOD_ID + ".items", () -> new ItemStack(ModItems.FLINT_SHARD.get()));
}
