package notreepunching.jei.forge;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import notreepunching.recipe.forge.ForgeRecipe;

import javax.annotation.Nonnull;
import java.util.List;

public class ForgeRecipeWrapper implements IRecipeWrapper {

    private final List<List<ItemStack>> input;
    private final List<List<ItemStack>> output;
    private final int temperature;

    public ForgeRecipeWrapper(ForgeRecipe recipe){

        ImmutableList.Builder<List<ItemStack>> builder = ImmutableList.builder();

        // Add the ingredient
        if(!recipe.getOre().equals("") && OreDictionary.doesOreNameExist(recipe.getOre())) {
            builder.add(OreDictionary.getOres(recipe.getOre()));
        }else{
            builder.add(ImmutableList.of(recipe.getStack()));
        }

        // Set the input
        input = builder.build();

        // Reset builder and add output
        builder = ImmutableList.builder();
        builder.add(ImmutableList.of(recipe.getOutput()));

        // Set the output
        output = builder.build();

        temperature = recipe.getTemp();
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setInputLists(ItemStack.class, input);
        ingredients.setOutputLists(ItemStack.class, output);
    }

    public int getTemperature(){ return temperature; }
}
