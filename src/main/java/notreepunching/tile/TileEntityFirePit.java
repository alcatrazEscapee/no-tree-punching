package notreepunching.tile;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import notreepunching.NoTreePunching;

import javax.annotation.Nullable;

public class TileEntityFirePit extends TileEntityBase implements ITickable{

    public boolean burning;
    private int counter;
    private int burnTime;

    /*public static int INPUT_SLOT_IDX = 0;
    public static int OUTPUT_SLOT_IDX = 1;
    public static int FUEL_SLOT_IDX = 2;

    private float temperature;

    private int burnTicks;
    private int maxBurnTicks;

    private int cookTimer;
    private int maxCookTimer;

    private ItemStackHandler inventory = new ItemStackHandler(3)
        {
            @Override
            protected void onContentsChanged(int slot) {
             TileEntityFirePit.this.markDirty();
             //TileEntityFirePit.this.updateCrafting();
            }
        };*/

    public TileEntityFirePit(){
        counter = 0;
        burnTime = 30;
        lightFirepit();
    }

    public void update(){
        if(world.isRemote) {
            return;
        }

        // Learning how to tile entity code
        counter++;
        if (counter%5 == 0) {
            //counter = 0;
            //System.out.println("Firepit | Lit? " + burning + " | Burn Ticks: " + burnTicks + " | Temperature: " + temperature);
            if (burning) {
                NoTreePunching.proxy.generateTEParticle(this,EnumParticleTypes.FLAME);
            }
        }
        if(counter == 20){
            counter = 1;
            if(burning){
                burnTime--;
                if(burnTime==0){
                    burning = false;
                }
            }

        }
        /*// Natural cooling of firepit
        if(temperature>0){
            temperature -= 0.2;
        }
        // firepit became too cold to continue burning, requires relighting
        if(temperature<0){
            burning = false;
            this.
            //this.world.getBlockState(this.pos).getBlock().setLightLevel(0f);
            temperature = 0;
        }
        // if burning, remove fuel and increace burnTicks
        if(burning){
            if(burnTicks == 0 && inventory.getStackInSlot(FUEL_SLOT_IDX) != null) {
                ItemStack stack = inventory.getStackInSlot(FUEL_SLOT_IDX);

                burnTicks += getItemBurnTime(stack.getItem());
                maxBurnTicks = burnTicks;

                stack.shrink(1);
                if (stack.getCount() == 0 || stack.isEmpty()) {
                    inventory.setStackInSlot(FUEL_SLOT_IDX,ItemStack.EMPTY);
                }
            }

            if(burnTicks > 0){
                burnTicks--;

                temperature += 0.4;
                if(temperature >100){
                    temperature = 100;
                }
            }

            ItemStack cookStack = inventory.getStackInSlot(INPUT_SLOT_IDX);
            ItemStack outStack = inventory.getStackInSlot(OUTPUT_SLOT_IDX);
            //ItemHeat.increace(cookStack,3f); DO THINGS WITH HEAT HERE LATER
            if(cookStack != ItemStack.EMPTY && outStack.getCount() < outStack.getItem().getItemStackLimit(outStack)){
                if(FirePitCrafting.getOutputStack(cookStack).getItem() == outStack.getItem() || outStack.isEmpty()) {
                    cookTimer += (int) temperature;
                    maxCookTimer = FirePitCrafting.getCookTime(cookStack);
                    if (cookTimer >= maxCookTimer) {
                        cookItemInFirepit(cookStack);
                    }
                }else{
                    cookTimer = 0;
                }
            }else{
                cookTimer = 0;
            }

        }*/
    }

    /*public void build(ItemStack startLog){
        if(startLog.getCount()>1){
            startLog.setCount(1);
        }
        inventory.setStackInSlot(FUEL_SLOT_IDX,startLog);

        lightFirepit();
    }*/

    public void lightFirepit(){
        if(burnTime>0) {
            burning = true;
        }
        //temperature = 30;
        //burnTicks = 0;
        //maxBurnTicks = 0;
        System.out.println("LIT VIA THE FIRESTARTER");
        //this.world.getBlockState(this.pos).getBlock().setLightLevel(0f);

        //maxCookTimer = 10*20*100; // 10 seconds * 20 ticks / second * optimal firepit temperature (100)
    }

    public boolean addFuel(){
        if(burnTime>20){
            return false;
        }else{
            burnTime+=10;
            return true;
        }
    }

    /*public void cookItemInFirepit(ItemStack cookStack){
        if(!world.isRemote) {
            cookTimer = 0;
            ItemStack cookedStack = FirePitCrafting.getOutputStack(cookStack);
            cookStack.shrink(1);
            if (cookStack.getCount() == 0 || cookStack.isEmpty()) {
                inventory.setStackInSlot(INPUT_SLOT_IDX, ItemStack.EMPTY);
            }
            if(inventory.getStackInSlot(OUTPUT_SLOT_IDX).isEmpty()) {
                inventory.setStackInSlot(OUTPUT_SLOT_IDX, cookedStack);
            }
            else{
                inventory.getStackInSlot(OUTPUT_SLOT_IDX).grow(1);
            }
        }
    }*/

    // ------------------- NBT Saving and Reading ----------------------- //

    public void readSyncableNBT(NBTTagCompound nbt){
        //temperature = nbt.getFloat("temperature");
        //burnTicks = nbt.getInteger("burnTicks");
        //maxBurnTicks = nbt.getInteger("maxBurnTicks");
        //cookTimer = nbt.getInteger("cookTimer");
        //maxCookTimer = nbt.getInteger("maxCookTimer");
    }

    public void readNonSyncableNBT(NBTTagCompound nbt){
        //inventory.deserializeNBT(nbt.getCompoundTag("inventory"));
    }

    public void writeSyncableNBT(NBTTagCompound nbt){
        //nbt.setFloat("temperature",temperature);
        //nbt.setInteger("burnTicks",burnTicks);
        //nbt.setInteger("maxBurnTicks",maxBurnTicks);
        //nbt.setInteger("cookTimer",cookTimer);
        //nbt.setInteger("maxCookTimer",maxCookTimer);
    }

    public void writeNonSyncableNBT(NBTTagCompound nbt){
        //nbt.setTag("inventory", inventory.serializeNBT());
    }

    /*@Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T)inventory : super.getCapability(capability, facing);
    }

    public static boolean isItemFuel(Item item){
       return(getItemBurnTime(item)>0);
    }

    public static int getItemBurnTime(Item item){
        if(item == Item.getItemFromBlock(Blocks.PLANKS)){
            return 160; // planks have -20% efficiency as a fuel
        }
        if(item == Item.getItemFromBlock(Blocks.LOG) || item == Item.getItemFromBlock(Blocks.LOG2)){
            return 800;
        }

        return 0;
    }

    public int getScaledTemp(){
        float f1 = temperature/100f;
        return Math.round(31*f1);
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
    }*/

}
