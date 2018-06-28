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
import crafttweaker.api.oredict.IOreDictEntry;
import net.minecraft.item.ItemStack;
import notreepunching.recipe.grindstone.GrindstoneRecipe;
import notreepunching.recipe.grindstone.GrindstoneRecipeHandler;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.notreepunching.Grindstone")
public class CTGrindstoneRecipe {

    @ZenMethod
    public static void add(IIngredient input, IItemStack output){
        GrindstoneRecipe recipe;
        if(input instanceof IOreDictEntry){
            IOreDictEntry ore = (IOreDictEntry) input;
            recipe = new GrindstoneRecipe(CTPluginHelper.toStack(output),ore.getName(), ore.getAmount());
            CraftTweakerAPI.apply(new Add(recipe));
        }else if(input instanceof IItemStack){
            recipe = new GrindstoneRecipe(CTPluginHelper.toStack(output), CTPluginHelper.toStack(input));
            CraftTweakerAPI.apply(new Add(recipe));
        }
    }

    @ZenMethod
    public static void remove(IItemStack output){
        ItemStack stack = CTPluginHelper.toStack(output);
        CraftTweakerAPI.apply(new Remove(stack));
    }

    private static class Add implements IAction {
        private final GrindstoneRecipe recipe;

        public Add(GrindstoneRecipe recipe) {
            this.recipe = recipe;
        }

        @Override
        public void apply() {
            GrindstoneRecipeHandler.addEntry(recipe, true);
        }

        @Override
        public String describe() {
            return "Adding Grindstone Recipe for " + recipe.getOutput().getDisplayName();
        }

    }

    private static class Remove implements IAction {
        private final ItemStack stack;

        public Remove(ItemStack stack) {
            this.stack = stack;
        }

        @Override
        public void apply() {
            GrindstoneRecipeHandler.addEntry(new GrindstoneRecipe(stack, ItemStack.EMPTY), false);
        }

        @Override
        public String describe(){
            return "Removing Grindstone Recipe for" + stack.getDisplayName();
        }
    }
}
