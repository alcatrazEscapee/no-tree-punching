/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.common.items;

import com.google.common.collect.ImmutableMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.alcatrazescapee.alcatrazcore.item.ItemCore;
import com.alcatrazescapee.alcatrazcore.util.OreDictionaryHelper;
import com.alcatrazescapee.alcatrazcore.util.RegistryHelper;
import com.alcatrazescapee.alcatrazcore.util.collections.ImmutableEnumTable;
import com.alcatrazescapee.notreepunching.util.Metal;
import com.alcatrazescapee.notreepunching.util.Stone;

import static com.alcatrazescapee.alcatrazcore.util.CoreHelpers.getNull;
import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;
import static com.alcatrazescapee.notreepunching.client.ModTabs.TAB_ITEMS;
import static com.alcatrazescapee.notreepunching.client.ModTabs.TAB_TOOLS;

@GameRegistry.ObjectHolder(value = MOD_ID)
public class ModItems
{
    public static final Item FLINT_SHARD = getNull();
    public static final Item TUYERE = getNull();

    public static final Item SMALL_VESSEL = getNull();
    public static final Item CERAMIC_BUCKET = getNull();

    private static ImmutableMap<Metal.ToolType, Item> FLINT_TOOLS;
    private static ImmutableEnumTable<Metal.ToolType, Metal, Item> METAL_TOOLS;
    private static ImmutableEnumTable<Metal.ArmorType, Metal, ItemArmor> METAL_ARMOR;

    public static Item getFlintTool(Metal.ToolType type)
    {
        return FLINT_TOOLS.get(type);
    }

    public static Item getTool(Metal.ToolType type, Metal metal)
    {
        return METAL_TOOLS.get(type, metal);
    }

    public static ItemArmor getArmor(Metal.ArmorType type, Metal metal)
    {
        return METAL_ARMOR.get(type, metal);
    }

    public static void preInit()
    {
        RegistryHelper r = RegistryHelper.get(MOD_ID);
        Item item;

        r.registerItem(new ItemCore(), "grass_fiber", TAB_ITEMS);
        r.registerItem(new ItemCore(), "grass_string", TAB_ITEMS);
        r.registerItem(new ItemTuyere(), "tuyere", TAB_ITEMS);
        r.registerItem(new ItemClayTool(), "clay_tool", TAB_TOOLS);
        r.registerItem(new ItemSmallVessel(), "ceramic_small_vessel", TAB_ITEMS);
        r.registerItem(new ItemCeramicBucket(), "ceramic_bucket", TAB_ITEMS);

        r.registerItem(item = new ItemCore(), "flint_shard", TAB_ITEMS);
        OreDictionaryHelper.register(item, "shard", "flint");
        r.registerItem(item = new ItemCore(), "gear_wood", TAB_ITEMS);
        OreDictionaryHelper.register(item, "gear", "wood");
        r.registerItem(item = new ItemCore(), "clay_brick", TAB_ITEMS);
        OreDictionaryHelper.register(item, "brick", "clay");

        for (Stone type : Stone.values())
        {
            r.registerItem(new ItemRock(type), "rock/" + type.name(), TAB_ITEMS);
        }

        {
            // Flint Tools
            ImmutableMap.Builder<Metal.ToolType, Item> b = new ImmutableMap.Builder<>();

            for (Metal.ToolType type : Metal.ToolType.values())
            {
                if (type.isFlint)
                {
                    b.put(type, r.registerItem(item = type.createFlint(), type.name() + "/flint", TAB_TOOLS));
                    OreDictionaryHelper.register(item, type.name(), "flint");
                }
            }

            FLINT_TOOLS = b.build();
        }

        for (Metal metal : Metal.values())
        {
            if (!metal.isDefault)
            {
                r.registerItem(item = new ItemCore(), "dust/" + metal.name(), TAB_ITEMS);
                OreDictionaryHelper.register(item, "dust", metal.name());

                r.registerItem(item = new ItemCore(), "nugget/" + metal.name(), TAB_ITEMS);
                OreDictionaryHelper.register(item, "nugget", metal.name());

                r.registerItem(item = new ItemCore(), "ingot/" + metal.name(), TAB_ITEMS);
                OreDictionaryHelper.register(item, "ingot", metal.name());
            }

            {
                // Metal Tools
                ImmutableEnumTable.Builder<Metal.ToolType, Metal, Item> b = new ImmutableEnumTable.Builder<>(Metal.ToolType.class, Metal.class);

                for (Metal.ToolType type : Metal.ToolType.values())
                {
                    if (!(metal.isDefault && type.isDefault) && metal.toolMaterial != null)
                    {
                        b.put(type, metal, r.registerItem(item = type.create(metal.toolMaterial), type.name() + "/" + metal.name(), TAB_TOOLS));
                        OreDictionaryHelper.register(item, type.name(), metal.name());
                    }
                }

                METAL_TOOLS = b.build();
            }

            {
                // Metal Armor
                ImmutableEnumTable.Builder<Metal.ArmorType, Metal, ItemArmor> b = new ImmutableEnumTable.Builder<>(Metal.ArmorType.class, Metal.class);
                ItemArmor armor;

                for (Metal.ArmorType type : Metal.ArmorType.values())
                {
                    if (!metal.isDefault && metal.armorMaterial != null)
                    {
                        b.put(type, metal, r.registerItem(armor = type.create(metal.armorMaterial), type.name() + "/" + metal.name(), TAB_TOOLS));
                        OreDictionaryHelper.register(armor, type.name(), metal.name());
                    }
                }

                METAL_ARMOR = b.build();
            }
        }
    }
}
