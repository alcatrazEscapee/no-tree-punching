package notreepunching.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import notreepunching.block.tile.TileEntityBlastFurnace;
import notreepunching.block.tile.inventory.SlotTEOutput;
import notreepunching.block.tile.inventory.SlotTERecipeInput;
import notreepunching.recipe.forge.BlastRecipeHandler;

import javax.annotation.Nonnull;

public class ContainerBlastFurnace extends ContainerBase<TileEntityBlastFurnace> {

    public ContainerBlastFurnace(InventoryPlayer playerInv, TileEntityBlastFurnace tile) {
        super(playerInv, tile);
    }

    @Override
    protected void addContainerSlots(TileEntityBlastFurnace tile) {
        IItemHandler inventory = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        addSlotToContainer(new SlotTERecipeInput(inventory, 0,71,26, tile, BlastRecipeHandler::isIngredient));
        addSlotToContainer(new SlotTEOutput(inventory, 1,127,35, tile));
    }

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
}
