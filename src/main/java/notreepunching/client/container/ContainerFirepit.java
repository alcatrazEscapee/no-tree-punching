package notreepunching.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import notreepunching.block.tile.TileEntityFirepit;
import notreepunching.block.tile.inventory.SlotOutput;
import notreepunching.block.tile.inventory.SlotRecipeInput;
import notreepunching.recipe.firepit.FirepitRecipeHandler;

import javax.annotation.Nonnull;

public class ContainerFirepit extends ContainerBase<TileEntityFirepit> {

    public ContainerFirepit(InventoryPlayer playerInv, TileEntityFirepit firepit) {
        super(playerInv, firepit);
    }

    @Override
    protected void addContainerSlots(TileEntityFirepit tile) {
        IItemHandler inventory = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        addSlotToContainer(new SlotRecipeInput(inventory, 0,80,59, tile, TileEntityFirepit::isItemValidFuel)); // Fuel slot
        addSlotToContainer(new SlotRecipeInput(inventory, 1,52,23, tile, FirepitRecipeHandler::isRecipe)); // Input slot
        addSlotToContainer(new SlotOutput(inventory, 2,108,23, tile));
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
}
