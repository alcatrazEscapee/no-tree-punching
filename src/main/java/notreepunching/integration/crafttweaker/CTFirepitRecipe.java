package notreepunching.integration.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import notreepunching.recipe.firepit.FirepitRecipe;
import notreepunching.recipe.firepit.FirepitRecipeHandler;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.notreepunching.Firepit")
public class CTFirepitRecipe {

    @ZenMethod
    public static void add(IItemStack input, IItemStack output){
        FirepitRecipe recipe = new FirepitRecipe(CTPluginHelper.toStack(input),
                CTPluginHelper.toStack(output));
        CraftTweakerAPI.apply(new CTFirepitRecipe.Add(recipe));
    }

    @ZenMethod
    public static void remove(IItemStack output){
        ItemStack stack = CTPluginHelper.toStack(output);
        CraftTweakerAPI.apply(new CTFirepitRecipe.Remove(stack));
    }

    private static class Add implements IAction {
        private final FirepitRecipe recipe;

        public Add(FirepitRecipe recipe) {
            this.recipe = recipe;
        }

        @Override
        public void apply() {
            FirepitRecipeHandler.addRecipe(recipe);
        }

        @Override
        public String describe() {
            return "Adding Firepit Recipe for " + recipe.getInput().getDisplayName();
        }

    }

    private static class Remove implements IAction {
        private final ItemStack stack;

        public Remove(ItemStack stack) {
            this.stack = stack;
        }

        @Override
        public void apply() {
            FirepitRecipeHandler.removeRecipe(stack);
        }

        @Override
        public String describe(){
            return "Removing Firepit Recipe for" + stack.getDisplayName();
        }
    }


}
