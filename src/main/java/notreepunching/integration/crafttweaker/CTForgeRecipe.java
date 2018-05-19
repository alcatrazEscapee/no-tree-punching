package notreepunching.integration.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import notreepunching.recipe.forge.ForgeRecipe;
import notreepunching.recipe.forge.ForgeRecipeHandler;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.notreepunching.Forge")
public class CTForgeRecipe {

    @ZenMethod
    public static void add(IItemStack input, IItemStack output, int mininumTemperature){
        ForgeRecipe recipe = new ForgeRecipe(CTPluginHelper.toStack(output),
                CTPluginHelper.toStack(input), mininumTemperature);
        CraftTweakerAPI.apply(new Add(recipe));
    }

    @ZenMethod
    public static void remove(IItemStack output){
        ItemStack stack = CTPluginHelper.toStack(output);
        CraftTweakerAPI.apply(new Remove(stack));
    }

    private static class Add implements IAction {
        private final ForgeRecipe recipe;

        public Add(ForgeRecipe recipe) {
            this.recipe = recipe;
        }

        @Override
        public void apply() {
            ForgeRecipeHandler.addRecipe(recipe);
        }

        @Override
        public String describe() {
            return "Adding Forge Recipe for " + recipe.getStack().getDisplayName();
        }

    }

    private static class Remove implements IAction {
        private final ItemStack stack;

        public Remove(ItemStack stack) {
            this.stack = stack;
        }

        @Override
        public void apply() {
            ForgeRecipeHandler.removeRecipe(stack);
        }

        @Override
        public String describe(){
            return "Removing Forge Recipe for" + stack.getDisplayName();
        }
    }
}
