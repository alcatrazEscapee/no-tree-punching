package notreepunching.block.tile.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemStackInventoryWrapper implements ICapabilityProvider {

    protected ItemStack stack;
    private final ItemHandlerInventoryItem inventory;

    public ItemStackInventoryWrapper(ItemStack stack, NBTTagCompound compound, int size){
        super();
        this.stack = stack;

        inventory = new ItemHandlerInventoryItem(size);

        readNBT(compound);
    }

    private void readNBT(NBTTagCompound compound){
        if(compound != null) {
            inventory.deserializeNBT(compound.getCompoundTag("inventory"));
        }
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing enumFacing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            return (T) inventory;
        }
        return null;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing enumFacing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }
}
