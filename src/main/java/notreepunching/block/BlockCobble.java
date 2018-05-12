package notreepunching.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import notreepunching.NoTreePunching;
import notreepunching.item.ModItems;

public class BlockCobble extends BlockBase {

    public BlockCobble(String name){
        super(name+"_cobblestone", Material.ROCK);

        setHardness(2.0F);
        setResistance(10.0F);

    }

}
