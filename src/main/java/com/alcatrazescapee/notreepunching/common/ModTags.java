/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Copyright (c) 2019. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

public final class ModTags
{
    public static final class Items
    {
        public static final Tag<Item> FIRE_STARTER_LOGS = create("fire_starter_logs");
        public static final Tag<Item> FIRE_STARTER_KINDLING = create("fire_starter_kindling");
        public static final Tag<Item> FIRE_STARTER_TINDER = create("fire_starter_tinder");

        public static final Tag<Item> FLINT_KNAPPABLE = create("flint_knappable");

        private static Tag<Item> create(String id)
        {
            return new ItemTags.Wrapper(new ResourceLocation(MOD_ID, id));
        }
    }

    public static final class Blocks
    {
        public static final Tag<Block> ALWAYS_BREAKABLE = create("always_breakable");
        public static final Tag<Block> ALWAYS_DROPS = create("always_drops");

        public static final Tag<Block> LOOSE_ROCK_PLACEABLE_ON = create("loose_rock_placeable_on");

        private static Tag<Block> create(String id)
        {
            return new BlockTags.Wrapper(new ResourceLocation(MOD_ID, id));
        }
    }

    public static final class Fluids
    {
        public static final Tag<Fluid> CERAMIC_BUCKETABLE = create("ceramic_bucketable");

        @SuppressWarnings("SameParameterValue")
        private static Tag<Fluid> create(String id)
        {
            return new FluidTags.Wrapper(new ResourceLocation(MOD_ID, id));
        }
    }
}
