package notreepunching.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import notreepunching.block.tile.TileEntityForge;
import notreepunching.client.container.ContainerBase;
import notreepunching.recipe.forge.ForgeRecipeHandler;

import javax.annotation.Nonnull;

public class ContainerForge extends ContainerBase<TileEntityForge> {

    public ContainerForge(InventoryPlayer playerInv, TileEntityForge forge) {
        super(playerInv, forge);
    }

    @Override
    protected void addContainerSlots(TileEntityForge tile) {
        IItemHandler inventory = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);

        addSlotToContainer(new SlotForgeInput(inventory, 0,52,23, tile));
        addSlotToContainer(new SlotForgeOutput(inventory, 1,108,23, tile));
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
        public boolean isItemValid(@Nonnull ItemStack stack) {
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
        public boolean isItemValid(@Nonnull ItemStack stack) {
            return false;
        }
    }
}
