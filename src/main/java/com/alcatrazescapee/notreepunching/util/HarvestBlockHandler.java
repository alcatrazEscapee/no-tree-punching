/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.util;

import java.util.*;
import java.util.function.Predicate;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

import com.alcatrazescapee.alcatrazcore.util.CoreHelpers;
import com.alcatrazescapee.notreepunching.ModConfig;
import com.alcatrazescapee.notreepunching.ModConstants;
import com.alcatrazescapee.notreepunching.NoTreePunching;
import com.alcatrazescapee.notreepunching.common.items.ItemKnife;
import com.alcatrazescapee.notreepunching.common.items.ItemRock;
import com.alcatrazescapee.notreepunching.common.items.ModItems;
import com.alcatrazescapee.notreepunching.util.types.Stone;
import mcp.MethodsReturnNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public final class HarvestBlockHandler
{
    private static final Set<Predicate<IBlockState>> exceptions = new HashSet<>();
    private static final List<ItemStack> grassDrops = new ArrayList<>();

    public static void postInit()
    {
        reloadWhitelist();

        addGrassDrop(new ItemStack(ModItems.GRASS_FIBER));
        addGrassDrop(new ItemStack(ModItems.GRASS_FIBER, 2));
    }

    public static void reloadWhitelist()
    {
        exceptions.clear();

        // Add exceptions from config file
        for (String s : ModConfig.GENERAL.alwaysBreakable)
        {
            try
            {
                exceptions.add(createPredicate(s));
            }
            catch (IllegalArgumentException e)
            {
                NoTreePunching.getLog().warn("Problem loading entry in alwaysBreakable: {}, {}", s, e);
            }
        }

        // Cleanup
        exceptions.removeIf(Objects::isNull);
    }

    public static void addExtraDrops(List<ItemStack> drops, IBlockState state, EntityPlayer player, ItemStack stack)
    {
        // Stone -> Loose Rocks instead of cobblestone
        Stone stone = Stone.getFromBlock(state);
        if (stone != null)
        {
            drops.clear();
            drops.add(new ItemStack(ItemRock.get(stone), 2 + ModConstants.RNG.nextInt(3)));
        }

        // Sticks from leaves
        if (state.getBlock() instanceof BlockLeaves)
        {
            float stickChance = (float) ModConfig.BALANCE.leavesStickDropChance;
            if (CoreHelpers.doesStackMatchOre(stack, "toolKnife"))
            {
                stickChance += 0.3f;
            }
            if (ModConstants.RNG.nextFloat() < stickChance)
            {
                drops.add(new ItemStack(Items.STICK, 1 + ModConstants.RNG.nextInt(1)));
            }
        }

        // Plant fiber from grass
        if (state.getBlock() instanceof BlockDoublePlant || state.getBlock() instanceof BlockTallGrass)
        {
            if (stack.getItem() instanceof ItemKnife)
            {
                if (ModConstants.RNG.nextFloat() < ModConfig.BALANCE.tallGrassDropPlantFiberChance)
                {
                    ItemStack drop = grassDrops.get(ModConstants.RNG.nextInt(grassDrops.size()));
                    drops.add(drop.copy());
                }
                stack.damageItem(1, player);
                player.setHeldItem(EnumHand.MAIN_HAND, stack); // Necessary because the stack came from IPlayerItem
            }
        }
    }

    public static boolean isInvalidTool(ItemStack stack, EntityPlayer player, IBlockState state)
    {
        // Always allow whitelisted IBlockStates
        if (isWhitelisted(state))
        {
            return false;
        }

        int neededHarvestLevel = state.getBlock().getHarvestLevel(state);
        String neededToolClass = state.getBlock().getHarvestTool(state);

        // Blocks must require a valid tool class to have an invalid one
        if (neededToolClass != null)
        {
            // Breaking with your fists has no tool classes
            if (stack.isEmpty())
            {
                return true;
            }
            // Compare tool classes between each IBlockState
            for (String toolClass : stack.getItem().getToolClasses(stack))
            {
                if (neededToolClass.equals(toolClass) && stack.getItem().getHarvestLevel(stack, toolClass, player, state) >= neededHarvestLevel)
                {
                    return false;
                }
                // Metal pickaxes can break shovel blocks
                else if (neededToolClass.equals("shovel") && toolClass.equals("pickaxe") && stack.getItem().getHarvestLevel(stack, toolClass, player, state) >= 1)
                {
                    return false;
                }
                // Tinkers mattock + NTP mattock
                else if (toolClass.equals("mattock") && (neededToolClass.equals("axe") || neededToolClass.equals("shovel")))
                {
                    return false;
                }
            }
            return true;
        }
        // The case that there is no needed tool class, always return false
        return false;
    }

    public static float getSpeedModifier(String toolClass)
    {
        switch (toolClass)
        {
            case "axe":
            case "mattock":
                return 0.2f; // = 1 / 5
            case "pickaxe":
                return 0.125f; // = 1 / 8
            case "shovel":
            default:
                return 0.33f; // = 1 / 3
        }
    }

    public static boolean isWhitelisted(IBlockState state)
    {
        return exceptions.stream().anyMatch(x -> x.test(state));
    }

    public static void addGrassDrop(ItemStack stack)
    {
        grassDrops.add(stack);
    }

    private static Predicate<IBlockState> createPredicate(String entry) throws IllegalArgumentException
    {
        // Specific checks for keywords:
        if (entry.substring(0, 9).equals("material:"))
        {
            try
            {
                Object obj = Material.class.getField(entry.substring(9).toUpperCase()).get(null);
                if (obj instanceof Material)
                {
                    Material material = (Material) obj;
                    return state -> (state.getMaterial() == material);
                }
            }
            catch (Exception e)
            {
                throw new IllegalArgumentException("Material is incorrect somehow", e);
            }
        }
        // Default, fall back to block state based predicate
        int colon = entry.indexOf(':');
        int colon2 = entry.lastIndexOf(':');
        if (colon == colon2)
        {
            // Construct a generic stack
            Block block = Block.getBlockFromName(entry);
            if (block == null) throw new IllegalArgumentException("Block is null, no metadata");
            return state -> (state.getBlock() == block);
        }
        else
        {
            // Construct a stack with metadata
            Block block = Block.getBlockFromName(entry.substring(0, colon2));
            if (block == null) throw new IllegalArgumentException("Block is null, with metadata");
            int meta = Integer.valueOf(entry.substring(colon2 + 1));
            return state -> (state.getBlock() == block && state.getBlock().getMetaFromState(state) == meta);
        }
    }
}
