/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.items;


import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.TieredItem;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import com.alcatrazescapee.notreepunching.Config;
import com.alcatrazescapee.notreepunching.common.ModItemGroup;
import com.alcatrazescapee.notreepunching.util.Helpers;

public class ClayToolItem extends TieredItem
{
    public static ItemStack interactWithBlock(IWorld world, BlockPos pos, BlockState state, @Nullable PlayerEntity player, @Nullable Hand hand, ItemStack stack)
    {
        final List<Block> sequence = Config.SERVER.getPotteryBlockSequences();
        for (int i = 0; i < sequence.size() - 1; i++)
        {
            if (state.getBlock() == sequence.get(i) && state.getBlock() != Blocks.AIR)
            {
                final Block replacement = sequence.get(i + 1);
                world.setBlock(pos, replacement.defaultBlockState(), 3);
                world.playSound(null, pos, SoundEvents.GRAVEL_PLACE, SoundCategory.BLOCKS, 0.5F, 1.0F);
                stack = Helpers.hurtAndBreak(player, hand, stack, 1);
                return stack;
            }
        }
        return stack;
    }

    public ClayToolItem()
    {
        super(ItemTier.WOOD, new Properties().tab(ModItemGroup.ITEMS).setNoRepair());
    }

    @Override
    public ActionResultType useOn(ItemUseContext context)
    {
        final IWorld world = context.getLevel();
        if (!world.isClientSide())
        {
            final BlockPos pos = context.getClickedPos();
            interactWithBlock(world, pos, world.getBlockState(pos), context.getPlayer(), context.getHand(), context.getItemInHand());
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack)
    {
        return Helpers.hurtAndBreak(itemStack.copy(), 1);
    }

    @Override
    public boolean hasContainerItem(ItemStack stack)
    {
        return true;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
    {
        return enchantment.category == EnchantmentType.BREAKABLE;
    }
}