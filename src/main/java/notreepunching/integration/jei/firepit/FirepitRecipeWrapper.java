package notreepunching.integration.jei.firepit;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import notreepunching.recipe.firepit.FirepitRecipe;

import javax.annotation.Nonnull;
import java.util.List;

public class FirepitRecipeWrapper implements IRecipeWrapper {

    private final List<List<ItemStack>> input;
    private final List<List<ItemStack>> output;

    public FirepitRecipeWrapper(FirepitRecipe recipe){

        ImmutableList.Builder<List<ItemStack>> builder = ImmutableList.builder();

        // Add the ingredient
        builder.add(ImmutableList.of(recipe.getInput()));

        // Set the input
        input = builder.build();

        // Reset builder and add output
        builder = ImmutableList.builder();
        builder.add(ImmutableList.of(recipe.getOutput()));

        // Set the output
        output = builder.build();

    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setInputLists(ItemStack.class, input);
        ingredients.setOutputLists(ItemStack.class, output);
    }
}
