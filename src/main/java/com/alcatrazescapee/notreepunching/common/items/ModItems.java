/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.items;

import java.util.function.Supplier;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import com.alcatrazescapee.notreepunching.common.ModItemGroup;
import com.alcatrazescapee.notreepunching.common.ModItemTier;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;

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

    public static final RegistryObject<TieredItem> FLINT_AXE = register("flint_axe", () -> new AxeItem(ModItemTier.FLINT, 3.0f, -3.3f, new Item.Properties().tab(ModItemGroup.ITEMS)));
    public static final RegistryObject<TieredItem> FLINT_PICKAXE = register("flint_pickaxe", () -> new PickaxeItem(ModItemTier.FLINT, 1, -2.8f, new Item.Properties().tab(ModItemGroup.ITEMS)));
    public static final RegistryObject<TieredItem> FLINT_HOE = register("flint_hoe", () -> new HoeItem(ModItemTier.FLINT, 0, -3.0f, new Item.Properties().tab(ModItemGroup.ITEMS)));
    public static final RegistryObject<TieredItem> FLINT_SHOVEL = register("flint_shovel", () -> new ShovelItem(ModItemTier.FLINT, -1.0f, -3.0f, new Item.Properties().tab(ModItemGroup.ITEMS)));
    public static final RegistryObject<SwordItem> MACUAHUITL = register("macuahuitl", () -> new SwordItem(ModItemTier.FLINT, 3, -2.4f, new Item.Properties().tab(ModItemGroup.ITEMS)));

    public static final RegistryObject<KnifeItem> FLINT_KNIFE = register("flint_knife", () -> new KnifeItem(ModItemTier.FLINT, 1, -2.2f, new Item.Properties().tab(ModItemGroup.ITEMS)));
    public static final RegistryObject<KnifeItem> IRON_KNIFE = register("iron_knife", () -> new KnifeItem(Tiers.IRON, 1, -2.2f, new Item.Properties().tab(ModItemGroup.ITEMS)));
    public static final RegistryObject<KnifeItem> GOLD_KNIFE = register("gold_knife", () -> new KnifeItem(Tiers.GOLD, 1, -2.2f, new Item.Properties().tab(ModItemGroup.ITEMS)));
    public static final RegistryObject<KnifeItem> DIAMOND_KNIFE = register("diamond_knife", () -> new KnifeItem(Tiers.DIAMOND, 1, -2.2f, new Item.Properties().tab(ModItemGroup.ITEMS)));
    public static final RegistryObject<KnifeItem> NETHERTE_KNIFE = register("netherite_knife", () -> new KnifeItem(Tiers.NETHERITE, 1, -2.2f, new Item.Properties().tab(ModItemGroup.ITEMS)));

    public static final RegistryObject<MattockItem> IRON_MATTOCK = register("iron_mattock", () -> new MattockItem(Tiers.IRON, 0.5f, -3.0f, new Item.Properties().tab(ModItemGroup.ITEMS)));
    public static final RegistryObject<MattockItem> GOLD_MATTOCK = register("gold_mattock", () -> new MattockItem(Tiers.GOLD, 0.5f, -3.0f, new Item.Properties().tab(ModItemGroup.ITEMS)));
    public static final RegistryObject<MattockItem> DIAMOND_MATTOCK = register("diamond_mattock", () -> new MattockItem(Tiers.DIAMOND, 0.5f, -3.0f, new Item.Properties().tab(ModItemGroup.ITEMS)));
    public static final RegistryObject<MattockItem> NETHERITE_MATTOCK = register("netherite_mattock", () -> new MattockItem(Tiers.NETHERITE, 0.5f, -3.0f, new Item.Properties().tab(ModItemGroup.ITEMS)));

    public static final RegistryObject<AxeItem> IRON_SAW = register("iron_saw", () -> new AxeItem(Tiers.IRON, 2.0f, -3.2f, new Item.Properties().tab(ModItemGroup.ITEMS)));
    public static final RegistryObject<AxeItem> GOLD_SAW = register("gold_saw", () -> new AxeItem(Tiers.GOLD, 2.0f, -3.2f, new Item.Properties().tab(ModItemGroup.ITEMS)));
    public static final RegistryObject<AxeItem> DIAMOND_SAW = register("diamond_saw", () -> new AxeItem(Tiers.DIAMOND, 2.0f, -3.2f, new Item.Properties().tab(ModItemGroup.ITEMS)));
    public static final RegistryObject<AxeItem> NETHERITE_SAW = register("netherite_saw", () -> new AxeItem(Tiers.NETHERITE, 2.0f, -3.2f, new Item.Properties().tab(ModItemGroup.ITEMS)));

    private static RegistryObject<Item> register(String name)
    {
        return register(name, () -> new Item(new Item.Properties().tab(ModItemGroup.ITEMS)));
    }

    private static <T extends Item> RegistryObject<T> register(String name, Supplier<T> item)
    {
        return ITEMS.register(name, item);
    }
}