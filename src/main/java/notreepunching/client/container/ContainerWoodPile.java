package notreepunching.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import notreepunching.block.tile.TileEntityWoodPile;
import notreepunching.block.tile.inventory.SlotRecipeInput;

import javax.annotation.Nonnull;

public class ContainerWoodPile extends Container {

    private TileEntityWoodPile tile;

    public ContainerWoodPile(InventoryPlayer playerInv, TileEntityWoodPile woodPile) {
        IItemHandler inventory = woodPile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        // Container Slots
        addSlotToContainer(new SlotRecipeInput(inventory, 0, 71, 23, woodPile, TileEntityWoodPile::isItemValid));
        addSlotToContainer(new SlotRecipeInput(inventory, 1, 89, 23, woodPile, TileEntityWoodPile::isItemValid));
        addSlotToContainer(new SlotRecipeInput(inventory, 2, 71, 41, woodPile, TileEntityWoodPile::isItemValid));
        addSlotToContainer(new SlotRecipeInput(inventory, 3, 89, 41, woodPile, TileEntityWoodPile::isItemValid));

        tile = woodPile;

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
        if(this.tile.burning){ return false; }
        return this.tile.countLogs() > 0;
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
            tile.setAndUpdateSlots(index);
        }
        // Transfer into the container
        else {
            if (!this.mergeItemStack(itemstack1, 0, 4, false)) {
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
}
