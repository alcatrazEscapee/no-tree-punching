package notreepunching.integration.crafttweaker;

import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;

public class CTPluginHelper {

    public static ItemStack toStack(IItemStack iStack) {
        if (iStack == null)
            return ItemStack.EMPTY;
        return (ItemStack) iStack.getInternal();
    }

}
