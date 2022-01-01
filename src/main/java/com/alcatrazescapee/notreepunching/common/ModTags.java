/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

public final class ModTags
{
    public static final class Items
    {
        public static final ITag.INamedTag<Item> FIRE_STARTER_LOGS = create("fire_starter_logs");
        public static final ITag.INamedTag<Item> FIRE_STARTER_KINDLING = create("fire_starter_kindling");
        public static final ITag.INamedTag<Item> FIRE_STARTER_SOUL_FIRE_CATALYST = create("fire_starter_soul_fire_catalyst");

        public static final ITag.INamedTag<Item> FLINT_KNAPPABLE = create("flint_knappable");

        public static final ITag.INamedTag<Item> KNIVES = create("knives");

        public static final ITag.INamedTag<Item> SMALL_VESSEL_BLACKLIST = create("small_vessel_blacklist");
        public static final ITag.INamedTag<Item> LARGE_VESSEL_BLACKLIST = create("large_vessel_blacklist");

        private static ITag.INamedTag<Item> create(String id)
        {
            return ItemTags.bind(new ResourceLocation(MOD_ID, id).toString());
        }
    }

    public static final class Blocks
    {
        public static final ITag.INamedTag<Block> ALWAYS_BREAKABLE = create("always_breakable");
        public static final ITag.INamedTag<Block> ALWAYS_DROPS = create("always_drops");

        public static final ITag.INamedTag<Block> LOOSE_ROCK_PLACEABLE_ON = create("loose_rock_placeable_on");

        private static ITag.INamedTag<Block> create(String id)
        {
            return BlockTags.bind(new ResourceLocation(MOD_ID, id).toString());
        }
    }
}