package notreepunching.block.tile;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import notreepunching.block.IHasBellowsInput;
import notreepunching.block.ModBlocks;

public class TileEntityTuyere extends TileEntity implements IHasBellowsInput{

    public TileEntityTuyere(){
        super();
    }

    @Override
    public void setAirTimer() {
        for(EnumFacing face : EnumFacing.HORIZONTALS){
            if(world.getBlockState(pos.offset(face)).getBlock() == ModBlocks.forge){
                TileEntityForge te = (TileEntityForge) world.getTileEntity(pos.offset(face));
                if(te != null) te.setAirTimer();
            }
        }
    }
}
