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
import net.minecraftforge.fml.common.Loader;

import com.alcatrazescapee.notreepunching.ModConfig;

public enum Stone
{
    STONE,
    ANDESITE,
    GRANITE,
    DIORITE,
    SANDSTONE,
    MARBLE,
    LIMESTONE,
    SLATE;

    @Nonnull
    public static Stone getFromBlock(IBlockState state, Random random)
    {
        Stone stone = getFromBlock(state);
        if (stone == STONE)
        {
            // Chance to spice it up if normal stone is drawn
            switch (random.nextInt(5))
            {
                case 0:
                    return GRANITE;
                case 1:
                    return ANDESITE;
                case 2:
                    return DIORITE;
            }
        }
        return stone == null ? STONE : stone;
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
        //noinspection ConstantConditions
        String registryName = state.getBlock().getRegistryName().toString();
        switch (registryName)
        {
            case "quark:limestone":
                return LIMESTONE;
            case "quark:marble":
                return MARBLE;
            case "rustic:slate":
                return SLATE;
            default:
                return null;
        }
    }

    public boolean isEnabled()
    {
        switch (this)
        {
            case STONE:
            case ANDESITE:
            case DIORITE:
            case GRANITE:
            case SANDSTONE:
                return true;
            case MARBLE:
                return LIMESTONE.isEnabled() || SLATE.isEnabled();
            case LIMESTONE:
                return Loader.isModLoaded("quark") && ModConfig.COMPAT.enableQuarkCompat;
            case SLATE:
                return Loader.isModLoaded("rustic") && ModConfig.COMPAT.enableRusticCompat;
            default:
                return false;
        }
    }

    public boolean hasCobblestone()
    {
        switch (this)
        {
            case DIORITE:
            case GRANITE:
            case ANDESITE:
            case SLATE:
            case MARBLE:
            case LIMESTONE:
                return true;
            default:
                return false;
        }
    }
}
