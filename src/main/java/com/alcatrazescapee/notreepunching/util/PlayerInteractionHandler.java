/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.util;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.alcatrazescapee.core.util.CoreHelpers;
import com.alcatrazescapee.notreepunching.ModConfig;
import com.alcatrazescapee.notreepunching.client.ModSounds;
import com.alcatrazescapee.notreepunching.common.items.ModItems;

import static com.alcatrazescapee.notreepunching.util.Util.RNG;

@ParametersAreNonnullByDefault
public final class PlayerInteractionHandler
{
    public static boolean hasAction(World world, BlockPos pos, ItemStack stack, @Nullable EnumFacing face)
    {
        IBlockState state = world.getBlockState(pos);
        if (stack.getItem() == Items.FLINT)
        {
            return ModConfig.BALANCE.flintKnappingChance > 0 && state.getMaterial() == Material.ROCK && state.isNormalCube() && face == EnumFacing.UP;
        }
        if (WoodRecipeHandler.isAxe(stack))
        {
            IBlockState stateDown = world.getBlockState(pos.down());
            if (face == EnumFacing.UP && stateDown.isOpaqueCube())
            {
                return (WoodRecipeHandler.isLog(world, pos, state) && !WoodRecipeHandler.isLog(world, pos.down(), stateDown)) || WoodRecipeHandler.isPlank(world, pos, state);
            }
        }
        return false;
    }

    /**
     * Performs the action
     *
     * @return true if the event should be cancelled
     */
    public static boolean performAction(World world, BlockPos pos, EntityPlayer player, ItemStack stack, @Nullable EnumFacing face, EnumHand hand)
    {
        if (stack.getItem() == Items.FLINT)
        {
            return handleFlint(world, pos, player, stack, hand);
        }
        if (WoodRecipeHandler.isAxe(stack))
        {
            return handleChopping(world, pos, player, stack);
        }
        return false;
    }

    private static boolean handleFlint(World world, BlockPos pos, EntityPlayer player, ItemStack stack, EnumHand hand)
    {
        if (!world.isRemote)
        {
            if (RNG.nextFloat() < ModConfig.BALANCE.flintKnappingChance)
            {
                if (RNG.nextFloat() < ModConfig.BALANCE.flintKnappingSuccessChance)
                {
                    CoreHelpers.dropItemInWorldExact(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, new ItemStack(ModItems.FLINT_SHARD, 2));
                }
                player.setHeldItem(hand, CoreHelpers.consumeItem(player, stack));
            }
            world.playSound(null, pos, ModSounds.KNAPPING, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
        return true;
    }

    private static boolean handleChopping(World world, BlockPos pos, EntityPlayer player, ItemStack stack)
    {
        if (!world.isRemote)
        {
            if (RNG.nextFloat() < ModConfig.BALANCE.logChoppingChance)
            {
                int amount = 1 + (RNG.nextFloat() < 0.75 ? 1 : 0);
                if (!WoodRecipeHandler.isWeakAxe(stack))
                {
                    amount++;
                }

                IBlockState state = world.getBlockState(pos);
                ItemStack result = WoodRecipeHandler.getPlankForLog(world, pos, state);
                if (result != null)
                {
                    result.setCount(amount);
                    CoreHelpers.dropItemInWorldExact(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, result);
                    stack.damageItem(1, player);
                    world.setBlockToAir(pos);
                }
                else
                {
                    if (WoodRecipeHandler.isPlank(world, pos, state))
                    {
                        result = new ItemStack(Items.STICK, 1 + (RNG.nextFloat() < 0.25 ? 1 : 0));
                        CoreHelpers.dropItemInWorldExact(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, result);
                        stack.damageItem(1, player);
                        world.setBlockToAir(pos);
                    }
                }
                world.playSound(null, pos, SoundEvents.BLOCK_WOOD_BREAK, SoundCategory.PLAYERS, 1.2f, 1.0f);
            }
            else
            {
                world.playSound(null, pos, SoundEvents.BLOCK_WOOD_BREAK, SoundCategory.PLAYERS, 0.5f, 1.0f);
            }
        }
        return true;
    }
}
