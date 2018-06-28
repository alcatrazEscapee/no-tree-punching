/*
 *
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 *
 */

package notreepunching.integration.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import notreepunching.recipe.knife.KnifeRecipeHandler;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.notreepunching.NoTreePunching")
public class CTPluginHelper {

    // Helper methods
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

    // Craft Tweaker Util Methods

    @ZenMethod
    public static void addKnifeGrassDrop(IItemStack stack){
        CraftTweakerAPI.apply(new Add(toStack(stack)));
    }

    private static class Add implements IAction {
        private final ItemStack stack;

        public Add(ItemStack stack) {
            this.stack = stack;
        }

        @Override
        public void apply() {
            KnifeRecipeHandler.addGrassDrop(stack);
        }

        @Override
        public String describe() {
            return "Adding Knife grass drop for " + stack.getDisplayName();
        }

    }

}
