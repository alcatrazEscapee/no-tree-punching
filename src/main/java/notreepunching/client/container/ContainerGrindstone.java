/*
 *
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 *
 */

package notreepunching.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import notreepunching.block.tile.TileEntityGrindstone;
import notreepunching.block.tile.inventory.SlotTEOutput;
import notreepunching.block.tile.inventory.SlotTERecipeInput;
import notreepunching.recipe.grindstone.GrindstoneRecipeHandler;

import javax.annotation.Nonnull;

public class ContainerGrindstone extends ContainerBase<TileEntityGrindstone> {

    public ContainerGrindstone(InventoryPlayer playerInv, TileEntityGrindstone grindstone) {
        super(playerInv, grindstone);
    }

    @Override
    protected void addContainerSlots(TileEntityGrindstone tile) {
        IItemHandler inventory = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        if (inventory != null) {
            addSlotToContainer(new SlotTERecipeInput(inventory, 0, 52, 44, tile, GrindstoneRecipeHandler::isIngredient));
            addSlotToContainer(new SlotTEOutput(inventory, 1, 108, 31, tile));
            addSlotToContainer(new SlotTERecipeInput(inventory, 2, 52, 18, tile, TileEntityGrindstone::isWheel));
        }
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
            tile.setAndUpdateSlots(index);
        }
        // Transfer into the container
        else {
            // Try wheel slot
            if(this.inventorySlots.get(2).isItemValid(itemstack)){
                if (!this.mergeItemStack(itemstack1, 2, 3, false)) {
                    return ItemStack.EMPTY;
                }
            } else { // Try input slot next
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
}
