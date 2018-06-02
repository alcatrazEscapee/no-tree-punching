package notreepunching.integration.crafttweaker;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;

public class CTPluginHelper {

    protected static ItemStack toStack(IItemStack iStack) {
        if (iStack == null)
            return ItemStack.EMPTY;
        return (ItemStack) iStack.getInternal();
    }
    protected static ItemStack toStack(IIngredient ingredient) {
        if (ingredient == null)
            return ItemStack.EMPTY;
        return (ItemStack) ingredient.getInternal();
    }

}
