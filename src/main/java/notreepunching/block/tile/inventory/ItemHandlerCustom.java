package notreepunching.block.tile.inventory;

import net.minecraftforge.items.ItemStackHandler;
import notreepunching.block.tile.TileEntitySidedInventory;

public class ItemHandlerCustom extends ItemStackHandler {

    private final TileEntitySidedInventory tile;

    public ItemHandlerCustom(TileEntitySidedInventory tile, int size){
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
