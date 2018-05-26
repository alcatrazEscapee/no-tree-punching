package notreepunching.block.tile;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TileEntityGrindstone extends TileEntityInventory implements ITickable {

    private int timer = 0;

    public TileEntityGrindstone(){
        super(2);
    }

    public void update(){
        timer++;
        if(timer == 360){
            timer = 0;
        }
    }

    public int getTimer(){ return timer; }

    @Override
    protected NBTTagCompound writeNBT(NBTTagCompound compound) {

        return compound;
    }

    @Override
    public void readNBT(NBTTagCompound compound) {

    }
}
