package notreepunching.block.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import notreepunching.block.ModBlocks;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static notreepunching.block.BlockWoodPile.ONFIRE;

import static notreepunching.block.BlockCharcoalPile.LAYERS;

public class TileEntityWoodPile extends TileEntityInventory implements ITickable {

    public boolean burning;
    private int burnTicks;
    private final int maxBurnTicks;

    public static final int NUM_SLOTS = 4;

    public TileEntityWoodPile(){
        super(NUM_SLOTS,4);

        burnTicks = 0;
        maxBurnTicks = 200;
        burning = false;
    }

    public void update(){
        if(world.isRemote) { return; }

        if (burning) {
            if(burnTicks < maxBurnTicks){
                burnTicks ++;
            }else{
                // Turn this log pile into charcoal
                createCharcoal();
            }
        }else{
            if(world.getBlockState(pos).getBlock() == Blocks.FIRE){
                burning = true;
            }
        }
    }

    @Override
    public void setAndUpdateSlots(){
        if(world.isRemote){ return; }
        for(int i = 0; i < 4; i++){
            if(!inventory.getStackInSlot(i).isEmpty()){
                return;
            }
        }

        world.setBlockToAir(pos);
    }

    public void insertLog(@Nonnull ItemStack stack){
        inventory.setStackInSlot(0, stack);
    }

    public void createCharcoal(){
        double logs = 0;
        for(int i = 0; i < inventory.getSlots(); i++){
            logs += inventory.getStackInSlot(i).getCount();
        }
        double log2 = 0.015625*logs*logs + 3.0d*Math.random() + 1.0d;
        int charcoal = (int) Math.round(log2);
        world.setBlockState(pos, ModBlocks.charcoalPile.getDefaultState().withProperty(LAYERS, charcoal));
    }

    public void tryLightNearby(World world, BlockPos pos){
        for(EnumFacing side : EnumFacing.values()){
            IBlockState state = world.getBlockState(pos.offset(side));
            if(state.getBlock() == ModBlocks.woodPile){
                if(state.getValue(ONFIRE)) continue;
                world.setBlockState(pos.offset(side),state.withProperty(ONFIRE, true));
                TileEntityWoodPile tile = (TileEntityWoodPile) world.getTileEntity(pos.offset(side));
                if(tile == null) continue;
                tile.burning = true;
                tile.tryLightNearby(world, pos.offset(side));
            }
        }
    }

    @Nonnull
    @Override
    protected NBTTagCompound writeNBT(NBTTagCompound c) {
        c.setInteger("burn_ticks",burnTicks);
        c.setBoolean("burning", burning);
        return c;
    }

    protected void readNBT(NBTTagCompound c){
        burnTicks = c.getInteger("burn_ticks");
        burning = c.getBoolean("burning");
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    {
        return (oldState.getBlock() != newState.getBlock());
    }

}
