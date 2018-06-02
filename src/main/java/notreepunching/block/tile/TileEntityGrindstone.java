package notreepunching.block.tile;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import notreepunching.block.tile.inventory.ItemHandlerWrapper;
import notreepunching.client.ModSounds;
import notreepunching.item.ModItems;
import notreepunching.network.*;
import notreepunching.recipe.grindstone.GrindstoneRecipe;
import notreepunching.recipe.grindstone.GrindstoneRecipeHandler;
import notreepunching.util.ItemUtil;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TileEntityGrindstone extends TileEntitySidedInventory implements ITickable, IHasFields {

    private int rotation = 0;
    private boolean hasWheel = false;
    private GrindstoneRecipe currentRecipe;

    private final byte INPUT_SLOT = 0;
    private final byte OUTPUT_SLOT = 1;
    private final byte WHEEL_SLOT = 2;

    private final ItemHandlerWrapper outputWrapper;
    private final ItemHandlerWrapper inputWrapper;

    public TileEntityGrindstone(){
        super(3);

        outputWrapper = new ItemHandlerWrapper(inventory, this);
        outputWrapper.addExtractSlot(OUTPUT_SLOT);

        inputWrapper = new ItemHandlerWrapper(inventory, this);
        inputWrapper.addInsertSlot(INPUT_SLOT, WHEEL_SLOT);
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
    public boolean isItemValid(int slot, ItemStack stack) {
        return slot == WHEEL_SLOT ? isWheel(stack) : super.isItemValid(slot, stack);
    }
    public static boolean isWheel(ItemStack stack){ return stack.getItem() == ModItems.grindWheel; }

    @Override
    public void setAndUpdateSlots(int slot){
        updateWheel();
        this.markDirty();
    }

    public boolean startGrinding() {
        if (!world.isRemote) {
            if(rotation >= 1) return false;

            ItemStack input = inventory.getStackInSlot(INPUT_SLOT);
            ItemStack output = inventory.getStackInSlot(OUTPUT_SLOT);

            GrindstoneRecipe recipe = GrindstoneRecipeHandler.getRecipe(input, false);
            if(recipe != null){
                if(!ItemUtil.canMergeStack(output,recipe.getOutput())) return false;

                // check if grind wheel is powerful enough
                ItemStack wheel = inventory.getStackInSlot(WHEEL_SLOT);
                if(wheel.getItem() != ModItems.grindWheel) return false;

                // Begin grinding;
                inventory.setStackInSlot(INPUT_SLOT, ItemUtil.consumeItem(input, 1));
                currentRecipe = recipe;
                rotation = 3;
                ModNetwork.network.sendToAllAround(new PacketUpdateGrindstone(this), new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64));
                world.playSound(null, pos, ModSounds.grindstone, SoundCategory.BLOCKS, 1.0F, 1.0F);
                return true;
            }
        }
        return false;
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

    private void updateWheel(){
        if(!world.isRemote){
            ItemStack stack = inventory.getStackInSlot(WHEEL_SLOT);
            hasWheel = stack.getItem() == ModItems.grindWheel;
            if(!hasWheel){
                rotation = 0;
            }
            ModNetwork.network.sendToAllAround(new PacketUpdateGrindstone(this), new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64));
        }
    }
    public void insertWheel(ItemStack stack){
        if(inventory.getStackInSlot(WHEEL_SLOT).isEmpty()) {
            inventory.setStackInSlot(WHEEL_SLOT, stack);
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

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing != null){
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing != null){
            if(facing == EnumFacing.DOWN){
                return (T) outputWrapper;
            }else{
                return (T) inputWrapper;
            }
        }
        return super.getCapability(capability, facing);
    }
}
