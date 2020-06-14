/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.common.items;

import com.alcatrazescapee.notreepunching.common.ModItemGroups;
import com.alcatrazescapee.notreepunching.common.ModTiers;

import net.minecraft.item.*;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

@SuppressWarnings("unused")
public final class ModItems
{
    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, MOD_ID);

    public static final RegistryObject<Item> FLINT_SHARD = register("flint_shard");
    public static final RegistryObject<Item> GRASS_FIBER = register("plant_fiber");
    public static final RegistryObject<Item> GRASS_STRING = register("plant_string");
    public static final RegistryObject<Item> CLAY_BRICK = register("clay_brick");
    public static final RegistryObject<SmallVesselItem> CERAMIC_SMALL_VESSEL = register("ceramic_small_vessel", SmallVesselItem::new);
    public static final RegistryObject<CeramicBucketItem> CERAMIC_BUCKET = register("ceramic_bucket", () -> new CeramicBucketItem(new Item.Properties().group(ModItemGroups.ITEMS).maxStackSize(1)));
    public static final RegistryObject<ClayToolItem> CLAY_TOOL = register("clay_tool", ClayToolItem::new);
    public static final RegistryObject<FireStarterItem> FIRE_STARTER = register("fire_starter", FireStarterItem::new);

    public static final RegistryObject<TieredItem> FLINT_AXE = register("flint_axe", () -> new AxeItem(ModTiers.FLINT, 3.0f, -3.3f, new Item.Properties().group(ModItemGroups.ITEMS)));
    public static final RegistryObject<TieredItem> FLINT_PICKAXE = register("flint_pickaxe", () -> new PickaxeItem(ModTiers.FLINT, 1, -2.8f, new Item.Properties().group(ModItemGroups.ITEMS)));
    public static final RegistryObject<TieredItem> FLINT_HOE = register("flint_hoe", () -> new HoeItem(ModTiers.FLINT, -3.0f, new Item.Properties().group(ModItemGroups.ITEMS)));
    public static final RegistryObject<TieredItem> FLINT_SHOVEL = register("flint_shovel", () -> new ShovelItem(ModTiers.FLINT, -1.0f, -3.0f, new Item.Properties().group(ModItemGroups.ITEMS)));
    public static final RegistryObject<SwordItem> MACUAHUITL = register("macuahuitl", () -> new SwordItem(ModTiers.FLINT, 3, -2.4f, new Item.Properties().group(ModItemGroups.ITEMS)));

    public static final RegistryObject<KnifeItem> FLINT_KNIFE = register("flint_knife", () -> new KnifeItem(ModTiers.FLINT, 1, -2.2f, new Item.Properties().group(ModItemGroups.ITEMS)));
    public static final RegistryObject<KnifeItem> IRON_KNIFE = register("iron_knife", () -> new KnifeItem(ItemTier.IRON, 1, -2.2f, new Item.Properties().group(ModItemGroups.ITEMS)));
    public static final RegistryObject<KnifeItem> GOLD_KNIFE = register("gold_knife", () -> new KnifeItem(ItemTier.GOLD, 1, -2.2f, new Item.Properties().group(ModItemGroups.ITEMS)));
    public static final RegistryObject<KnifeItem> DIAMOND_KNIFE = register("diamond_knife", () -> new KnifeItem(ItemTier.DIAMOND, 1, -2.2f, new Item.Properties().group(ModItemGroups.ITEMS)));

    public static final RegistryObject<MattockItem> IRON_MATTOCK = register("iron_mattock", () -> new MattockItem(ItemTier.IRON, 0.5f, -3.0f));
    public static final RegistryObject<MattockItem> GOLD_MATTOCK = register("gold_mattock", () -> new MattockItem(ItemTier.GOLD, 0.5f, -3.0f));
    public static final RegistryObject<MattockItem> DIAMOND_MATTOCK = register("diamond_mattock", () -> new MattockItem(ItemTier.DIAMOND, 0.5f, -3.0f));

    public static final RegistryObject<SawItem> IRON_SAW = register("iron_saw", () -> new SawItem(ItemTier.IRON, 3.0f, -3.2f));
    public static final RegistryObject<SawItem> GOLD_SAW = register("gold_saw", () -> new SawItem(ItemTier.GOLD, 3.0f, -3.2f));
    public static final RegistryObject<SawItem> DIAMOND_SAW = register("diamond_saw", () -> new SawItem(ItemTier.DIAMOND, 2.0f, -3.2f));

    private static RegistryObject<Item> register(String name)
    {
        return register(name, () -> new Item(new Item.Properties().group(ModItemGroups.ITEMS)));
    }

    private static <T extends Item> RegistryObject<T> register(String name, Supplier<T> item)
    {
        return ITEMS.register(name, item);
    }
}
