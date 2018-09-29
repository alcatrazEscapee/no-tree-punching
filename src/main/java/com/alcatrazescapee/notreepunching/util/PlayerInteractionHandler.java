/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.util;

import java.util.Random;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.alcatrazescapee.alcatrazcore.util.CoreHelpers;
import com.alcatrazescapee.notreepunching.ModConfig;
import com.alcatrazescapee.notreepunching.client.ModSounds;
import com.alcatrazescapee.notreepunching.common.items.ModItems;

@ParametersAreNonnullByDefault
public final class PlayerInteractionHandler
{
    private static final Random rand = new Random();

    public static boolean hasAction(World world, BlockPos pos, ItemStack stack, @Nullable EnumFacing face)
    {
        IBlockState state = world.getBlockState(pos);
        if (stack.getItem() == Items.FLINT)
        {
            return state.getMaterial() == Material.ROCK && state.isNormalCube() && face == EnumFacing.UP;
        }
        if (CoreHelpers.doesStackMatchOre(stack, "toolWeakAxe") || CoreHelpers.doesStackMatchOre(stack, "toolAxe") || stack.getItem() instanceof ItemAxe)
        {
            IBlockState stateDown = world.getBlockState(pos.down());
            return WoodRecipeHandler.isLog(world, pos, state) && !WoodRecipeHandler.isLog(world, pos.down(), stateDown) &&
                    stateDown.isOpaqueCube() && face == EnumFacing.UP;
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
        if (CoreHelpers.doesStackMatchOre(stack, "toolWeakAxe") || CoreHelpers.doesStackMatchOre(stack, "toolAxe") || stack.getItem() instanceof ItemAxe)
        {
            return handleChopping(world, pos, player, stack);
        }
        return false;
    }

    private static boolean handleFlint(World world, BlockPos pos, EntityPlayer player, ItemStack stack, EnumHand hand)
    {
        if (!world.isRemote)
        {
            if (rand.nextFloat() < ModConfig.BALANCE.flintKnappingChance)
            {
                if (rand.nextFloat() < ModConfig.BALANCE.flintKnappingSuccessChance)
                    CoreHelpers.dropItemInWorldExact(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, new ItemStack(ModItems.FLINT_SHARD));

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
            if (rand.nextFloat() < ModConfig.BALANCE.logChoppingChance)
            {
                int amount = 1;
                if (CoreHelpers.doesStackMatchOre(stack, "toolWeakAxe"))
                {
                    amount += (rand.nextFloat() < 0.75 ? 1 : 0);
                }
                else
                {
                    amount += 1 + (rand.nextFloat() < 0.75 ? 1 : 0);
                }


                ItemStack result = WoodRecipeHandler.getPlankForLog(world, pos, world.getBlockState(pos));
                if (result != null)
                {
                    result.setCount(amount);
                    CoreHelpers.dropItemInWorldExact(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, result);
                    stack.damageItem(1, player);
                    world.setBlockToAir(pos);
                }
                world.playSound(null, pos, SoundEvents.BLOCK_WOOD_BREAK, SoundCategory.PLAYERS, 0.6f, 1.0f);
            }
            else
            {
                world.playSound(null, pos, SoundEvents.BLOCK_WOOD_BREAK, SoundCategory.BLOCKS, 1.0f, 1.0f);
            }
        }
        return true;
    }
}
