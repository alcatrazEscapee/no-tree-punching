package notreepunching.integration.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.oredict.IOreDictEntry;
import net.minecraft.item.ItemStack;
import notreepunching.recipe.forge.BlastRecipeHandler;
import notreepunching.recipe.forge.ForgeRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.notreepunching.BlastFurnace")
public class CTBlastRecipe {

    @ZenMethod
    public static void add(IIngredient input, IItemStack output, int mininumTemperature){
        ForgeRecipe recipe;
        if(input instanceof IOreDictEntry){
            IOreDictEntry ore = (IOreDictEntry) input;
            recipe = new ForgeRecipe(CTPluginHelper.toStack(output),ore.getName(), ore.getAmount(), mininumTemperature);
            CraftTweakerAPI.apply(new Add(recipe));
        }else if(input instanceof IItemStack){
            recipe = new ForgeRecipe(CTPluginHelper.toStack(output), CTPluginHelper.toStack(input), mininumTemperature);
            CraftTweakerAPI.apply(new Add(recipe));
        }
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
            BlastRecipeHandler.addEntry(recipe, true);
        }

        @Override
        public String describe() {
            return "Adding Blast Furnace Recipe for " + recipe.getStack().getDisplayName();
        }

    }

    private static class Remove implements IAction {
        private final ItemStack stack;

        public Remove(ItemStack stack) {
            this.stack = stack;
        }

        @Override
        public void apply() {
            BlastRecipeHandler.addEntry(new ForgeRecipe(stack, ItemStack.EMPTY, 0), false);
        }

        @Override
        public String describe(){
            return "Removing Blast Furnace Recipe for" + stack.getDisplayName();
        }
    }
}
