package notreepunching.block.tile;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import notreepunching.item.ModItems;
import notreepunching.network.*;
import notreepunching.recipe.grindstone.GrindstoneRecipe;
import notreepunching.recipe.grindstone.GrindstoneRecipeHandler;
import notreepunching.util.ItemUtil;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TileEntityGrindstone extends TileEntityInventory implements ITickable, IHasFields {

    private int rotation = 0;
    private boolean hasWheel = false;
    private GrindstoneRecipe currentRecipe;

    private final byte INPUT_SLOT = 0;
    private final byte OUTPUT_SLOT = 1;
    private final byte WHEEL_SLOT = 2;

    public TileEntityGrindstone(){
        super(3);
    }

    public void update(){
        if(rotation >= 1){
            rotation += 3;
        }
        if(rotation > 360){
            rotation = 0;
            if(!world.isRemote){
                finishGrinding();
            }
        }
    }

    @Override
    public void setAndUpdateSlots(){
        updateWheel();
    }

    public void startGrinding() {
        if (!world.isRemote) {
            if(rotation >= 1) return;

            ItemStack input = inventory.getStackInSlot(INPUT_SLOT);
            ItemStack output = inventory.getStackInSlot(OUTPUT_SLOT);

            GrindstoneRecipe recipe = GrindstoneRecipeHandler.getRecipe(input, false);
            if(recipe != null){
                if(!ItemUtil.canMergeStack(output,recipe.getOutput())) return;

                // check if grind wheel is powerful enough
                ItemStack wheel = inventory.getStackInSlot(WHEEL_SLOT);
                if(wheel.getItem() != ModItems.grindWheel) return;

                // Begin grinding;
                inventory.setStackInSlot(INPUT_SLOT, ItemUtil.consumeItem(input, 1));
                currentRecipe = recipe;
                rotation = 3;
                ModNetwork.network.sendToAllAround(new PacketUpdateGrindstone(this), new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64));
            }

        }
    }
    private void finishGrinding(){
        if(!world.isRemote){
            ItemStack outStack = inventory.getStackInSlot(OUTPUT_SLOT);
            ItemStack wheel = inventory.getStackInSlot(WHEEL_SLOT);
            if(currentRecipe != null) {
                inventory.setStackInSlot(WHEEL_SLOT, ItemUtil.damageItem(wheel,1));
                inventory.setStackInSlot(OUTPUT_SLOT, ItemUtil.mergeStacks(outStack, currentRecipe.getOutput()));
            }
            updateWheel();
        }
    }

    public void updateWheel(){
        if(!world.isRemote){
            ItemStack stack = inventory.getStackInSlot(WHEEL_SLOT);
            hasWheel = stack.getItem() == ModItems.grindWheel;
            if(!hasWheel){
                rotation = 0;
            }
            ModNetwork.network.sendToAllAround(new PacketUpdateGrindstone(this), new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64));
        }
    }

    public int getRotation(){ return rotation; }
    public void setRotation(int rotation){ this.rotation = rotation; }
    public boolean getHasWheel(){ return hasWheel; }
    public void setHasWheel(boolean hasWheel){ this.hasWheel = hasWheel; }

    @Override
    protected NBTTagCompound writeNBT(NBTTagCompound compound) {
        compound.setInteger("rotation", rotation);
        compound.setBoolean("has_wheel", hasWheel);
        return compound;
    }

    @Override
    public void readNBT(NBTTagCompound compound) {
        rotation = compound.getInteger("rotation");
        hasWheel = compound.getBoolean("has_wheel");
    }

    @Override
    public void onLoad() {
        if (world.isRemote) {
            ModNetwork.network.sendToServer(new PacketRequestGrindstone(this));
        }
        currentRecipe = GrindstoneRecipeHandler.getRecipe(inventory.getStackInSlot(INPUT_SLOT), false);
    }

    // FIELDS
    @Override
    public int getField(int id) {
        return rotation;
    }
    @Override
    public void setField(int id, int value) {
        rotation = (short) value;
    }
    @Override
    public int getFieldCount() {
        return 1;
    }
}
