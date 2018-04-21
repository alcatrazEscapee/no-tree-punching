package notreepunching.block.firepit;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import notreepunching.block.ModBlocks;
import notreepunching.config.Config;

import javax.annotation.Nullable;

import static notreepunching.block.firepit.BlockFirepit.BURNING;

public class TileEntityFirepit extends TileEntity implements ITickable {

    private ItemStackHandler inventory = new ItemStackHandler(3);

    private int burnTicks;
    private int maxBurnTicks;

    private int cookTimer = 0;
    private int maxCookTimer = 0;

    private static final int NUM_FIELDS = 2;
    private static final byte BURN_FIELD_ID = 0;
    private static final byte MAX_BURN_FIELD_ID = 1;
    private static final byte COOK_FIELD_ID = 2;

    public TileEntityFirepit(){
        // Initial burn time from log that was thrown = 3000
        burnTicks = 50;
        maxBurnTicks = burnTicks;
    }

    public void update(){
        if(!world.isRemote) {
            IBlockState state = world.getBlockState(pos);
            //System.out.println("Firepit is currently burning? "+state.getValue(BURNING));
            if(state.getValue(BURNING)){
                // Firepit is currently burning
                //System.out.println("IS BURNING: "+burnTicks);
                burnTicks--;
                if(burnTicks <= 0){
                    // Try and consume one item in fuel slot
                    ItemStack is = inventory.getStackInSlot(0);
                    if(isItemValidFuel(is)){
                        burnTicks += TileEntityFurnace.getItemBurnTime(is) * Config.Firepit.FUEL_MULT;
                        maxBurnTicks = burnTicks;
                        is.shrink(1);
                        if(is.getCount()==0){
                            is = ItemStack.EMPTY;
                        }
                        inventory.setStackInSlot(0,is);
                    } else {

                        // Else, extinguish the firepit
                        burnTicks = 0;
                        world.setBlockState(pos, ModBlocks.firepit.getDefaultState().withProperty(BURNING, false));
                    }
                }

            }else{
                burnTicks = 0;
                maxBurnTicks = 0;
            }
        }

    }

    public static boolean isItemValidFuel(ItemStack is){
        return TileEntityFurnace.getItemBurnTime(is) > 0 && TileEntityFurnace.getItemBurnTime(is) <= Config.Firepit.FUEL_MAX;
    }

    public static boolean isItemValidInput(ItemStack is){
        return true; //                                         TODO: Change this to only accept valid items
    }

    public int getField(int id) {
        if (id == BURN_FIELD_ID) return burnTicks;
        if (id == MAX_BURN_FIELD_ID) return maxBurnTicks;
        if (id == COOK_FIELD_ID) return cookTimer;

        System.err.println("Invalid field ID in TileEntityFirepit.getField:" + id);
        return 0;
    }

    public void setField(int id, int value)
    {
        if (id == BURN_FIELD_ID) { burnTicks = (short) value; }
        else if (id == MAX_BURN_FIELD_ID) { maxBurnTicks = (short) value; }
        else if (id == COOK_FIELD_ID) { cookTimer = (short) value; }
        else {
            System.err.println("Invalid field ID in TileEntityFirepit.setField:" + id);
        }
    }

    public int getFieldCount() {
        return NUM_FIELDS;
    }

    public int getScaledBurnTicks(){
        if(maxBurnTicks!=0 && burnTicks!=0) {
            float f1 = burnTicks / (float) maxBurnTicks;
            return Math.round(14 * f1);
        }
        return 0;
    }
    public int getScaledCookTime(){
        if(maxCookTimer != 0 && cookTimer != 0){
            float f1 = cookTimer/ (float) maxCookTimer;
            return Math.round(23*f1);
        }
        return 0;
    }

    // ******************** Tile Entity / NBT Methods **************** //

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("inventory", inventory.serializeNBT());
        compound.setInteger("burn_ticks",burnTicks);
        compound.setInteger("max_burn_ticks",maxBurnTicks);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        inventory.deserializeNBT(compound.getCompoundTag("inventory"));
        burnTicks = compound.getInteger("burn_ticks");
        maxBurnTicks = compound.getInteger("max_burn_ticks");
        super.readFromNBT(compound);
    }

    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        NBTTagCompound updateTagDescribingTileEntityState = getUpdateTag();
        return new SPacketUpdateTileEntity(this.pos, 1, updateTagDescribingTileEntityState);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        NBTTagCompound updateTagDescribingTileEntityState = pkt.getNbtCompound();
        handleUpdateTag(updateTagDescribingTileEntityState);
    }

    /* Creates a tag containing the TileEntity information, used by vanilla to transmit from server to client
       Warning - although our getUpdatePacket() uses this method, vanilla also calls it directly, so don't remove it.
     */
    @Override
    public NBTTagCompound getUpdateTag()
    {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        writeToNBT(nbtTagCompound);
        return nbtTagCompound;
    }

    /* Populates this TileEntity with information from the tag, used by vanilla to transmit from server to client
     Warning - although our onDataPacket() uses this method, vanilla also calls it directly, so don't remove it.
   */
    @Override
    public void handleUpdateTag(NBTTagCompound tag)
    {
        this.readFromNBT(tag);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T)inventory : super.getCapability(capability, facing);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    {
        return (oldState.getBlock() != newState.getBlock());
    }

}
