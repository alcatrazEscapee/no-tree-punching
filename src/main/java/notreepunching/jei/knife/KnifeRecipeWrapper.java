package notreepunching.jei.knife;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import notreepunching.item.ModItems;
import notreepunching.recipe.CuttingRecipe;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class KnifeRecipeWrapper implements IRecipeWrapper {

    private final List<ItemStack> input;
    private final ItemStack output;

    public KnifeRecipeWrapper(CuttingRecipe recipe){
        input = new ArrayList<>();
        input.add(recipe.ingredient);
        input.add(new ItemStack(ModItems.stoneKnife));
        output = recipe.drops[0];
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setInputs(ItemStack.class, input);
        ingredients.setOutput(ItemStack.class, output);
    }
}
