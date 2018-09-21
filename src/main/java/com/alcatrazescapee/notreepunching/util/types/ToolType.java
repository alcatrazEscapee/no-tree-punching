/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.util.types;

import javax.annotation.Nonnull;

import net.minecraft.item.Item;

import com.alcatrazescapee.alcatrazcore.item.tool.ItemAxeCore;
import com.alcatrazescapee.alcatrazcore.item.tool.ItemHoeCore;
import com.alcatrazescapee.alcatrazcore.item.tool.ItemPickCore;
import com.alcatrazescapee.alcatrazcore.item.tool.ItemSpadeCore;
import com.alcatrazescapee.notreepunching.common.items.ItemKnife;
import com.alcatrazescapee.notreepunching.common.items.ItemMattock;
import com.alcatrazescapee.notreepunching.common.items.ItemSaw;

public enum ToolType
{
    PICKAXE(true, false),
    AXE(true, false),
    SHOVEL(true, false),
    HOE(true, false),
    KNIFE(true, true),
    MATTOCK(false, true),
    SAW(false, true);

    public final boolean isFlintTool;
    public final boolean isNewTool;

    ToolType(boolean isFlintTool, boolean isNewTool)
    {
        this.isFlintTool = isFlintTool;
        this.isNewTool = isNewTool;
    }

    @Nonnull
    public Item createFlint(Item.ToolMaterial material)
    {
        switch (this)
        {
            case PICKAXE:
                return new ItemPickCore(material);
            case HOE:
                return new ItemHoeCore(material);
            case AXE:
                return new ItemAxeCore(material);
            case SHOVEL:
                return new ItemSpadeCore(material);
            case KNIFE:
                return new ItemKnife(material);
        }
        throw new IllegalStateException("This type does not support flint tools");
    }

    @Nonnull
    public Item createTool(Item.ToolMaterial material)
    {
        switch (this)
        {
            case KNIFE:
                return new ItemKnife(material);
            case SAW:
                return new ItemSaw(material);
            case MATTOCK:
                return new ItemMattock(material);
        }
        throw new IllegalStateException("This type does not support new tools");
    }
}
