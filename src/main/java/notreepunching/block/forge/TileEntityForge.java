package notreepunching.block.forge;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import notreepunching.recipe.forge.ForgeRecipe;
import notreepunching.recipe.forge.ForgeRecipeHandler;
import notreepunching.util.ItemUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

import static notreepunching.block.forge.BlockForge.BURNING;
import static notreepunching.block.forge.BlockForge.LAYERS;

public class TileEntityForge extends TileEntity implements ITickable {

    private int burnTicks;
    private final int maxBurnTicks = 1600;
    private int cookTicks;
    private int maxCookTicks = 400;
    private int temperature;
    private int minTemperature;
    private int maxTemperature;
    private final int maxTemp;

    protected boolean closed;
    private boolean burning;
    private boolean cooking;

    private final byte IN_SLOT = 0;
    private final byte OUT_SLOT = 1;

    private final byte NUM_FIELDS = 4;
    private final byte BURN_ID = 0;
    private final byte TEMP_ID = 1;
    private final byte COOK_ID = 2;
    private final byte MAX_COOK_ID = 3;

    private ItemStackHandler inventory = new ItemStackHandler(2){
        @Override
        protected void onContentsChanged(int slot) {
            if(!world.isRemote) {
                TileEntityForge.this.markDirty();
            }
        }
    };

    public TileEntityForge(){
        burning = true;
        cooking = false;
        closed = true;

        burnTicks = maxBurnTicks;
        cookTicks = 0;
        temperature = 0;
        minTemperature = 800;
        maxTemperature = 0;
        maxTemp = 1500;
    }

    public void update(){
        if(!world.isRemote) {
            IBlockState state = world.getBlockState(pos);
            burning = state.getValue(BURNING);
            //System.out.println("THIS IS CURRENTLY BURNING? "+burning);

            if(burning){
                if(burnTicks <= 0){
                    burnTicks = maxBurnTicks;

                    // Burn off one layer of charcoal

                    int layers = state.getValue(LAYERS);
                    if(layers == 1){
                        // Set the block to air and drop contents
                        world.setBlockState(pos, Blocks.AIR.getDefaultState());
                        return;
                    }else{
                        // Remove topmost layer
                        world.setBlockState(pos,state.withProperty(LAYERS,layers - 1));
                    }
                    state = world.getBlockState(pos);
                    // Attempt to burn out
                    Random rand = new Random();
                    if((closed && layers == 2) || // Always burn out on last layer when closed
                            (closed && rand.nextFloat() < 0.25 && layers <= 4) || // 25% Chance to burn out when closed, layer  4>3>2
                            (!closed && rand.nextFloat() < 0.25 && layers <= 2)){ // 25% Chance to burn out when open, layer 2>1
                        // Always burn out at 1 when closed
                        world.setBlockState(pos, state.withProperty(BURNING, false));
                        burning = false;
                    }

                }else{
                    burnTicks -= burnAmount();
                }
            }

            // Update temperature (use burning as refrence)
            // if burning and bellows: 1500
            // if burning: 1000
            // if not closed: 500
            // if not: 0
            // Takes 75s to reach max temp, 50s to reach 1000.
            // One piece of charcoal provides 80s of fuel
            maxTemperature = burning ? 1000 : 0;
            if(!closed){ maxTemperature = 500; }

            // Adjust temperature towards maxTemp
            if(temperature > maxTemperature){
                temperature --;
            }
            if(temperature < maxTemperature){
                temperature ++;
            }


            ItemStack inStack = inventory.getStackInSlot(IN_SLOT);
            ItemStack outStack = inventory.getStackInSlot(OUT_SLOT);

            ForgeRecipe recipe = ForgeRecipeHandler.getRecipe(inStack);

            if(recipe == null){
                cooking = false;
            }else{
                minTemperature = recipe.getTemp();
                // Reference: At 0C = 5s
                // (Bricks) 400C = 11s
                // (Ingots) 1000C = 20s
                // (Steel) 1400C = 26s
                maxCookTicks = 400 + (int)(0.3*(minTemperature-1000));

                cooking = ItemUtil.canMergeStack(outStack, recipe.getOutput()) && temperature >= minTemperature;
            }

            // At this point it is cooking, with a recipe and items
            if(cooking){
                cookTicks++;

                if(cookTicks >= maxCookTicks){
                    // cook an item
                    cookStacks(recipe);
                    cookTicks = 0;
                }
            }else{
                if(cookTicks>0){
                    cookTicks = 0;
                }
            }
        }
    }

    private void cookStacks(@Nonnull ForgeRecipe recipe){
        if(!world.isRemote) {

            ItemStack inStack = inventory.getStackInSlot(IN_SLOT);
            ItemStack outStack = inventory.getStackInSlot(OUT_SLOT);

            inventory.setStackInSlot(IN_SLOT, ItemUtil.consumeItem(inStack, 1));

            inventory.setStackInSlot(OUT_SLOT, ItemUtil.mergeStacks(outStack, recipe.getOutput()));
            this.markDirty();
        }
    }

    private int burnAmount(){ return closed ? 1 : 3; }

    @Override
    @ParametersAreNonnullByDefault
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return (oldState.getBlock() != newState.getBlock());
    }

    // ************* FIELD METHODS ************* //

    public int getField(int id) {
        switch(id){
            case BURN_ID:
                return burnTicks;
            case TEMP_ID:
                return temperature;
            case COOK_ID:
                return cookTicks;
            case MAX_COOK_ID:
                return maxCookTicks;
        }
        return 0;
    }

    public void setField(int id, int value)
    {
        switch(id){
            case BURN_ID:
                burnTicks = (short) value;
                break;
            case TEMP_ID:
                temperature = (short) value;
                break;
            case COOK_ID:
                cookTicks = (short) value;
                break;
            case MAX_COOK_ID:
                maxCookTicks = (short) value;
                break;
        }
    }

    public int getFieldCount() {
        return NUM_FIELDS;
    }

    public int getScaledFuelTicks(){
        //System.out.println("SETTING FIELD: "+burnTicks+" | "+burning);
        if(maxBurnTicks!=0 && burnTicks!=0 && world.getBlockState(pos).getValue(BURNING)) {
            float f1 = burnTicks / (float) maxBurnTicks;
            return Math.round(14 * f1);
        }
        return 0;
    }
    public int getScaledTemp(){
        if(temperature != 0 && maxTemp != 0){
            float f1 = temperature/ (float) maxTemp;
            return Math.round(30*f1);
        }
        return 0;
    }
    public int getScaledCookTicks(){
        if(cookTicks != 0 && maxCookTicks != 0){
            float f1 = cookTicks/ (float) maxCookTicks;
            return Math.round(22*f1);
        }
        return 0;
    }

    // ************** NBT METHODS ************* //

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T)inventory : super.getCapability(capability, facing);
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("inventory", inventory.serializeNBT());
        compound.setInteger("burn_ticks", burnTicks);
        compound.setInteger("cook_ticks",cookTicks);
        compound.setInteger("temperature", temperature);
        compound.setBoolean("cooking", cooking);
        compound.setInteger("min_temperature", minTemperature);
        compound.setBoolean("closed", closed);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        inventory.deserializeNBT(compound.getCompoundTag("inventory"));
        burnTicks = compound.getInteger("burn_ticks");
        cookTicks = compound.getInteger("cook_ticks");
        temperature = compound.getInteger("temperature");
        cooking = compound.getBoolean("cooking");
        minTemperature = compound.getInteger("min_temperature");
        closed = compound.getBoolean("closed");
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
    @Nonnull
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
    public void handleUpdateTag(@Nonnull NBTTagCompound tag)
    {
        this.readFromNBT(tag);
    }
}
