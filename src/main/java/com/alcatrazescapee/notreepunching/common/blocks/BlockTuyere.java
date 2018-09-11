/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.common.blocks;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import com.alcatrazescapee.alcatrazcore.block.BlockCore;
import com.alcatrazescapee.notreepunching.common.items.ModItems;
import com.alcatrazescapee.notreepunching.util.Stone;

@ParametersAreNonnullByDefault
public class BlockTuyere extends BlockCore
{
    private static final Map<Stone, BlockTuyere> MAP = new HashMap<>();

    public static BlockTuyere get(Stone type)
    {
        return MAP.get(type);
    }

    private final Stone type;

    public BlockTuyere(Stone type)
    {
        super(Material.ROCK);

        this.type = type;
        MAP.put(type, this);

        setHardness(3.0f);
        setHarvestLevel("pickaxe", 0);
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        drops.clear();
        drops.add(new ItemStack(ModItems.TUYERE));
        drops.add(new ItemStack(BlockCobble.get(type)));
    }
}
