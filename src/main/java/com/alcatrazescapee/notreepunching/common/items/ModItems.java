/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.common.items;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.alcatrazescapee.alcatrazcore.item.ItemCore;
import com.alcatrazescapee.alcatrazcore.util.RegistryHelper;
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

    private static final Map<Metal.ToolType, Item> FLINT_TOOLS = new HashMap<>();
    private static final Map<Metal.ToolType, Map<Metal, Item>> METAL_TOOLS = new HashMap<>();
    private static final Map<Metal.ArmorType, Map<Metal, ItemArmor>> METAL_ARMOR = new HashMap<>();

    public static Item getFlintTool(Metal.ToolType type)
    {
        return FLINT_TOOLS.get(type);
    }

    public static Item getTool(Metal.ToolType type, Metal metal)
    {
        return METAL_TOOLS.get(type).get(metal);
    }

    public static ItemArmor getArmor(Metal.ArmorType type, Metal metal)
    {
        return METAL_ARMOR.get(type).get(metal);
    }

    public static void preInit()
    {
        RegistryHelper r = RegistryHelper.get(MOD_ID);

        r.registerItem(new ItemCore(), "grass_fiber", TAB_ITEMS);
        r.registerItem(new ItemCore(), "grass_string", TAB_ITEMS);
        r.registerItem(new ItemCore(), "flint_shard", TAB_ITEMS);
        r.registerItem(new ItemCore(), "gear_wood", TAB_ITEMS);
        r.registerItem(new ItemCore(), "clay_brick", TAB_ITEMS);

        for (Stone type : Stone.values())
        {
            r.registerItem(new ItemRock(type), "rock/" + type.name(), TAB_ITEMS);
        }

        // Tools
        for (Metal.ToolType type : Metal.ToolType.values())
        {
            Map<Metal, Item> map = new HashMap<>();

            for (Metal metal : Metal.values())
            {
                if (!(metal.isDefault && type.isDefault) && metal.toolMaterial != null)
                    map.put(metal, r.registerItem(type.create(metal.toolMaterial), type.name() + "/" + metal.name(), TAB_TOOLS));
            }

            METAL_TOOLS.put(type, map);
            if (type.isFlint)
                FLINT_TOOLS.put(type, r.registerItem(type.createFlint(), type.name() + "/flint"));
        }

        // Armor
        for (Metal.ArmorType type : Metal.ArmorType.values())
        {
            Map<Metal, ItemArmor> map = new HashMap<>();

            for (Metal metal : Metal.values())
            {
                if (!metal.isDefault && metal.armorMaterial != null)
                    map.put(metal, r.registerItem(type.create(metal.armorMaterial), type.name() + "/" + metal.name(), TAB_TOOLS));
            }

            METAL_ARMOR.put(type, map);
        }

        for (Metal metal : Metal.values())
        {
            if (!metal.isDefault)
            {
                r.registerItem(new ItemCore(), "dust/" + metal.name(), TAB_ITEMS);
                r.registerItem(new ItemCore(), "nugget/" + metal.name(), TAB_ITEMS);
                r.registerItem(new ItemCore(), "ingot/" + metal.name(), TAB_ITEMS);
            }
        }
    }
}
