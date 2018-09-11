/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.common.items;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;

import net.minecraft.block.state.IBlockState;
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
import com.alcatrazescapee.alcatrazcore.util.CoreHelpers;
import com.alcatrazescapee.alcatrazcore.util.OreDictionaryHelper;
import com.alcatrazescapee.notreepunching.common.blocks.BlockRock;
import com.alcatrazescapee.notreepunching.util.Stone;

public class ItemRock extends ItemCore
{
    private static Map<Stone, ItemRock> MAP = new HashMap<>();

    public static ItemRock get(Stone type)
    {
        return MAP.get(type);
    }

    private final Stone type;

    public ItemRock(Stone type)
    {
        super();
        this.type = type;
        MAP.put(type, this);

        OreDictionaryHelper.register(this, "rock");
        OreDictionaryHelper.register(this, "rock", type.name());
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        // Try and place
        IBlockState stateUnder = worldIn.getBlockState(pos);
        IBlockState stateAt = worldIn.getBlockState(pos.up());

        if (stateUnder.isNormalCube() && stateAt.getBlock().isReplaceable(worldIn, pos.up()))
        {
            if (!worldIn.isRemote)
            {
                ItemStack stack = player.getHeldItem(hand);

                player.setHeldItem(hand, CoreHelpers.consumeItem(player, stack));

                worldIn.playSound(null, pos, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                worldIn.setBlockState(pos.up(), BlockRock.get(type).getDefaultState());
            }
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }
}
