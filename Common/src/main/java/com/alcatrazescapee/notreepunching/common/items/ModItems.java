package com.alcatrazescapee.notreepunching.common.items;

import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import com.alcatrazescapee.notreepunching.common.ModTiers;
import com.alcatrazescapee.notreepunching.platform.RegistryHolder;
import com.alcatrazescapee.notreepunching.platform.RegistryInterface;
import com.alcatrazescapee.notreepunching.platform.XPlatform;
import com.alcatrazescapee.notreepunching.util.Helpers;

@SuppressWarnings("unused")
public final class ModItems
{
    public static final RegistryInterface<Item> ITEMS = XPlatform.INSTANCE.registryInterface(Registry.ITEM);

    public static final RegistryHolder<Item> FLINT_SHARD = register("flint_shard");
    public static final RegistryHolder<Item> GRASS_FIBER = register("plant_fiber");
    public static final RegistryHolder<Item> GRASS_STRING = register("plant_string");
    public static final RegistryHolder<Item> CLAY_BRICK = register("clay_brick");

    public static final RegistryHolder<SmallVesselItem> CERAMIC_SMALL_VESSEL = register("ceramic_small_vessel", SmallVesselItem::new);
    public static final RegistryHolder<CeramicBucketItem> CERAMIC_BUCKET = register("ceramic_bucket", () -> new CeramicBucketItem(Fluids.EMPTY, new Item.Properties().tab(Tab.ITEMS).stacksTo(1)));
    public static final RegistryHolder<CeramicBucketItem> CERAMIC_WATER_BUCKET = register("ceramic_water_bucket", () -> new CeramicBucketItem(Fluids.WATER, new Item.Properties().tab(Tab.ITEMS).stacksTo(1).craftRemainder(CERAMIC_BUCKET.get())));

    public static final RegistryHolder<ClayToolItem> CLAY_TOOL = register("clay_tool", ClayToolItem::new);
    public static final RegistryHolder<FireStarterItem> FIRE_STARTER = register("fire_starter", FireStarterItem::new);

    public static final RegistryHolder<TieredItem> FLINT_AXE = register("flint_axe", () -> axe(ModTiers.FLINT, 3.0f, -3.3f, new Item.Properties().tab(Tab.ITEMS)));
    public static final RegistryHolder<TieredItem> FLINT_PICKAXE = register("flint_pickaxe", () -> new PickaxeItem(ModTiers.FLINT, 1, -2.8f, new Item.Properties().tab(Tab.ITEMS)) {});
    public static final RegistryHolder<TieredItem> FLINT_HOE = register("flint_hoe", () -> new HoeItem(ModTiers.FLINT, 0, -3.0f, new Item.Properties().tab(Tab.ITEMS)) {});
    public static final RegistryHolder<TieredItem> FLINT_SHOVEL = register("flint_shovel", () -> new ShovelItem(ModTiers.FLINT, -1.0f, -3.0f, new Item.Properties().tab(Tab.ITEMS)));
    public static final RegistryHolder<SwordItem> MACUAHUITL = register("macuahuitl", () -> new SwordItem(ModTiers.FLINT, 3, -2.4f, new Item.Properties().tab(Tab.ITEMS)));

    public static final RegistryHolder<KnifeItem> FLINT_KNIFE = register("flint_knife", () -> new KnifeItem(ModTiers.FLINT, 1, -2.2f, new Item.Properties().tab(Tab.ITEMS)));
    public static final RegistryHolder<KnifeItem> IRON_KNIFE = register("iron_knife", () -> new KnifeItem(Tiers.IRON, 1, -2.2f, new Item.Properties().tab(Tab.ITEMS)));
    public static final RegistryHolder<KnifeItem> GOLD_KNIFE = register("gold_knife", () -> new KnifeItem(Tiers.GOLD, 1, -2.2f, new Item.Properties().tab(Tab.ITEMS)));
    public static final RegistryHolder<KnifeItem> DIAMOND_KNIFE = register("diamond_knife", () -> new KnifeItem(Tiers.DIAMOND, 1, -2.2f, new Item.Properties().tab(Tab.ITEMS)));
    public static final RegistryHolder<KnifeItem> NETHERTE_KNIFE = register("netherite_knife", () -> new KnifeItem(Tiers.NETHERITE, 1, -2.2f, new Item.Properties().tab(Tab.ITEMS)));

    public static final RegistryHolder<MattockItem> IRON_MATTOCK = register("iron_mattock", () -> new MattockItem(Tiers.IRON, 0.5f, -3.0f, new Item.Properties().tab(Tab.ITEMS)));
    public static final RegistryHolder<MattockItem> GOLD_MATTOCK = register("gold_mattock", () -> new MattockItem(Tiers.GOLD, 0.5f, -3.0f, new Item.Properties().tab(Tab.ITEMS)));
    public static final RegistryHolder<MattockItem> DIAMOND_MATTOCK = register("diamond_mattock", () -> new MattockItem(Tiers.DIAMOND, 0.5f, -3.0f, new Item.Properties().tab(Tab.ITEMS)));
    public static final RegistryHolder<MattockItem> NETHERITE_MATTOCK = register("netherite_mattock", () -> new MattockItem(Tiers.NETHERITE, 0.5f, -3.0f, new Item.Properties().tab(Tab.ITEMS)));

    public static final RegistryHolder<AxeItem> IRON_SAW = register("iron_saw", () -> axe(Tiers.IRON, 2.0f, -3.2f, new Item.Properties().tab(Tab.ITEMS)));
    public static final RegistryHolder<AxeItem> GOLD_SAW = register("gold_saw", () -> axe(Tiers.GOLD, 2.0f, -3.2f, new Item.Properties().tab(Tab.ITEMS)));
    public static final RegistryHolder<AxeItem> DIAMOND_SAW = register("diamond_saw", () -> axe(Tiers.DIAMOND, 2.0f, -3.2f, new Item.Properties().tab(Tab.ITEMS)));
    public static final RegistryHolder<AxeItem> NETHERITE_SAW = register("netherite_saw", () -> axe(Tiers.NETHERITE, 2.0f, -3.2f, new Item.Properties().tab(Tab.ITEMS)));

    private static AxeItem axe(Tier tier, float attackDamage, float attackSpeed, Item.Properties properties)
    {
        return new AxeItem(tier, attackDamage, attackSpeed, properties) {};
    }

    private static RegistryHolder<Item> register(String name)
    {
        return register(name, () -> new Item(new Item.Properties().tab(Tab.ITEMS)));
    }

    private static <T extends Item> RegistryHolder<T> register(String name, Supplier<T> item)
    {
        return ITEMS.register(name, item);
    }

    public static final class Tab
    {
        public static final CreativeModeTab ITEMS = XPlatform.INSTANCE.creativeTab(Helpers.identifier("items"), () -> new ItemStack(FLINT_SHARD.get()));
    }
}