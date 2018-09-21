/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.util;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

import com.alcatrazescapee.alcatrazcore.util.CoreHelpers;
import com.alcatrazescapee.notreepunching.ModConfig;
import com.alcatrazescapee.notreepunching.common.blocks.BlockRock;
import com.alcatrazescapee.notreepunching.util.types.Stone;
import mcp.MethodsReturnNonnullByDefault;

import static net.minecraftforge.fml.common.FMLLog.log;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class HarvestBlockHandler
{
    public static final Set<ItemStack> exceptions = new HashSet<>();
    public static final Set<ItemStack> registeredExceptions = new HashSet<>();

    public static void postInit()
    {
        // Add registered exceptions
        for (Stone type : Stone.values())
        {
            Block block = BlockRock.get(type);
            if (block != null)
                registeredExceptions.add(new ItemStack(block));
        }

        // reload exceptions
        reloadWhitelist();
    }

    public static void reloadWhitelist()
    {
        exceptions.clear();
        // Add exceptions from config file
        for (String s : ModConfig.GENERAL.alwaysBreakable)
            exceptions.add(toStack(s));

        // Add registered exceptions (not over-writable)
        for (ItemStack s : registeredExceptions)
            exceptions.add(s.copy());

        // Cleanup
        exceptions.removeIf(Objects::isNull);
    }

    public static boolean isValidTool(ItemStack stack, IBlockState state)
    {
        if (isWhitelisted(state))
        {
            return true;
        }

        // Get variables for the required and current harvest levels + tools
        int neededHarvestLevel = state.getBlock().getHarvestLevel(state);
        String neededToolClass = state.getBlock().getHarvestTool(state);

        // heldItemStack != ItemStack.EMPTY  && neededHarvestLevel >= 0
        if (neededToolClass != null)
        {
            for (String toolClass : stack.getItem().getToolClasses(stack))
            {
                if (neededToolClass.equals(toolClass) && stack.getItem().getHarvestLevel(stack, toolClass, null, null) >= neededHarvestLevel)
                {
                    return true;
                }
                else if (neededToolClass.equals("shovel") && toolClass.equals("pickaxe") && stack.getItem().getHarvestLevel(stack, toolClass, null, null) >= 1)
                {
                    return true;
                }
                else if (toolClass.equals("mattock") && (neededToolClass.equals("axe") || neededToolClass.equals("shovel")))
                {
                    return true;
                }
            }
        }
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
        int meta = state.getBlock().getMetaFromState(state);
        ItemStack stack = new ItemStack(state.getBlock(), 1, meta);
        return exceptions.stream().anyMatch(x -> CoreHelpers.doStacksMatch(x, stack));
    }

    @Nullable
    private static ItemStack toStack(String s)
    {
        try
        {
            int colon = s.indexOf(':');
            int colon2 = s.lastIndexOf(':');
            if (colon == colon2)
            {
                // Construct a generic stack
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(s.substring(0, colon), s.substring(colon + 1)));
                if (item == null) throw new Exception("Item is null, no metadata");
                return new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE);
            }
            else
            {
                // Construct a stack with metadata
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(s.substring(0, colon), s.substring(colon, colon2)));
                if (item == null) throw new Exception("Item is null, with metadata");
                return new ItemStack(item, 1, Integer.valueOf(s.substring(colon2 + 1)));
            }
        }
        catch (Exception e)
        {
            log.warn("Illegal Entry in Prospectus config: {}, {}", s, e);
            return null;
        }
    }
}
