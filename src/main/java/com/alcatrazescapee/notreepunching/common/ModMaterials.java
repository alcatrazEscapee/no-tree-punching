/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.common;

import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;

import com.alcatrazescapee.notreepunching.ModConfig;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

public class ModMaterials
{
    public static final Item.ToolMaterial TOOL_FLINT = EnumHelper.addToolMaterial(MOD_ID + ":flint", ModConfig.TOOLS.miningLevelFlint, 45, 2.5f, 0.5f, 0);
    public static final Item.ToolMaterial TOOL_TIN = EnumHelper.addToolMaterial(MOD_ID + ":tin", ModConfig.TOOLS.miningLevelTin, 120, 3f, 1.0f, 2);
    public static final Item.ToolMaterial TOOL_COPPER = EnumHelper.addToolMaterial(MOD_ID + ":copper", ModConfig.TOOLS.miningLevelCopper, 220, 4f, 1.5f, 4);
    public static final Item.ToolMaterial TOOL_BRONZE = EnumHelper.addToolMaterial(MOD_ID + ":bronze", ModConfig.TOOLS.miningLevelBronze, 560, 8f, 2.0f, 8);
    public static final Item.ToolMaterial TOOL_STEEL = EnumHelper.addToolMaterial(MOD_ID + ":steel", ModConfig.TOOLS.miningLevelSteel, 1800, 11f, 2.5f, 10);
}
