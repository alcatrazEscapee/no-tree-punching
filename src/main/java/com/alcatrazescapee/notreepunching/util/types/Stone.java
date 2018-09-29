/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.util.types;

import java.util.Random;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.BlockStone;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public enum Stone
{
    STONE,
    ANDESITE,
    GRANITE,
    DIORITE,
    SANDSTONE;

    @Nonnull
    public static Stone getFromMaterial(Material material, Random random)
    {
        switch (random.nextInt(8))
        {
            case 0:
                return ANDESITE;
            case 1:
                return DIORITE;
            case 2:
                return GRANITE;
            default:
                return material == Material.SAND ? SANDSTONE : STONE;
        }
    }

    @Nullable
    public static Stone getFromBlock(IBlockState state)
    {
        if (state.getBlock() == Blocks.STONE)
        {
            switch (state.getValue(BlockStone.VARIANT))
            {
                case STONE:
                    return STONE;
                case DIORITE:
                    return DIORITE;
                case GRANITE:
                    return GRANITE;
                case ANDESITE:
                    return ANDESITE;
            }
        }
        if (state.getBlock() == Blocks.SANDSTONE)
        {
            return SANDSTONE;
        }
        return null;
    }
}
