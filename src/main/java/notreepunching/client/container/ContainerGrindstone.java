package notreepunching.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import notreepunching.block.tile.TileEntityGrindstone;
import notreepunching.item.ModItems;
import notreepunching.network.ModNetwork;
import notreepunching.network.PacketUpdateGrindstone;
import notreepunching.recipe.grindstone.GrindstoneRecipeHandler;

import javax.annotation.Nonnull;

public class ContainerGrindstone extends ContainerBase<TileEntityGrindstone> {

    public ContainerGrindstone(InventoryPlayer playerInv, TileEntityGrindstone grindstone) {
        super(playerInv, grindstone);
    }

    @Override
    protected void addContainerSlots(TileEntityGrindstone tile) {
        IItemHandler inventory = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        addSlotToContainer(new SlotInput(inventory, 0,52,44, tile));
        addSlotToContainer(new SlotOutput(inventory, 1,108,31, tile));
        addSlotToContainer(new SlotWheel(inventory, 2,52,18, tile));
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
            // This is the only circumstance when the slot doesn't send this itself
            tile.setAndUpdateSlots();
        }
        // Transfer into the container
        else {
            // Try wheel slot
            if(this.inventorySlots.get(2).isItemValid(itemstack)){
                if (!this.mergeItemStack(itemstack1, 2, 3, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else{ // Try input slot next
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

    // ***************** Slot Classes ******************** //

    class SlotInput extends SlotItemHandler {
        private TileEntityGrindstone te;

        private SlotInput(IItemHandler inventory, int idx, int x, int y, final TileEntityGrindstone te){
            super(inventory, idx,x,y);
            this.te = te;
        }

        @Override
        public void onSlotChanged() {
            te.markDirty();
        }

        @Override
        public boolean isItemValid(@Nonnull ItemStack stack) {
            return GrindstoneRecipeHandler.isIngredient(stack);
        }
    }

    class SlotOutput extends SlotItemHandler{
        private TileEntityGrindstone te;

        private SlotOutput(IItemHandler inventory, int idx, int x, int y, final TileEntityGrindstone te){
            super(inventory, idx,x,y);
            this.te = te;
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

    class SlotWheel extends SlotItemHandler{
        private TileEntityGrindstone te;

        private SlotWheel(IItemHandler inventory, int idx, int x, int y, final TileEntityGrindstone te){
            super(inventory, idx,x,y);
            this.te = te;
        }

        @Override
        public void onSlotChanged() {
            te.markDirty();
            te.updateWheel();
        }

        @Override
        public boolean isItemValid(@Nonnull ItemStack stack) {
            return stack.getItem() == ModItems.grindWheel;
        }
    }
}
