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
import notreepunching.block.IHasBellowsInput;
import notreepunching.block.ModBlocks;
import notreepunching.block.tile.inventory.ItemHandlerWrapper;
import notreepunching.recipe.forge.BlastRecipeHandler;
import notreepunching.recipe.forge.ForgeRecipe;
import notreepunching.util.ItemUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.Random;

import static notreepunching.block.BlockForge.BURNING;
import static notreepunching.block.BlockForge.LAYERS;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TileEntityBlastFurnace extends TileEntitySidedInventory implements IHasFields, ITickable, IHasBellowsInput {

    private final byte INPUT_SLOT = 0;
    private final byte OUTPUT_SLOT = 1;

    private int burnTicks;
    private final int maxBurnTicks = 400; // This can never be zero
    private int cookTicks;
    private int maxCookTicks = 400;
    private int temperature;
    private int minTemperature;
    private int maxTemperature;
    private int airTicks;
    private int charcoal;
    private int counter;

    private boolean closed;
    private boolean burning;
    private boolean cooking;

    // Fields
    private final byte BURN_ID = 0;
    private final byte TEMP_ID = 1;
    private final byte COOK_ID = 2;
    private final byte CHARCOAL_ID = 3;

    private final ItemHandlerWrapper outputWrapper;
    private final ItemHandlerWrapper inputWrapper;

    public TileEntityBlastFurnace(){
        super(2);
        burning = false;
        cooking = false;
        closed = true;

        burnTicks = 0;
        cookTicks = 0;
        temperature = 0;
        minTemperature = 800;
        maxTemperature = 0;
        counter = 0;
        airTicks = 0;

        outputWrapper = new ItemHandlerWrapper(inventory, this);
        outputWrapper.addExtractSlot(OUTPUT_SLOT);

        inputWrapper = new ItemHandlerWrapper(inventory, this);
        inputWrapper.addInsertSlot(INPUT_SLOT);
    }

    public void update(){
        if(!world.isRemote) {
            // Update burn timer
            IBlockState state = world.getBlockState(pos);
            burning = state.getValue(BURNING);

            if(burning){
                if(burnTicks <= 0){
                    burnTicks = maxBurnTicks;

                    // Burn off one layer of charcoal
                    updateMultiblock();
                    charcoal--;
                    if(charcoal <= 0){
                        burning = false;
                        world.setBlockState(pos, state.withProperty(BURNING, false));
                    }else if(charcoal%8 ==  0){
                        world.setBlockState(pos.up(1 + charcoal/8), Blocks.AIR.getDefaultState());
                    }else{
                        world.setBlockState(pos.up(1 + charcoal/8), ModBlocks.charcoalPile.getDefaultState().withProperty(LAYERS, charcoal%8));
                    }

                }else{
                    burnTicks--;
                }
            }
            // Update air timer
            if(airTicks > 0) {
                airTicks--;
            }
            // Update temperature
            if(burning){
                maxTemperature = 800 + (int)(12.5d*charcoal) + (airTicks>0 ? 300 : 0);
            }else{
                maxTemperature = 0;
            }
            if(temperature > maxTemperature){
                temperature -= 2;
            }
            if(temperature < maxTemperature){
                temperature += 2;
            }
            // Update counter to check if still multiblock
            counter++;
            if(counter == 20){
                counter = 0;
                updateMultiblock();
            }
            // Update cook timer
            if(cooking){
                cookTicks++;
                if(cookTicks >= maxCookTicks){
                    cookStacks();
                    cookTicks = 0;
                    setAndUpdateSlots(0);
                }
            }else{
                if(cookTicks>0){
                    cookTicks = 0;
                }
            }
        }
    }

    @Override
    public void setAndUpdateSlots(int slot) {
        if(!world.isRemote) {
            this.markDirty();
            System.out.println("GOT SLOT UPDATE");
            // do things to check slot things
            updateCooking();
        }
    }

    public void setAirTimer(){
        airTicks = 80;
    }

    private void updateMultiblock(){
        charcoal = 0;
        for(int i = 1; i <= 3; i++){

            boolean flag = true;
            for(EnumFacing facing : EnumFacing.HORIZONTALS){
                IBlockState state = world.getBlockState(pos.up(i).offset(facing));
                if(state.getBlock() != Blocks.STONEBRICK){
                    flag = false;
                    break;
                }
            }
            if(flag){
                IBlockState state = world.getBlockState(pos.up(i));
                if(state.getBlock() == ModBlocks.charcoalPile){
                    charcoal += state.getValue(LAYERS);
                }
            }
        }
        closed = charcoal != 0;
    }

    private void updateCooking(){
        if(!world.isRemote) {
            ItemStack inStack = inventory.getStackInSlot(INPUT_SLOT);
            ItemStack outStack = inventory.getStackInSlot(OUTPUT_SLOT);

            ForgeRecipe recipe = BlastRecipeHandler.getRecipe(inStack);
            cooking = false;
            // Check that recipe is nonnull
            if (recipe == null) return;
            // Check temperature
            if (temperature < recipe.getTemp()) return;
            // Check output can merge
            if (!ItemUtil.canMergeStack(outStack, recipe.getOutput())) return;
            cooking = true;
        }
    }

    private void cookStacks(){
        if(!world.isRemote) {
            ItemStack inStack = inventory.getStackInSlot(INPUT_SLOT);
            ItemStack outStack = inventory.getStackInSlot(OUTPUT_SLOT);

            ForgeRecipe recipe = BlastRecipeHandler.getRecipe(inStack);

            // Check that recipe is nonnull
            if (recipe != null) {
                inventory.setStackInSlot(INPUT_SLOT, ItemUtil.consumeItem(inStack, recipe.getCount()));
                inventory.setStackInSlot(OUTPUT_SLOT, ItemUtil.mergeStacks(outStack, recipe.getOutput()));
            }
        }

    }

    @Override
    protected void readNBT(NBTTagCompound c) {
        burnTicks = c.getInteger("burn_ticks");
        cookTicks = c.getInteger("cook_ticks");
        temperature = c.getInteger("temperature");
        cooking = c.getBoolean("cooking");
        minTemperature = c.getInteger("min_temperature");
        closed = c.getBoolean("closed");
        airTicks = c.getInteger("air_ticks");
    }

    @Override
    protected NBTTagCompound writeNBT(NBTTagCompound c) {
        c.setInteger("burn_ticks", burnTicks);
        c.setInteger("cook_ticks",cookTicks);
        c.setInteger("temperature", temperature);
        c.setBoolean("cooking", cooking);
        c.setInteger("min_temperature", minTemperature);
        c.setBoolean("closed", closed);
        c.setInteger("air_ticks", airTicks);
        return c;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing != null && facing != EnumFacing.UP){
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing != null && facing != EnumFacing.UP){
            if(facing == EnumFacing.DOWN){
                return (T) outputWrapper;
            }else{
                return (T) inputWrapper;
            }
        }
        return super.getCapability(capability, facing);
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
            case CHARCOAL_ID:
                return charcoal;
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
            case CHARCOAL_ID:
                charcoal = (short) value;
                break;
        }
    }

    public int getFieldCount() {
        return 4;
    }

    public int getScaledFuelTicks(){
        if(burnTicks!=0 && world.getBlockState(pos).getValue(BURNING)) {
            float f1 = burnTicks / (float) maxBurnTicks;
            return Math.round(14 * f1);
        }
        return 0;
    }
    public int getScaledTemp(){
        if(temperature != 0){
            float f1 = temperature/ 1500.0f;
            return Math.round(30*f1);
        }
        return 0;
    }
    public int getScaledCharcoal(){
        return charcoal*2;
    }
    public int getScaledCookTicks(){
        if(cookTicks != 0 && maxCookTicks != 0){
            float f1 = cookTicks/ (float) maxCookTicks;
            return Math.round(22*f1);
        }
        return 0;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return (oldState.getBlock() != newState.getBlock());
    }

}
