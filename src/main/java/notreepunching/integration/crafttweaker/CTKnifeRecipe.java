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
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import notreepunching.recipe.knife.KnifeRecipe;
import notreepunching.recipe.knife.KnifeRecipeHandler;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.notreepunching.Knife")
public class CTKnifeRecipe {

    @ZenMethod
    public static void add(IItemStack input, IItemStack output){
        KnifeRecipe recipe = new KnifeRecipe(CTPluginHelper.toStack(input),
                CTPluginHelper.toStack(output));
        CraftTweakerAPI.apply(new Add(recipe));
    }

    @ZenMethod
    public static void remove(IItemStack input){
        ItemStack stack = CTPluginHelper.toStack(input);
        CraftTweakerAPI.apply(new Remove(stack));
    }

    private static class Add implements IAction {
        private final KnifeRecipe recipe;

        public Add(KnifeRecipe recipe) {
            this.recipe = recipe;
        }

        @Override
        public void apply() {
            KnifeRecipeHandler.addEntry(recipe, true);
        }

        @Override
        public String describe() {
            return "Adding Knife Recipe for " + recipe.getInput().getDisplayName();
        }

    }

    private static class Remove implements IAction {
        private final ItemStack stack;

        public Remove(ItemStack stack) {
            this.stack = stack;
        }

        @Override
        public void apply() {
            KnifeRecipeHandler.addEntry(new KnifeRecipe(stack, ItemStack.EMPTY), false);
        }

        @Override
        public String describe(){
            return "Removing Knife Recipe for" + stack.getDisplayName();
        }
    }
}
