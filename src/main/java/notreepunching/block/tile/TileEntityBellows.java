package notreepunching.block.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import notreepunching.NoTreePunching;
import notreepunching.network.ModNetwork;
import notreepunching.network.PacketRequestBellows;
import notreepunching.network.PacketUpdateBellows;

import javax.annotation.Nonnull;

public class TileEntityBellows extends TileEntity implements ITickable{

    private boolean power;
    private double height; // Min 0.125 Max 0.875
    private double step = 0.021;

    public TileEntityBellows(){
        super();
        power = false;
        height = 0.875;
    }

    public void update(){
        // This needs to run on both client and server (for graphical purposes)
        // the powered status is updated on the client through the network
        height += step;
        if(height <= 0.2 && step < 0){
            step = 0;
            height = 0.2;
        }else if(height >= 0.875 && step > 0){
            step = 0;
            height = 0.875;
        }
    }

    public void updatePower(boolean power){
        setPower(power);
        ModNetwork.network.sendToAllAround(new PacketUpdateBellows(TileEntityBellows.this), new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64));
    }

    @Override
    public void onLoad() {
        if (world.isRemote) {
            ModNetwork.network.sendToServer(new PacketRequestBellows(this));
        }
    }

    public double getHeight(){ return height; }
    public void setPower(boolean power){
        this.power = power;
        this.step = power ? 0.016875 : -0.016875;
        this.markDirty();
    }
    public boolean getPower(){ return power; }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setBoolean("power", power);
        compound.setDouble("height", height);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        power = compound.getBoolean("power");
        height = compound.getDouble("height");
        super.readFromNBT(compound);
    }
}
