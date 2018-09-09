/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.common.items;

import com.alcatrazescapee.alcatrazcore.item.tool.ItemToolCore;

public class ItemKnife extends ItemToolCore
{
    public ItemKnife(ToolMaterial material)
    {
        super(material, material.getAttackDamage() + 1.0f, -2.4f);
    }
}
