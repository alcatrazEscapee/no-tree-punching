/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.common.items;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.alcatrazescapee.alcatrazcore.item.tool.ItemToolCore;
import com.alcatrazescapee.notreepunching.common.blocks.BlockPottery;
import com.alcatrazescapee.notreepunching.util.Pottery;

@ParametersAreNonnullByDefault
public class ItemClayTool extends ItemToolCore
{
    public ItemClayTool()
    {
        super(ToolMaterial.WOOD, 0.0f, 2.2f);

        setMaxStackSize(1);
        setMaxDamage(60);
        setContainerItem(this);
    }

    @Nonnull
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (!worldIn.isRemote)
        {
            ItemStack stack = player.getHeldItem(hand);
            IBlockState state = worldIn.getBlockState(pos);

            if (state.getBlock() == Blocks.CLAY)
            {
                worldIn.setBlockState(pos, BlockPottery.get(Pottery.WORKED).getDefaultState());
                worldIn.playSound(null, pos, SoundEvents.BLOCK_GRAVEL_PLACE, SoundCategory.BLOCKS, 0.5F, 1.0F);
                stack.damageItem(1, player);
            }
            else if (state.getBlock() instanceof BlockPottery)
            {
                if (worldIn.rand.nextDouble() < 0.4)
                {
                    Pottery next = ((BlockPottery) state.getBlock()).getType().next();

                    if (next == null)
                    {
                        worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
                    }
                    else
                    {
                        worldIn.setBlockState(pos, BlockPottery.get(next).getDefaultState());
                    }
                }
                worldIn.playSound(null, pos, SoundEvents.BLOCK_GRAVEL_PLACE, SoundCategory.BLOCKS, 0.5F, 1.0F);
                stack.damageItem(1, player);
            }
            player.setHeldItem(hand, stack);
        }
        return EnumActionResult.SUCCESS;
    }

    @Override
    @Nonnull
    public ItemStack getContainerItem(ItemStack stack)
    {
        ItemStack copy = stack.copy();
        copy.setItemDamage(copy.getItemDamage() + 2);
        if (copy.getItemDamage() >= copy.getMaxDamage())
        {
            copy = ItemStack.EMPTY;
        }
        return copy;
    }
}
