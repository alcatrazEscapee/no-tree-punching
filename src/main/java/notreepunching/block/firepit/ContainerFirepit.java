package notreepunching.block.firepit;

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

public class ContainerFirepit extends Container {

    private int [] cachedFields;

    private TileEntityFirepit firepit;

    public ContainerFirepit(InventoryPlayer playerInv, TileEntityFirepit firepit) {
        IItemHandler inventory = firepit.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
        // Firepit Slots
        addSlotToContainer(new SlotFirepitFuel(inventory, 0,80,59,firepit));
        addSlotToContainer(new SlotFirepitInput(inventory, 1,58,23,firepit));
        addSlotToContainer(new SlotFirepitOutput(inventory, 2,102,23,firepit));

        this.firepit = firepit;

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
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    // index is the id of the slot shift-clicked
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        // Slot that was clicked
        Slot slot = inventorySlots.get(index);

        ItemStack itemstack = ItemStack.EMPTY;

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
            // TODO: Write custom transfer code here so that you can't shift click into the output slot, and it obeys fuel / normal priority rules
            if (!this.mergeItemStack(itemstack1, 0, containerSlots, false)) {
                return ItemStack.EMPTY;
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
        boolean fieldHasChanged [] = new boolean[firepit.getFieldCount()];
        if (cachedFields == null) {
            cachedFields = new int[firepit.getFieldCount()];
            allFieldsHaveChanged = true;
        }
        for (int i = 0; i < cachedFields.length; ++i) {
            if (allFieldsHaveChanged || cachedFields[i] != firepit.getField(i)) {
                cachedFields[i] = firepit.getField(i);
                fieldHasChanged[i] = true;
            }
        }

        // go through the list of listeners (players using this container) and update them if necessary
        for (IContainerListener listener : this.listeners) {
            for (int fieldID = 0; fieldID < firepit.getFieldCount(); ++fieldID) {
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
        firepit.setField(id, data);
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
        public boolean isItemValid(ItemStack stack) {
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
        public boolean isItemValid(ItemStack stack) {
            System.out.println("Dumping a "+stack.getItem().getUnlocalizedName()+" with meta "+stack.getMetadata());
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
        public boolean isItemValid(ItemStack stack) {
            return false;
        }
    }
}
