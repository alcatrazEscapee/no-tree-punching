package com.alcatrazescapee.notreepunching.util;

import javax.annotation.Nullable;

import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;

import com.alcatrazescapee.notreepunching.common.ModTags;

/**
 * This represents a primary tool that must be present on a given block
 * Blocks can be added to any number of tags to determine their validity with such a tool
 * However, all blocks have a fallback 'tool type' which is known to NTP
 * This is because when we check if a block is harvestable by an item, we have no way of knowing if that block is harvestable by *any* item.
 */
public enum ToolType
{
    PICKAXE(ModTags.Items.PICKAXE_TOOLS), // Stone
    AXE(ModTags.Items.AXE_TOOLS), // Wood
    SHOVEL(ModTags.Items.SHOVEL_TOOLS), // Earth, Dirt
    HOE(ModTags.Items.HOE_TOOLS), // Plants, Misc.
    SHARP(ModTags.Items.SHARP_TOOLS), // (Of Swords and Axes) Plants
    NONE(null);

    @Nullable private final Tag.Named<Item> tag;

    ToolType(@Nullable Tag.Named<Item> tag)
    {
        this.tag = tag;
    }

    public boolean is(Item item)
    {
        return this.tag != null && this.tag.contains(item);
    }
}
