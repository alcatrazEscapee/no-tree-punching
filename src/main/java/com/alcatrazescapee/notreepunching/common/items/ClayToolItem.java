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
        super(ItemTier.WOOD, new Properties().group(ModItemGroup.ITEMS).setNoRepair());
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context)
    {
        IWorld world = context.getWorld();
        if (!world.isRemote())
        {
            BlockPos pos = context.getPos();
            BlockState state = world.getBlockState(pos);
            PlayerEntity player = context.getPlayer();
            ItemStack stack = context.getItem();

            if (state.getBlock() == Blocks.CLAY)
            {
                world.setBlockState(pos, ModBlocks.POTTERY.get(PotteryBlock.Variant.WORKED).get().getDefaultState(), 3);
                world.playSound(null, pos, SoundEvents.BLOCK_GRAVEL_PLACE, SoundCategory.BLOCKS, 0.5F, 1.0F);
                Helpers.damageItem(player, context.getHand(), stack, 1);
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
                        world.setBlockState(pos, ModBlocks.POTTERY.get(next).get().getDefaultState(), 3);
                    }
                }
                world.playSound(null, pos, SoundEvents.BLOCK_GRAVEL_PLACE, SoundCategory.BLOCKS, 0.5F, 1.0F);
                Helpers.damageItem(player, context.getHand(), stack, 1);
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack)
    {
        return Helpers.damageItem(itemStack.copy(), 1);
    }

    @Override
    public boolean hasContainerItem(ItemStack stack)
    {
        return true;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
    {
        return enchantment.type == EnchantmentType.BREAKABLE;
    }
}
