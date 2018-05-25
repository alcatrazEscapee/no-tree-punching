package notreepunching.item;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import notreepunching.block.ModBlocks;
import notreepunching.util.ItemUtil;

@MethodsReturnNonnullByDefault
public class ItemTuyere extends ItemBase{

    public ItemTuyere(String name){
        super(name);
        setMaxStackSize(1);
    }

    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote){
            IBlockState state = worldIn.getBlockState(pos);
            worldIn.setBlockState(pos, ModBlocks.blockTuyere.getDefaultState());
            player.setHeldItem(hand, ItemUtil.consumeItem(player.getHeldItem(hand)));
        }
        return EnumActionResult.SUCCESS;
    }
}
