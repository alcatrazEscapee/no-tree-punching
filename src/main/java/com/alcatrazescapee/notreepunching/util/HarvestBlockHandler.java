/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.util;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

import com.alcatrazescapee.alcatrazcore.util.CoreHelpers;
import com.alcatrazescapee.notreepunching.ModConfig;
import com.alcatrazescapee.notreepunching.ModConstants;
import com.alcatrazescapee.notreepunching.common.blocks.BlockRock;
import com.alcatrazescapee.notreepunching.common.items.ItemKnife;
import com.alcatrazescapee.notreepunching.common.items.ItemRock;
import com.alcatrazescapee.notreepunching.common.items.ModItems;
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
            float stickChance = (float) ModConfig.GENERAL.leavesStickDropChance;
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
                if (ModConstants.RNG.nextFloat() < ModConfig.GENERAL.tallGrassDropPlantFiberChance)
                {
                    drops.add(new ItemStack(ModItems.GRASS_FIBER));
                }
                stack.damageItem(1, player);
            }
        }
    }

    public static boolean isValidTool(ItemStack stack, EntityPlayer player, IBlockState state)
    {
        if (isWhitelisted(state))
        {
            return true;
        }

        // Get variables for the required and current harvest levels + tools
        int neededHarvestLevel = state.getBlock().getHarvestLevel(state);
        String neededToolClass = state.getBlock().getHarvestTool(state);

        if (neededToolClass != null && !stack.isEmpty())
        {
            for (String toolClass : stack.getItem().getToolClasses(stack))
            {
                if (neededToolClass.equals(toolClass) && stack.getItem().getHarvestLevel(stack, toolClass, player, state) >= neededHarvestLevel)
                {
                    return true;
                }
                else if (neededToolClass.equals("shovel") && toolClass.equals("pickaxe") && stack.getItem().getHarvestLevel(stack, toolClass, player, state) >= 1)
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
