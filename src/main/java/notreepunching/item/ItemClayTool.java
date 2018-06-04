package notreepunching.item;

import com.google.common.collect.Sets;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.*;
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
        setMaxDamage(30);
    }

    public void register(){
        ModItems.addItemToRegistry(this,name, ModTabs.TOOLS_TAB);
        NoTreePunching.proxy.addModelToRegistry(new ItemStack(this), this.getRegistryName(), "inventory");
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
