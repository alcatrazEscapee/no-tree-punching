/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.items;


import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.TieredItem;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import com.alcatrazescapee.notreepunching.common.ModItemGroup;
import com.alcatrazescapee.notreepunching.common.blocks.ModBlocks;
import com.alcatrazescapee.notreepunching.common.blocks.PotteryBlock;
import com.alcatrazescapee.notreepunching.util.Helpers;

public class ClayToolItem extends TieredItem
{
    public ClayToolItem()
    {
        super(ItemTier.WOOD, new Properties().tab(ModItemGroup.ITEMS).setNoRepair());
    }

    @Override
    public ActionResultType useOn(ItemUseContext context)
    {
        IWorld world = context.getLevel();
        if (!world.isClientSide())
        {
            BlockPos pos = context.getClickedPos();
            BlockState state = world.getBlockState(pos);
            PlayerEntity player = context.getPlayer();
            ItemStack stack = context.getItemInHand();

            if (state.getBlock() == Blocks.CLAY)
            {
                world.setBlock(pos, ModBlocks.POTTERY.get(PotteryBlock.Variant.WORKED).get().defaultBlockState(), 3);
                world.playSound(null, pos, SoundEvents.GRAVEL_PLACE, SoundCategory.BLOCKS, 0.5F, 1.0F);
                Helpers.hurtAndBreak(player, context.getHand(), stack, 1);
            }
            else if (state.getBlock() instanceof PotteryBlock)
            {
                if (random.nextDouble() < 0.4)
                {
                    PotteryBlock.Variant next = ((PotteryBlock) state.getBlock()).getVariant().next();

                    if (next == null)
                    {
                        world.destroyBlock(pos, false);
                    }
                    else
                    {
                        world.setBlock(pos, ModBlocks.POTTERY.get(next).get().defaultBlockState(), 3);
                    }
                }
                world.playSound(null, pos, SoundEvents.GRAVEL_PLACE, SoundCategory.BLOCKS, 0.5F, 1.0F);
                Helpers.hurtAndBreak(player, context.getHand(), stack, 1);
            }
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