package notreepunching.integration.jei.grindstone;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import notreepunching.integration.jei.JEIPluginHelper;
import notreepunching.item.ModItems;
import notreepunching.recipe.forge.ForgeRecipe;
import notreepunching.recipe.grindstone.GrindstoneRecipe;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static notreepunching.integration.jei.forge.ForgeRecipeCategory.LOC;

public class GrindstoneRecipeWrapper implements IRecipeWrapper {

    private final List<List<ItemStack>> input;
    private final List<List<ItemStack>> output;

    public GrindstoneRecipeWrapper(GrindstoneRecipe recipe){

        ImmutableList.Builder<List<ItemStack>> builder = ImmutableList.builder();

        // Add the ingredient
        if(!recipe.getOreInput().equals("") && OreDictionary.doesOreNameExist(recipe.getOreInput())) {
            builder.add(OreDictionary.getOres(recipe.getOreInput()));
        }else{
            builder.add(ImmutableList.of(recipe.getInput()));
        }
        builder.add(ImmutableList.of(new ItemStack(ModItems.grindWheel)));

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
