package notreepunching.block.tile;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TileEntityBlastFurnace extends TileEntitySidedInventory {

    public TileEntityBlastFurnace(){
        super(3);
    }

    @Override
    protected void readNBT(NBTTagCompound c) {

    }

    @Nonnull
    @Override
    protected NBTTagCompound writeNBT(NBTTagCompound c) {
        return c;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return super.getCapability(capability, facing);
    }
}
