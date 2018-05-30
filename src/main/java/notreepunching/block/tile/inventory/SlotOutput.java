package notreepunching.block.tile.inventory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import notreepunching.block.tile.TileEntitySidedInventory;

import javax.annotation.Nonnull;

public class SlotOutput extends SlotItemHandler {

    private final TileEntitySidedInventory te;

    public SlotOutput(IItemHandler inventory, int idx, int x, int y, TileEntitySidedInventory te){
        super(inventory, idx,x,y);
        this.te = te;
    }

    @Override
    public void onSlotChanged() {
        te.setAndUpdateSlots(slotNumber);
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        return false;
    }
}
