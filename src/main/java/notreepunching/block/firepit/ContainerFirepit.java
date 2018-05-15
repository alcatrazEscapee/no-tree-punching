package notreepunching.block.firepit;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import notreepunching.block.ContainerBase;

import javax.annotation.Nonnull;

public class ContainerFirepit extends ContainerBase<TileEntityFirepit> {

    public ContainerFirepit(InventoryPlayer playerInv, TileEntityFirepit firepit) {
        super(playerInv, firepit);

        IItemHandler inventory = firepit.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
        // Firepit Slots
        addSlotToContainer(new SlotFirepitFuel(inventory, 0,80,59,firepit));
        addSlotToContainer(new SlotFirepitInput(inventory, 1,51,23,firepit));
        addSlotToContainer(new SlotFirepitOutput(inventory, 2,109,23,firepit));
    }

    // index is the id of the slot shift-clicked
    @Override
    @Nonnull
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        // Slot that was clicked
        Slot slot = inventorySlots.get(index);

        ItemStack itemstack;

        if(slot == null || !slot.getHasStack()) { return ItemStack.EMPTY; }

        ItemStack itemstack1 = slot.getStack();
        itemstack = itemstack1.copy();

        // Begin custom transfer code here

        int containerSlots = inventorySlots.size() - player.inventory.mainInventory.size(); // number of slots in the container

        if (index < containerSlots) {
            // Transfer out of the container
            if (!this.mergeItemStack(itemstack1, containerSlots, inventorySlots.size(), true)) {
                // Don't transfer anything
                return ItemStack.EMPTY;
            }
        }
        // Transfer into the container
        else {
            // Try fuel slot first (most specific)
            if(this.inventorySlots.get(0).isItemValid(itemstack)){
                if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            }else{
                // Try input slot next (least specific)
                if (!this.mergeItemStack(itemstack1, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
                // don't try to merge into the output slot
            }
        }

        // Required
        if (itemstack1.getCount() == 0) {
            slot.putStack(ItemStack.EMPTY);
        } else {
            slot.onSlotChanged();
        }
        if (itemstack1.getCount() == itemstack.getCount()) {
            return ItemStack.EMPTY;
        }
        slot.onTake(player, itemstack1);
        return itemstack;
    }

    // ***************** Slot Classes ******************** //

    class SlotFirepitFuel extends SlotItemHandler{

        private TileEntityFirepit te;

        private SlotFirepitFuel(IItemHandler inventory, int idx, int x, int y, final TileEntityFirepit firepit){
            super(inventory, idx,x,y);
            this.te = firepit;
        }

        @Override
        public void onSlotChanged() {
            te.markDirty();
        }

        @Override
        public boolean isItemValid(@Nonnull ItemStack stack) {
            return TileEntityFirepit.isItemValidFuel(stack);
        }
    }

    class SlotFirepitInput extends SlotItemHandler{
        private TileEntityFirepit te;

        private SlotFirepitInput(IItemHandler inventory, int idx, int x, int y, final TileEntityFirepit firepit){
            super(inventory, idx,x,y);
            this.te = firepit;
        }

        @Override
        public void onSlotChanged() {
            te.markDirty();
            te.resetCookTimer();
        }

        @Override
        public boolean isItemValid(@Nonnull ItemStack stack) {
            return TileEntityFirepit.isItemValidInput(stack);
        }
    }

    class SlotFirepitOutput extends SlotItemHandler{
        private TileEntityFirepit te;

        private SlotFirepitOutput(IItemHandler inventory, int idx, int x, int y, final TileEntityFirepit firepit){
            super(inventory, idx,x,y);
            this.te = firepit;
        }

        @Override
        public void onSlotChanged() {
            te.markDirty();
        }

        @Override
        public boolean isItemValid(@Nonnull ItemStack stack) {
            return false;
        }
    }
}
