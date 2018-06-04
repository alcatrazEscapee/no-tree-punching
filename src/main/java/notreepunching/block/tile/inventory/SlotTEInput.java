package notreepunching.block.tile.inventory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import notreepunching.block.tile.TileEntitySidedInventory;

import javax.annotation.Nonnull;

public class SlotTEInput extends SlotItemHandler {

    private final TileEntitySidedInventory te;

    public SlotTEInput(@Nonnull IItemHandler inventory, int idx, int x, int y, @Nonnull TileEntitySidedInventory te){
        super(inventory, idx,x,y);
        this.te = te;
    }

    @Override
    public void onSlotChanged() {
        te.setAndUpdateSlots(getSlotIndex());
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public int getSlotStackLimit() {
        return te.getSlotLimit(getSlotIndex());
    }
}
