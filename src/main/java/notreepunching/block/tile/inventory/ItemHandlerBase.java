package notreepunching.block.tile.inventory;

import net.minecraftforge.items.ItemStackHandler;
import notreepunching.block.tile.TileEntitySidedInventory;

public class ItemHandlerBase extends ItemStackHandler {

    private final TileEntitySidedInventory tile;

    public ItemHandlerBase(TileEntitySidedInventory tile, int size){
        super(size);

        this.tile = tile;
    }

    @Override
    public int getSlotLimit(int slot) {
        return tile.getSlotLimit(slot);
    }

    @Override
    protected void onContentsChanged(int slot) {
        tile.setAndUpdateSlots(slot);
    }
}
