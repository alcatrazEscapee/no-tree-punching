package notreepunching.block.forge;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import notreepunching.block.firepit.TileEntityFirepit;
import notreepunching.recipe.forge.ForgeRecipeHandler;

import javax.annotation.Nonnull;

public class ContainerForge extends Container {

    private int [] cachedFields;

    private TileEntityForge te;

    public ContainerForge(InventoryPlayer playerInv, TileEntityForge te) {
        IItemHandler inventory = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
        // Firepit Slots
        addSlotToContainer(new SlotForgeInput(inventory, 0,52,23,te));
        addSlotToContainer(new SlotForgeOutput(inventory, 1,108,23,te));

        this.te = te;

        // Add Player Inventory Slots
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; k++) {
            addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer player) {
        return true;
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
            // Try input slot
            if(this.inventorySlots.get(0).isItemValid(itemstack)){
                if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
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


    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        boolean allFieldsHaveChanged = false;
        boolean fieldHasChanged [] = new boolean[te.getFieldCount()];
        if (cachedFields == null) {
            cachedFields = new int[te.getFieldCount()];
            allFieldsHaveChanged = true;
        }
        for (int i = 0; i < cachedFields.length; ++i) {
            if (allFieldsHaveChanged || cachedFields[i] != te.getField(i)) {
                cachedFields[i] = te.getField(i);
                fieldHasChanged[i] = true;
            }
        }

        // go through the list of listeners (players using this container) and update them if necessary
        for (IContainerListener listener : this.listeners) {
            for (int fieldID = 0; fieldID < te.getFieldCount(); ++fieldID) {
                if (fieldHasChanged[fieldID]) {
                    // Note that although sendWindowProperty takes 2 ints on a server these are truncated to shorts
                    listener.sendWindowProperty(this, fieldID, cachedFields[fieldID]);
                }
            }
        }
    }

    // Called when a progress bar update is received from the server. The two values (id and data) are the same two
    // values given to sendWindowProperty.  In this case we are using fields so we just pass them to the tileEntity.
    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int id, int data) {
        te.setField(id, data);
    }


    // ***************** Slot Classes ******************** //

    class SlotForgeInput extends SlotItemHandler{
        private TileEntityForge te;

        private SlotForgeInput(IItemHandler inventory, int idx, int x, int y, final TileEntityForge te){
            super(inventory, idx,x,y);
            this.te = te;
        }

        @Override
        public void onSlotChanged() {
            te.markDirty();
            //te.resetCookTimer();
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return ForgeRecipeHandler.isIngredient(stack);
        }
    }

    class SlotForgeOutput extends SlotItemHandler{
        private TileEntityForge te;

        private SlotForgeOutput(IItemHandler inventory, int idx, int x, int y, final TileEntityForge te){
            super(inventory, idx,x,y);
            this.te = te;
        }

        @Override
        public void onSlotChanged() {
            te.markDirty();
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return false;
        }
    }
}
