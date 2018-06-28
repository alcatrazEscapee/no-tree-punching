/*
 *
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 *
 */

package notreepunching.client.container;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import notreepunching.block.tile.TileEntityWoodPile;
import notreepunching.block.tile.inventory.SlotTERecipeInput;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ContainerWoodPile extends ContainerBase<TileEntityWoodPile> {

    public ContainerWoodPile(InventoryPlayer playerInv, TileEntityWoodPile woodPile) {
        super(playerInv, woodPile);
    }

    @Override
    protected void addContainerSlots(TileEntityWoodPile tile) {
        IItemHandler inventory = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if(inventory != null) {
            addSlotToContainer(new SlotTERecipeInput(inventory, 0, 71, 23, tile, TileEntityWoodPile::isItemValid));
            addSlotToContainer(new SlotTERecipeInput(inventory, 1, 89, 23, tile, TileEntityWoodPile::isItemValid));
            addSlotToContainer(new SlotTERecipeInput(inventory, 2, 71, 41, tile, TileEntityWoodPile::isItemValid));
            addSlotToContainer(new SlotTERecipeInput(inventory, 3, 89, 41, tile, TileEntityWoodPile::isItemValid));
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
