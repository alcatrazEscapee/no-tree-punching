/*
 *
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 *
 */

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
