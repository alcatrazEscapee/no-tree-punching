package notreepunching.block.tile.inventory;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import notreepunching.block.tile.TileEntitySidedInventory;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ItemHandlerWrapper extends ItemStackHandler {

    private final ItemStackHandler internalInventory;
    private final TileEntitySidedInventory tile;

    private boolean[] extractSlots;
    private boolean[] insertSlots;

    public ItemHandlerWrapper(ItemStackHandler inventory, TileEntitySidedInventory tile){
        super();

        this.internalInventory = inventory;
        this.tile = tile;

        extractSlots = new boolean[inventory.getSlots()];
        insertSlots = new boolean[inventory.getSlots()];
    }

    public void addInsertSlot(int... slot){
        for(int s : slot) {
            insertSlots[s] = true;
        }
    }
    public void addExtractSlot(int... slot) {
        for (int s : slot) {
            extractSlots[s] = true;
        }
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        internalInventory.setStackInSlot(slot, stack);
    }

    @Override
    public int getSlots() {
        return internalInventory.getSlots();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return internalInventory.getStackInSlot(slot);
    }

    // Return stack to prevent insertion
    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if(insertSlots[slot] && tile.isItemValid(slot, stack)) {
            return internalInventory.insertItem(slot, stack, simulate);
        }
        return stack;
    }

    // Return ItemStack.EMPTY to prevent insertion
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if(extractSlots[slot]) {
            return internalInventory.extractItem(slot, amount, simulate);
        }
        return ItemStack.EMPTY;
    }
}
