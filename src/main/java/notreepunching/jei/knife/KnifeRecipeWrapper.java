package notreepunching.jei.knife;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import notreepunching.item.ModItems;
import notreepunching.recipe.CuttingRecipe;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class KnifeRecipeWrapper implements IRecipeWrapper {

    private final List<List<ItemStack>> input;
    private final List<List<ItemStack>> output;

    public KnifeRecipeWrapper(CuttingRecipe recipe){

        ImmutableList.Builder<List<ItemStack>> builder = ImmutableList.builder();

        // Add the main ingredient and knife items
        builder.add(ImmutableList.of(recipe.getInput()));
        builder.add(ModItems.listAllKnives());

        // Set the input
        input = builder.build();

        // Reset builder and add output
        builder = ImmutableList.builder();
        builder.add(recipe.getOutput());

        // Set the output
        output = builder.build();

        /*input = new ArrayList<>();
        input.add(recipe.getInput());
        input.add(new ItemStack(ModItems.stoneKnife));
        output = recipe.getSingleOutput();*/

    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setInputLists(ItemStack.class, input);
        ingredients.setOutputLists(ItemStack.class, output);
    }
}
