/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.common.blocks;

import net.minecraft.block.material.Material;

import com.alcatrazescapee.alcatrazcore.block.BlockCore;
import com.alcatrazescapee.alcatrazcore.util.RegistryHelper;
import com.alcatrazescapee.notreepunching.util.Metal;
import com.alcatrazescapee.notreepunching.util.Stone;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

public class ModBlocks
{
    public static void preInit()
    {
        RegistryHelper r = RegistryHelper.get(MOD_ID);

        for (Stone type : Stone.values())
        {
            r.registerBlock(new BlockRock(type), null, "loose_rock/" + type.name());
        }

        for (Metal type : Metal.values())
        {
            r.registerBlock(new BlockCore(Material.IRON), "metal_block/" + type.name());
        }
    }
}
