package notreepunching.block.tile;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import notreepunching.block.ModBlocks;
import notreepunching.block.tile.inventory.ItemHandlerWrapper;
import notreepunching.recipe.forge.ForgeRecipe;
import notreepunching.recipe.forge.ForgeRecipeHandler;
import notreepunching.util.ItemUtil;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

import static notreepunching.block.BlockForge.BURNING;
import static notreepunching.block.BlockForge.LAYERS;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TileEntityForge extends TileEntitySidedInventory implements ITickable, IHasFields {

    private int burnTicks;
    private final int maxBurnTicks = 1600; // This can never be zero
    private int cookTicks;
    private int maxCookTicks = 400;
    private int temperature;
    private int minTemperature;
    private int maxTemperature;
    private int airTicks;
    private final int maxTemp;

    public boolean closed;
    private boolean burning;
    private boolean cooking;

    private final byte IN_SLOT = 0;
    private final byte OUT_SLOT = 1;

    // Fields
    private final byte NUM_FIELDS = 4;
    private final byte BURN_ID = 0;
    private final byte TEMP_ID = 1;
    private final byte COOK_ID = 2;
    private final byte MAX_COOK_ID = 3;

    private final ItemHandlerWrapper wrapper;


    public TileEntityForge(){
        super(2);
        burning = true;
        cooking = false;
        closed = true;

        burnTicks = maxBurnTicks;
        cookTicks = 0;
        temperature = 0;
        minTemperature = 800;
        maxTemperature = 0;
        maxTemp = 1500;

        wrapper = new ItemHandlerWrapper(inventory);
        wrapper.addExtractSlot(OUT_SLOT);
        wrapper.addInsertSlot(IN_SLOT);
    }

    public void update(){
        if(!world.isRemote) {
            IBlockState state = world.getBlockState(pos);
            burning = state.getValue(BURNING);

            if(burning){
                if(burnTicks <= 0){
                    burnTicks = maxBurnTicks;

                    // Burn off one layer of charcoal

                    int layers = state.getValue(LAYERS);
                    if(layers == 1){
                        // Set the block to air and drop contents
                        if(consumeFuelFromNearby(world, pos, layers)) {
                            world.setBlockState(pos, Blocks.AIR.getDefaultState());
                        }
                        return;
                    }else{
                        // Remove topmost layer
                        if(consumeFuelFromNearby(world, pos, layers)) {
                            world.setBlockState(pos, state.withProperty(LAYERS, layers - 1));
                        }
                    }
                    state = world.getBlockState(pos);
                    // Attempt to burn out
                    Random rand = new Random();
                    if((closed && layers == 2) || // Always burn out on last layer when closed
                            (closed && rand.nextFloat() < 0.25 && layers <= 3) || // 25% Chance to burn out when closed, layer  3>2
                            (!closed && rand.nextFloat() < 0.25 && layers <= 2)){ // 25% Chance to burn out when open, layer 2>1

                        world.setBlockState(pos, state.withProperty(BURNING, false));
                        burning = false;
                    }

                }else{
                    burnTicks -= closed ? 1 : 3;
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
            // Update Air ticks
            if(airTicks > 0){
                airTicks--;
                maxTemperature *= 1.5;
            }

            // Adjust temperature towards maxTemp
            if(temperature > maxTemperature){
                temperature --;
            }
            if(temperature < maxTemperature){
                temperature ++;
                if(temperature <= 1000 && airTicks >= 1){ temperature++; }
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

    public void setAirTimer(){
        this.airTicks = 40;
    }

    private void cookStacks(ForgeRecipe recipe){
        if(!world.isRemote) {

            ItemStack inStack = inventory.getStackInSlot(IN_SLOT);
            ItemStack outStack = inventory.getStackInSlot(OUT_SLOT);

            inventory.setStackInSlot(IN_SLOT, ItemUtil.consumeItem(inStack, recipe.getCount()));

            inventory.setStackInSlot(OUT_SLOT, ItemUtil.mergeStacks(outStack, recipe.getOutput()));
            this.markDirty();
        }
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return (oldState.getBlock() != newState.getBlock());
    }

    private boolean consumeFuelFromNearby(World world, BlockPos pos, int current){
        BlockPos currentPos = pos;
        int next;
        for(int i = -2; i <= 2; i++){
            for(int j = -2; j <= 2; j++){
                IBlockState state = world.getBlockState(pos.add(i,0,j));
                if(state.getBlock() == ModBlocks.forge){
                    next = state.getValue(LAYERS);
                    if(next > current || next == 8){
                        current = next;
                        currentPos = pos.add(i,0,j);
                    }
                }
            }
        }
        if(currentPos == pos){
            return true;
        }else{
            world.setBlockState(currentPos, ModBlocks.forge.getDefaultState().withProperty(BURNING, world.getBlockState(pos).getValue(BURNING)).withProperty(LAYERS, current - 1));
            return false;
        }
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
        if(burnTicks!=0 && world.getBlockState(pos).getValue(BURNING)) {
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
    protected NBTTagCompound writeNBT(NBTTagCompound compound) {
        compound.setInteger("burn_ticks", burnTicks);
        compound.setInteger("cook_ticks",cookTicks);
        compound.setInteger("temperature", temperature);
        compound.setBoolean("cooking", cooking);
        compound.setInteger("min_temperature", minTemperature);
        compound.setBoolean("closed", closed);
        compound.setInteger("air_ticks", airTicks);
        return compound;
    }

    @Override
    public void readNBT(NBTTagCompound compound) {
        burnTicks = compound.getInteger("burn_ticks");
        cookTicks = compound.getInteger("cook_ticks");
        temperature = compound.getInteger("temperature");
        cooking = compound.getBoolean("cooking");
        minTemperature = compound.getInteger("min_temperature");
        closed = compound.getBoolean("closed");
        airTicks = compound.getInteger("air_ticks");
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing == EnumFacing.UP){
            return (T) wrapper;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing == EnumFacing.UP) || super.hasCapability(capability, facing);
    }
}
