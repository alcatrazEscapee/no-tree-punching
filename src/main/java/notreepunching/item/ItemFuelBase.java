package notreepunching.item;

import net.minecraft.item.ItemStack;

public class ItemFuelBase extends ItemBase {

    private int burnTime;

    public ItemFuelBase(String name, int burnTime){
        super(name);

        this.burnTime = burnTime;
    }

    @Override
    public int getItemBurnTime(ItemStack stack){
        return burnTime;
    }
}
