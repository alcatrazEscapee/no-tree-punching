package notreepunching.block.tile.inventory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import notreepunching.block.tile.TileEntitySidedInventory;

import javax.annotation.Nonnull;
import java.util.function.Function;

public class SlotTERecipeInput extends SlotItemHandler {

    private final TileEntitySidedInventory te;
    private final Function<ItemStack, Boolean> f;

    public SlotTERecipeInput(@Nonnull IItemHandler inventory, int idx, int x, int y, @Nonnull TileEntitySidedInventory te, @Nonnull Function<ItemStack, Boolean> f){
        super(inventory, idx,x,y);
        this.te = te;
        this.f = f;
    }

    @Override
    public void onSlotChanged() {
        te.setAndUpdateSlots(getSlotIndex());
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        return f.apply(stack);
    }

    @Override
    public int getSlotStackLimit() {
        return te.getSlotLimit(getSlotIndex());
    }
}
