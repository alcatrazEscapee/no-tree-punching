package notreepunching.block.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import notreepunching.block.ModBlocks;
import notreepunching.client.sound.ModSounds;
import notreepunching.network.ModNetwork;
import notreepunching.network.PacketRequestBellows;
import notreepunching.network.PacketUpdateBellows;

import javax.annotation.Nonnull;

public class TileEntityBellows extends TileEntity implements ITickable{

    private boolean power;
    private double height; // Min 0.125 Max 0.875
    private final double stepSize = 0.028;
    private double step = stepSize;
    private int facing;

    public TileEntityBellows(){
        super();
        power = false;
        height = 0.2;
    }
    public TileEntityBellows(EnumFacing facing){
        this();
        this.facing = facing.getHorizontalIndex();
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
        if(power != this.power){
            if(!world.isRemote){
                updateForges();
                world.playSound(null,pos, power ? ModSounds.bellowsOut : ModSounds.bellowsIn, SoundCategory.BLOCKS,2.0F,1.0F);
            }
        }
        this.power = power;
        this.step = power ? stepSize : -stepSize;
        this.markDirty();
    }
    public boolean getPower(){ return power; }
    public int getFacing(){ return facing; }
    public void setFacing(int facing){ this.facing = facing; }

    private void updateForges(){
        if(!world.isRemote) {
            BlockPos forgePos = getPos().offset(EnumFacing.getHorizontal(facing)).down();

            IBlockState state = world.getBlockState(forgePos);
            if (state.getBlock() != ModBlocks.forge) return;

            TileEntityForge te = ((TileEntityForge) world.getTileEntity(forgePos));
            if (te != null) {
                te.setAirTimer();
            }
        }
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setBoolean("power", power);
        compound.setDouble("height", height);
        compound.setInteger("facing", facing);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        power = compound.getBoolean("power");
        height = compound.getDouble("height");
        facing = compound.getInteger("facing");
        super.readFromNBT(compound);
    }

}
