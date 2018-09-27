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
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public enum Stone
{
    STONE(true),
    ANDESITE(true),
    GRANITE(true),
    DIORITE(true);

    private static Stone[] values = values();

    @Nonnull
    public static Stone getRandom(Random random)
    {
        return values[random.nextInt(values.length)];
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
        return null;
    }

    public final boolean isEnabled;

    Stone(boolean isEnabled)
    {
        this.isEnabled = isEnabled;
    }
}
