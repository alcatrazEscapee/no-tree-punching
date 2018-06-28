/*
 *
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 *
 */

package notreepunching.item;

import com.google.common.collect.Sets;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import notreepunching.NoTreePunching;
import notreepunching.block.BlockWorkedClay;
import notreepunching.block.ModBlocks;
import notreepunching.client.ModTabs;

import javax.annotation.ParametersAreNonnullByDefault;

import static notreepunching.block.BlockWorkedClay.TYPE;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ItemClayTool extends ItemTool {

    public String name;

    ItemClayTool(String name){
        super(ToolMaterial.STONE, Sets.newHashSet());

        this.name = name;
        register();

        setMaxStackSize(1);
        setMaxDamage(60);
        setContainerItem(this);
    }

    public void register(){
        ModItems.addItemToRegistry(this,name, ModTabs.TOOLS_TAB);
        NoTreePunching.proxy.addModelToRegistry(new ItemStack(this), this.getRegistryName(), "inventory");
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack){
        ItemStack copy = stack.copy();
        copy.setItemDamage(copy.getItemDamage()+2);
        if(copy.getItemDamage() == copy.getMaxDamage()){
            copy = ItemStack.EMPTY;
        }
        return copy;
    }

    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote) {

            ItemStack stack = player.getHeldItem(hand);
            IBlockState state = worldIn.getBlockState(pos);

            if (state.getBlock() == Blocks.CLAY){
                worldIn.setBlockState(pos, ModBlocks.workedClay.getDefaultState());
                worldIn.playSound(null, pos, SoundEvents.BLOCK_GRAVEL_PLACE, SoundCategory.BLOCKS, 0.5F, 1.0F);
                stack.damageItem(1, player);
            }else if(state.getBlock() == ModBlocks.workedClay){
                if(worldIn.rand.nextDouble() < 0.4) {
                    if(state.getValue(TYPE) == BlockWorkedClay.EnumClayType.SMALL_POT){
                        worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
                    }else {
                        worldIn.setBlockState(pos, state.withProperty(TYPE, state.getValue(TYPE).getNext()));
                    }
                }
                worldIn.playSound(null, pos, SoundEvents.BLOCK_GRAVEL_PLACE, SoundCategory.BLOCKS, 0.5F, 1.0F);
                stack.damageItem(1, player);
            }
            player.setHeldItem(hand, stack);
        }
        return EnumActionResult.SUCCESS;
    }
}
