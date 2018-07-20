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
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import notreepunching.NoTreePunching;
import notreepunching.block.tile.inventory.ItemStackInventoryWrapper;
import notreepunching.block.tile.inventory.SlotItemInput;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ContainerSmallVessel extends Container {

    private final EntityPlayer player;
    private final ItemStack stack;
    private final int itemIndex;

    public ContainerSmallVessel(InventoryPlayer playerInv, ItemStack stack){
        this.player = playerInv.player;
        this.stack = stack;
        this.itemIndex = playerInv.currentItem + 31;

        addContainerSlots(stack);
        addPlayerInventorySlots(playerInv);
    }

    private void addContainerSlots(ItemStack stack){
        IItemHandler inventory = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if(inventory != null) {
            addSlotToContainer(new SlotItemInput(inventory, 0, 71, 23));
            addSlotToContainer(new SlotItemInput(inventory, 1, 89, 23));
            addSlotToContainer(new SlotItemInput(inventory, 2, 71, 41));
            addSlotToContainer(new SlotItemInput(inventory, 3, 89, 41));
        }

    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return true;
    }

    private void addPlayerInventorySlots(InventoryPlayer playerInv){
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
    public void onContainerClosed(EntityPlayer player){
        NoTreePunching.log.info("Call Trace 2");
        IItemHandler cap = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (cap != null && cap instanceof ItemStackInventoryWrapper) {
            ((ItemStackInventoryWrapper) cap).saveNBT(stack);
        }
        super.onContainerClosed(player);
    }

    @Override
    @Nonnull
    public ItemStack slotClick(int slotID, int dragType, ClickType clickType, EntityPlayer player) {
        NoTreePunching.log.info("Slot Click " + slotID + " | " + dragType + " | " + itemIndex + " | " + clickType);
        if ((clickType == ClickType.QUICK_MOVE || clickType == ClickType.PICKUP || clickType == ClickType.SWAP) && slotID == itemIndex) {
            return ItemStack.EMPTY;
        } else {
            return super.slotClick(slotID, dragType, clickType, player);
        }
    }

    @Override
    @Nonnull
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        // Slot that was clicked
        Slot slot = inventorySlots.get(index);

        ItemStack itemstack;

        if (slot == null || !slot.getHasStack())
            return ItemStack.EMPTY;

        if (index == itemIndex)
            return ItemStack.EMPTY;

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
            //tile.setAndUpdateSlots(index);
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
