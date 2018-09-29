/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.common.items;

import com.google.common.collect.ImmutableMap;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import com.alcatrazescapee.alcatrazcore.item.ItemCore;
import com.alcatrazescapee.alcatrazcore.util.OreDictionaryHelper;
import com.alcatrazescapee.alcatrazcore.util.RegistryHelper;
import com.alcatrazescapee.alcatrazcore.util.collections.ImmutableEnumTable;
import com.alcatrazescapee.notreepunching.ModConfig;
import com.alcatrazescapee.notreepunching.util.types.Metal;
import com.alcatrazescapee.notreepunching.util.types.Stone;
import com.alcatrazescapee.notreepunching.util.types.ToolType;

import static com.alcatrazescapee.alcatrazcore.util.CoreHelpers.getNull;
import static com.alcatrazescapee.notreepunching.ModConstants.MOD_ID;
import static com.alcatrazescapee.notreepunching.client.ModTabs.TAB_ITEMS;
import static com.alcatrazescapee.notreepunching.client.ModTabs.TAB_TOOLS;
import static com.alcatrazescapee.notreepunching.common.ModMaterials.TOOL_FLINT;

@GameRegistry.ObjectHolder(value = MOD_ID)
public final class ModItems
{
    public static final Item FLINT_SHARD = getNull();
    public static final Item GRASS_FIBER = getNull();
    public static final Item GRASS_STRING = getNull();
    public static final Item CLAY_BRICK = getNull();

    public static final Item CERAMIC_SMALL_VESSEL = getNull();
    public static final Item CERAMIC_BUCKET = getNull();

    private static ImmutableMap<ToolType, Item> FLINT_TOOLS;
    private static ImmutableEnumTable<ToolType, Metal, Item> METAL_TOOLS;

    public static Item getFlintTool(ToolType type)
    {
        return FLINT_TOOLS.get(type);
    }

    public static Item getTool(ToolType type, Metal metal)
    {
        return METAL_TOOLS.get(type, metal);
    }

    public static void preInit()
    {
        RegistryHelper r = RegistryHelper.get(MOD_ID);
        Item item;

        r.registerItem(new ItemCore(), "grass_fiber", TAB_ITEMS);
        r.registerItem(new ItemClayTool(), "clay_tool", TAB_TOOLS);
        r.registerItem(new ItemFireStarter(), "fire_starter", TAB_TOOLS);
        r.registerItem(new ItemSmallVessel(), "ceramic_small_vessel", TAB_ITEMS);
        r.registerItem(new ItemCeramicBucket(), "ceramic_bucket", TAB_ITEMS);

        r.registerItem(item = new ItemCore(), "grass_string", TAB_ITEMS);
        OreDictionaryHelper.register(item, "kindling");
        OreDictionaryHelper.register(item, "string");
        r.registerItem(item = new ItemCore(), "flint_shard", TAB_ITEMS);
        OreDictionaryHelper.register(item, "shard", "flint");
        r.registerItem(item = new ItemCore(), "clay_brick", TAB_ITEMS);
        OreDictionaryHelper.register(item, "brick", "clay");

        for (Stone type : Stone.values())
        {
            r.registerItem(new ItemRock(type), "rock/" + type.name(), TAB_ITEMS);
        }

        {
            // Tools
            ImmutableMap.Builder<ToolType, Item> flintTools = new ImmutableMap.Builder<>();
            ImmutableEnumTable.Builder<ToolType, Metal, Item> metalTools = new ImmutableEnumTable.Builder<>(ToolType.class, Metal.class);

            for (ToolType type : ToolType.values())
            {
                if (type.isFlintTool)
                {
                    flintTools.put(type, r.registerItem(item = type.createFlint(TOOL_FLINT), type.name() + "/flint", TAB_TOOLS));
                    OreDictionaryHelper.register(item, "tool", "weak", type.name());
                }

                if (type.isNewTool)
                {
                    for (Metal metal : Metal.values())
                    {
                        // Metal Tools
                        if (metal.isEnabled)
                        {
                            metalTools.put(type, metal, r.registerItem(item = type.createTool(metal.toolMaterial), type.name() + "/" + metal.name(), TAB_TOOLS));
                            OreDictionaryHelper.register(item, "tool", type.name());
                        }
                    }
                }
            }

            FLINT_TOOLS = flintTools.build();
            METAL_TOOLS = metalTools.build();
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static void init()
    {
        // Vanilla ore dictionary additions
        OreDictionary.registerOre("kindling", Items.PAPER);
        OreDictionary.registerOre("kindling", Blocks.HAY_BLOCK);

        // Remove wood + stone tools from creative tabs
        if (ModConfig.GENERAL.replaceVanillaRecipes)
        {
            Items.WOODEN_AXE.setCreativeTab(null);
            Items.WOODEN_HOE.setCreativeTab(null);
            Items.WOODEN_PICKAXE.setCreativeTab(null);
            Items.WOODEN_SHOVEL.setCreativeTab(null);
            Items.WOODEN_SWORD.setCreativeTab(null);

            Items.STONE_AXE.setCreativeTab(null);
            Items.STONE_HOE.setCreativeTab(null);
            Items.STONE_PICKAXE.setCreativeTab(null);
            Items.STONE_SHOVEL.setCreativeTab(null);
            Items.STONE_SWORD.setCreativeTab(null);
        }
    }
}
