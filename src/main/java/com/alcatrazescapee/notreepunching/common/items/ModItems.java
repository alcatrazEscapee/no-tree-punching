/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.items;

import java.util.function.Supplier;

import net.minecraft.item.*;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import com.alcatrazescapee.notreepunching.common.ModItemGroup;
import com.alcatrazescapee.notreepunching.common.ModItemTier;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

@SuppressWarnings("unused")
public final class ModItems
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static final RegistryObject<Item> FLINT_SHARD = register("flint_shard");
    public static final RegistryObject<Item> GRASS_FIBER = register("plant_fiber");
    public static final RegistryObject<Item> GRASS_STRING = register("plant_string");
    public static final RegistryObject<Item> CLAY_BRICK = register("clay_brick");
    public static final RegistryObject<SmallVesselItem> CERAMIC_SMALL_VESSEL = register("ceramic_small_vessel", SmallVesselItem::new);
    public static final RegistryObject<CeramicBucketItem> CERAMIC_BUCKET = register("ceramic_bucket", () -> new CeramicBucketItem(new Item.Properties().tab(ModItemGroup.ITEMS).stacksTo(1)));
    public static final RegistryObject<ClayToolItem> CLAY_TOOL = register("clay_tool", ClayToolItem::new);
    public static final RegistryObject<FireStarterItem> FIRE_STARTER = register("fire_starter", FireStarterItem::new);

    public static final RegistryObject<TieredItem> FLINT_AXE = register("flint_axe", () -> new CraftingAxeItem(ModItemTier.FLINT, 3.0f, -3.3f, new Item.Properties().tab(ModItemGroup.ITEMS)));
    public static final RegistryObject<TieredItem> FLINT_PICKAXE = register("flint_pickaxe", () -> new PickaxeItem(ModItemTier.FLINT, 1, -2.8f, new Item.Properties().tab(ModItemGroup.ITEMS)));
    public static final RegistryObject<TieredItem> FLINT_HOE = register("flint_hoe", () -> new HoeItem(ModItemTier.FLINT, 0, -3.0f, new Item.Properties().tab(ModItemGroup.ITEMS)));
    public static final RegistryObject<TieredItem> FLINT_SHOVEL = register("flint_shovel", () -> new ShovelItem(ModItemTier.FLINT, -1.0f, -3.0f, new Item.Properties().tab(ModItemGroup.ITEMS)));
    public static final RegistryObject<SwordItem> MACUAHUITL = register("macuahuitl", () -> new SwordItem(ModItemTier.FLINT, 3, -2.4f, new Item.Properties().tab(ModItemGroup.ITEMS)));

    public static final RegistryObject<KnifeItem> FLINT_KNIFE = register("flint_knife", () -> new KnifeItem(ModItemTier.FLINT, 1, -2.2f, new Item.Properties().tab(ModItemGroup.ITEMS)));
    public static final RegistryObject<KnifeItem> IRON_KNIFE = register("iron_knife", () -> new KnifeItem(ItemTier.IRON, 1, -2.2f, new Item.Properties().tab(ModItemGroup.ITEMS)));
    public static final RegistryObject<KnifeItem> GOLD_KNIFE = register("gold_knife", () -> new KnifeItem(ItemTier.GOLD, 1, -2.2f, new Item.Properties().tab(ModItemGroup.ITEMS)));
    public static final RegistryObject<KnifeItem> DIAMOND_KNIFE = register("diamond_knife", () -> new KnifeItem(ItemTier.DIAMOND, 1, -2.2f, new Item.Properties().tab(ModItemGroup.ITEMS)));

    public static final RegistryObject<MattockItem> IRON_MATTOCK = register("iron_mattock", () -> new MattockItem(ItemTier.IRON, 0.5f, -3.0f, new Item.Properties().tab(ModItemGroup.ITEMS)));
    public static final RegistryObject<MattockItem> GOLD_MATTOCK = register("gold_mattock", () -> new MattockItem(ItemTier.GOLD, 0.5f, -3.0f, new Item.Properties().tab(ModItemGroup.ITEMS)));
    public static final RegistryObject<MattockItem> DIAMOND_MATTOCK = register("diamond_mattock", () -> new MattockItem(ItemTier.DIAMOND, 0.5f, -3.0f, new Item.Properties().tab(ModItemGroup.ITEMS)));

    public static final RegistryObject<CraftingAxeItem> IRON_SAW = register("iron_saw", () -> new CraftingAxeItem(ItemTier.IRON, 3.0f, -3.2f, new Item.Properties().tab(ModItemGroup.ITEMS)));
    public static final RegistryObject<CraftingAxeItem> GOLD_SAW = register("gold_saw", () -> new CraftingAxeItem(ItemTier.GOLD, 3.0f, -3.2f, new Item.Properties().tab(ModItemGroup.ITEMS)));
    public static final RegistryObject<CraftingAxeItem> DIAMOND_SAW = register("diamond_saw", () -> new CraftingAxeItem(ItemTier.DIAMOND, 2.0f, -3.2f, new Item.Properties().tab(ModItemGroup.ITEMS)));

    private static RegistryObject<Item> register(String name)
    {
        return register(name, () -> new Item(new Item.Properties().tab(ModItemGroup.ITEMS)));
    }

    private static <T extends Item> RegistryObject<T> register(String name, Supplier<T> item)
    {
        return ITEMS.register(name, item);
    }
}