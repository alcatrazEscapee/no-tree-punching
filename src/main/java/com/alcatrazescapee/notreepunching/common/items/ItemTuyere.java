/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.common.items;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.alcatrazescapee.alcatrazcore.item.ItemCore;
import com.alcatrazescapee.alcatrazcore.util.OreDictionaryHelper;
import com.alcatrazescapee.notreepunching.common.blocks.BlockCobble;
import com.alcatrazescapee.notreepunching.common.blocks.BlockTuyere;
import com.alcatrazescapee.notreepunching.util.Stone;

public class ItemTuyere extends ItemCore
{
    public ItemTuyere()
    {
        setMaxStackSize(1);
        OreDictionaryHelper.register(this, "tuyere");
    }

    @Nonnull
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (player != null && !worldIn.isRemote)
        {
            // Check for block
            Stone type = BlockCobble.get(worldIn.getBlockState(pos).getBlock());
            if (type != null)
            {
                if (!player.isCreative())
                    player.setHeldItem(hand, ItemStack.EMPTY);
                worldIn.playSound(null, pos, SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.BLOCKS, 1.0F, 0.6F);
                worldIn.setBlockState(pos, BlockTuyere.get(type).getDefaultState());
                return EnumActionResult.SUCCESS;
            }
        }

        return EnumActionResult.PASS;
    }
}
