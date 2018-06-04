package notreepunching.block.tile;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TileEntityLargeVessel extends TileEntitySidedInventory {

    public TileEntityLargeVessel(){
        super(9);
    }

    @Override
    public void setAndUpdateSlots(int slot) {
        this.markDirty();
    }

    @Override
    protected void readNBT(NBTTagCompound c) {
    }

    @Override
    protected NBTTagCompound writeNBT(NBTTagCompound c) {
        return c;
    }
}
