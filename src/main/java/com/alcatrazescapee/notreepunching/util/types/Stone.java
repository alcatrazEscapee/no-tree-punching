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
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

import com.alcatrazescapee.alcatrazcore.util.CoreHelpers;
import com.alcatrazescapee.notreepunching.ModConfig;

public enum Stone
{
    STONE(true, false), // Vanilla
    ANDESITE(true, true),
    GRANITE(true, true),
    DIORITE(true, true),
    SANDSTONE(true, false),
    MARBLE(false, true), // Quark
    LIMESTONE(false, true),
    SLATE(false, true); // Rustic

    @Nonnull
    public static Stone getFromBlock(IBlockState state, Random random)
    {
        Stone stone = getFromBlock(state);
        if (stone == STONE)
        {
            // Chance to spice it up if normal stone is drawn (used in world gen)
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

    private final boolean isDefault;
    private final boolean hasCobblestone;

    Stone(boolean isDefault, boolean hasCobblestone)
    {
        this.isDefault = isDefault;
        this.hasCobblestone = hasCobblestone;
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

    @Nonnull
    public ItemStack getStoneStack()
    {
        switch (this)
        {
            case STONE:
                return new ItemStack(Blocks.STONE);
            case SANDSTONE:
                return new ItemStack(Blocks.SANDSTONE);
            case DIORITE:
                return new ItemStack(Blocks.STONE, 1, BlockStone.EnumType.DIORITE.getMetadata());
            case GRANITE:
                return new ItemStack(Blocks.STONE, 1, BlockStone.EnumType.GRANITE.getMetadata());
            case ANDESITE:
                return new ItemStack(Blocks.STONE, 1, BlockStone.EnumType.ANDESITE.getMetadata());
            case SLATE:
                return CoreHelpers.getStackByRegistryName("rustic:slate", 1, 0);
            case MARBLE:
                return CoreHelpers.getStackByRegistryName("quark:marble", 1, 0);
            case LIMESTONE:
                return CoreHelpers.getStackByRegistryName("quark:limestone", 1, 0);
            default:
                return ItemStack.EMPTY;
        }
    }

    public boolean isDefault()
    {
        return isDefault;
    }

    public boolean hasCobblestone()
    {
        return hasCobblestone;
    }

    public boolean isEnabled()
    {
        if (isDefault())
            return true;
        switch (this)
        {
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
}
