/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.common.blocks;

import javax.annotation.Nullable;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;

import com.alcatrazescapee.alcatrazcore.block.BlockCore;
import com.alcatrazescapee.alcatrazcore.util.OreDictionaryHelper;
import com.alcatrazescapee.notreepunching.util.types.Stone;

public class BlockCobble extends BlockCore
{
    private static final BiMap<Stone, Block> MAP;

    static
    {
        MAP = HashBiMap.create();
        MAP.put(Stone.STONE, Blocks.COBBLESTONE);
    }

    public static Block get(Stone type)
    {
        return MAP.get(type);
    }

    @Nullable
    public static Stone get(Block block)
    {
        return MAP.inverse().get(block);
    }

    private final Stone type;

    public BlockCobble(Stone type)
    {
        super(Material.ROCK);

        this.type = type;
        MAP.put(type, this);

        setHardness(2.5f);
        setHarvestLevel("pickaxe", 0);
        OreDictionaryHelper.register(this, "cobblestone");
        OreDictionaryHelper.register(this, "cobblestone", type.name());
    }
}
