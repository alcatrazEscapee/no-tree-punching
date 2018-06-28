/*
 *
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 *
 */

package notreepunching.integration.jei.blast;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import notreepunching.integration.jei.JEIPluginHelper;
import notreepunching.recipe.forge.ForgeRecipe;

import javax.annotation.Nonnull;
import java.util.List;

import static notreepunching.integration.jei.blast.BlastRecipeCategory.LOC;

public class BlastRecipeWrapper implements IRecipeWrapper{

    private final List<List<ItemStack>> input;
    private final List<List<ItemStack>> output;
    private final int temperature;
    private final IDrawable drawableTemperature;

    public BlastRecipeWrapper(ForgeRecipe recipe){

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

        temperature = recipe.getTemp() / 50;
        drawableTemperature = JEIPluginHelper.helper.createDrawable(LOC, 142,30-temperature,10,temperature);
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setInputLists(ItemStack.class, input);
        ingredients.setOutputLists(ItemStack.class, output);
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        drawableTemperature.draw(minecraft,0, 35 - temperature);
    }
}
