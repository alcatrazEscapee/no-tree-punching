/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common;

import java.util.function.Supplier;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.Lazy;

import com.alcatrazescapee.notreepunching.common.items.ModItems;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

/**
 * Simple implementation of {@link CreativeModeTab} which uses a lazily initialized icon stack.
 */
public final class ModItemGroup extends CreativeModeTab
{
    public static final CreativeModeTab ITEMS = new ModItemGroup(MOD_ID + ".items", () -> new ItemStack(ModItems.FLINT_SHARD.get()));

    private final Lazy<ItemStack> iconStack;

    public ModItemGroup(String label, Supplier<ItemStack> iconStack)
    {
        super(label);
        this.iconStack = Lazy.of(iconStack);
    }

    @Override
    public ItemStack makeIcon()
    {
        return iconStack.get();
    }
}