package com.alcatrazescapee.notreepunching.common;

import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;

import com.alcatrazescapee.notreepunching.util.Helpers;

public final class ModTags
{
    public static final class Items
    {
        public static final TagKey<Item> FIRE_STARTER_LOGS = create("fire_starter_logs");
        public static final TagKey<Item> FIRE_STARTER_KINDLING = create("fire_starter_kindling");
        public static final TagKey<Item> FIRE_STARTER_SOUL_FIRE_CATALYST = create("fire_starter_soul_fire_catalyst");

        public static final TagKey<Item> FLINT_KNAPPABLE = create("flint_knappable");

        public static final TagKey<Item> SMALL_VESSEL_BLACKLIST = create("small_vessel_blacklist");
        public static final TagKey<Item> LARGE_VESSEL_BLACKLIST = create("large_vessel_blacklist");

        public static final TagKey<Item> PICKAXE_TOOLS = create("pickaxe_tools");
        public static final TagKey<Item> AXE_TOOLS = create("axe_tools");
        public static final TagKey<Item> SHOVEL_TOOLS = create("shovel_tools");
        public static final TagKey<Item> HOE_TOOLS = create("hoe_tools");
        public static final TagKey<Item> SHARP_TOOLS = create("sharp_tools");

        private static TagKey<Item> create(String id)
        {
            return TagKey.create(Registry.ITEM_REGISTRY, Helpers.identifier(id));
        }
    }

    public static final class Blocks
    {
        public static final TagKey<Block> ALWAYS_BREAKABLE = create("always_breakable");
        public static final TagKey<Block> ALWAYS_DROPS = create("always_drops");

        public static final TagKey<Block> LOOSE_ROCK_PLACEABLE_ON = create("loose_rock_placeable_on");

        public static final TagKey<Block> MINEABLE_WITH_MATTOCK = create("mineable_with_mattock");
        public static final TagKey<Block> NEEDS_FLINT_TOOL = create("needs_flint_tool");

        private static TagKey<Block> create(String id)
        {
            return TagKey.create(Registry.BLOCK_REGISTRY, Helpers.identifier(id));
        }
    }
}