/*
 *
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 *
 */

package notreepunching.item;


import mcp.MethodsReturnNonnullByDefault;
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
import net.minecraftforge.common.util.FakePlayer;
import notreepunching.block.BlockRock;
import notreepunching.block.ModBlocks;
import notreepunching.util.MiscUtil;

import static notreepunching.block.BlockRock.TYPE;

@MethodsReturnNonnullByDefault
public class ItemTuyere extends ItemBase {

    ItemTuyere(String name){
        super(name);

        setMaxStackSize(1);
    }

    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(player != null && !(player instanceof FakePlayer)){
            if(!worldIn.isRemote){
                // Check for block
                IBlockState state = worldIn.getBlockState(pos);
                if(MiscUtil.doesStackMatchOre(new ItemStack(state.getBlock()),"cobblestone")) {

                    player.setHeldItem(hand, ItemStack.EMPTY);
                    // Player is null here otherwise sound is only played to other players see: https://mcforge.readthedocs.io/en/latest/effects/sounds/
                    worldIn.playSound(null, pos, SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.BLOCKS, 1.0F, 0.6F);
                    worldIn.setBlockState(pos, ModBlocks.blockTuyere.getDefaultState().withProperty(TYPE, BlockRock.EnumMineralType.byState(state)));
                    return EnumActionResult.SUCCESS;
                }
            }
        }

        return EnumActionResult.PASS;
    }
}
