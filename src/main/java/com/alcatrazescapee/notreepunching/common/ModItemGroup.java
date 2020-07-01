/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common;

import java.util.function.Supplier;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.Lazy;

import com.alcatrazescapee.notreepunching.common.items.ModItems;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

/**
 * Simple implementation of {@link ItemGroup} which uses a lazily initialized icon stack.
 */
public class ModItemGroup extends ItemGroup
{
    public static final ItemGroup ITEMS = new ModItemGroup(MOD_ID + ".items", () -> new ItemStack(ModItems.FLINT_SHARD.get()));

    private final Lazy<ItemStack> iconStack;

    public ModItemGroup(String label, Supplier<ItemStack> iconStack)
    {
        super(label);
        this.iconStack = Lazy.of(iconStack);
    }

    @Override
    public ItemStack createIcon()
    {
        return iconStack.get();
    }
}
