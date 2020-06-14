package com.alcatrazescapee.notreepunching.common;

import net.minecraft.item.Item;
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

        private static Tag<Item> create(String id)
        {
            return new ItemTags.Wrapper(new ResourceLocation(MOD_ID, id));
        }
    }
}
