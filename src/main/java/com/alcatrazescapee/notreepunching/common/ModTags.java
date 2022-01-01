/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

public final class ModTags
{
    public static final class Items
    {
        public static final Tag.Named<Item> FIRE_STARTER_LOGS = create("fire_starter_logs");
        public static final Tag.Named<Item> FIRE_STARTER_KINDLING = create("fire_starter_kindling");
        public static final Tag.Named<Item> FIRE_STARTER_SOUL_FIRE_CATALYST = create("fire_starter_soul_fire_catalyst");

        public static final Tag.Named<Item> FLINT_KNAPPABLE = create("flint_knappable");

        public static final Tag.Named<Item> KNIVES = create("knives");

        public static final Tag.Named<Item> SMALL_VESSEL_BLACKLIST = create("small_vessel_blacklist");
        public static final Tag.Named<Item> LARGE_VESSEL_BLACKLIST = create("large_vessel_blacklist");

        private static Tag.Named<Item> create(String id)
        {
            return ItemTags.bind(new ResourceLocation(MOD_ID, id).toString());
        }
    }

    public static final class Blocks
    {
        public static final Tag.Named<Block> ALWAYS_BREAKABLE = create("always_breakable");
        public static final Tag.Named<Block> ALWAYS_DROPS = create("always_drops");

        public static final Tag.Named<Block> LOOSE_ROCK_PLACEABLE_ON = create("loose_rock_placeable_on");

        public static final Tag.Named<Block> NEEDS_FLINT_TOOL = create("needs_flint_tool");

        private static Tag.Named<Block> create(String id)
        {
            return BlockTags.bind(new ResourceLocation(MOD_ID, id).toString());
        }
    }
}