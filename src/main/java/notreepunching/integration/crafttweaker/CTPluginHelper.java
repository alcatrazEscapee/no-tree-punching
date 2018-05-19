package notreepunching.integration.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;

public class CTPluginHelper {

    public static void register(){

        //CraftTweakerAPI.registerClass(CTForgeRecipe.class);
    }

    public static ItemStack toStack(IItemStack iStack) {
        if (iStack == null)
            return ItemStack.EMPTY;
        return (ItemStack) iStack.getInternal();
    }
}
