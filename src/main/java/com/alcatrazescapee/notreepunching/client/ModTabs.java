/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.client;

import javax.annotation.Nonnull;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.alcatrazescapee.notreepunching.common.items.ItemRock;
import com.alcatrazescapee.notreepunching.common.items.ModItems;
import com.alcatrazescapee.notreepunching.util.types.Stone;
import com.alcatrazescapee.notreepunching.util.types.ToolType;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

public class ModTabs
{
    public static final CreativeTabs TAB_ITEMS = new CreativeTabs(MOD_ID + ".items")
    {
        @SideOnly(Side.CLIENT)
        @Override
        @Nonnull
        public ItemStack createIcon()
        {
            return new ItemStack(ItemRock.get(Stone.STONE));
        }
    };

    public static final CreativeTabs TAB_TOOLS = new CreativeTabs(MOD_ID + ".tools")
    {
        @SideOnly(Side.CLIENT)
        @Override
        @Nonnull
        public ItemStack createIcon()
        {
            return new ItemStack(ModItems.getFlintTool(ToolType.PICKAXE));
        }
    };
}
