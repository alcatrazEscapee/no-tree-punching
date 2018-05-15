package notreepunching.block.firepit;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import notreepunching.block.IHasFields;
import notreepunching.block.ModBlocks;
import notreepunching.block.TileEntityInventory;
import notreepunching.config.Config;
import notreepunching.recipe.firepit.FirepitRecipe;
import notreepunching.recipe.firepit.FirepitRecipeHandler;
import notreepunching.util.ItemUtil;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static notreepunching.block.firepit.BlockFirepit.BURNING;

public class TileEntityFirepit extends TileEntityInventory implements ITickable, IHasFields {

    private int burnTicks;
    private int maxBurnTicks;

    private int cookTimer = 0;
    private int maxCookTimer = 0;

    private static final int NUM_FIELDS = 4;
    private static final byte BURN_FIELD_ID = 0;
    private static final byte MAX_BURN_FIELD_ID = 1;
    private static final byte COOK_FIELD_ID = 2;
    private static final byte MAX_COOK_FIELD_ID = 3;

    public TileEntityFirepit(){
        super(3);
        // Initial burn time from log that was thrown = 300
        burnTicks = 300*Config.Firepit.FUEL_MULT;
        maxBurnTicks = burnTicks;
    }

    public void update(){
        if(!world.isRemote) {
            IBlockState state = world.getBlockState(pos);
            if(state.getValue(BURNING)){

                // Try and cook the item in the firepit
                ItemStack cookStack = inventory.getStackInSlot(1);
                ItemStack outStack = inventory.getStackInSlot(2);
                if(!cookStack.isEmpty() && isItemValidInput(cookStack)){
                    FirepitRecipe recipe = FirepitRecipeHandler.getRecipe(cookStack);

                    if(ItemUtil.canMergeStack(recipe.getOutput(),outStack)){
                        cookTimer++;
                        maxCookTimer = Config.Firepit.COOK_MULT;

                        if(cookTimer >= maxCookTimer){
                            // Cook an item
                            cookStack = ItemUtil.consumeItem(cookStack,1);
                            outStack = ItemUtil.mergeStacks(outStack, recipe.getOutput());

                            inventory.setStackInSlot(1,cookStack);
                            inventory.setStackInSlot(2,outStack);
                            this.markDirty();

                            cookTimer = 0;
                        }
                    }
                }else{
                    cookTimer = 0;
                    maxCookTimer = 0;
                }

                burnTicks--;
                if(burnTicks <= 0){
                    // Try and consume one item in fuel slot
                    ItemStack is = inventory.getStackInSlot(0);
                    if(isItemValidFuel(is)){
                        burnTicks += TileEntityFurnace.getItemBurnTime(is) * Config.Firepit.FUEL_MULT;
                        maxBurnTicks = burnTicks;
                        is = ItemUtil.consumeItem(is);
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
        return FirepitRecipeHandler.isRecipe(is);
    }

    public int getField(int id) {
        if (id == BURN_FIELD_ID) return burnTicks;
        if (id == MAX_BURN_FIELD_ID) return maxBurnTicks;
        if (id == COOK_FIELD_ID) return cookTimer;
        if (id == MAX_COOK_FIELD_ID) return maxCookTimer;

        System.err.println("Invalid field ID in TileEntityFirepit.getField:" + id);
        return 0;
    }

    public void setField(int id, int value)
    {
        if (id == BURN_FIELD_ID) { burnTicks = (short) value; }
        else if (id == MAX_BURN_FIELD_ID) { maxBurnTicks = (short) value; }
        else if (id == COOK_FIELD_ID) { cookTimer = (short) value; }
        else if (id == MAX_COOK_FIELD_ID) { maxCookTimer = (short) value; }
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
            return Math.round(13 * f1);
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

    public void resetCookTimer(){
        if(!world.isRemote){
            cookTimer = 0;
        }
    }

    // ******************** Tile Entity / NBT Methods **************** //

    @Override
    @Nonnull
    protected NBTTagCompound writeNBT(NBTTagCompound compound) {
        compound.setTag("inventory", inventory.serializeNBT());
        compound.setInteger("burn_ticks",burnTicks);
        compound.setInteger("max_burn_ticks",maxBurnTicks);
        compound.setInteger("cook_ticks",cookTimer);
        return compound;
    }

    @Override
    protected void readNBT(NBTTagCompound compound) {
        inventory.deserializeNBT(compound.getCompoundTag("inventory"));
        burnTicks = compound.getInteger("burn_ticks");
        maxBurnTicks = compound.getInteger("max_burn_ticks");
        cookTimer = compound.getInteger("cook_ticks");
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    {
        return (oldState.getBlock() != newState.getBlock());
    }

}
