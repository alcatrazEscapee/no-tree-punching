/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.integration.guide.utils;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import amerifrance.guideapi.api.IRecipeRenderer;
import amerifrance.guideapi.page.PageIRecipe;
import amerifrance.guideapi.page.reciperenderer.ShapedOreRecipeRenderer;
import amerifrance.guideapi.page.reciperenderer.ShapedRecipesRenderer;
import amerifrance.guideapi.page.reciperenderer.ShapelessOreRecipeRenderer;
import amerifrance.guideapi.page.reciperenderer.ShapelessRecipesRenderer;
import com.alcatrazescapee.notreepunching.NoTreePunching;

public class PageIRecipeLazy extends PageIRecipe implements ILazyLoader
{
    private static IRecipeRenderer getRenderer(IRecipe recipe)
    {
        if (recipe instanceof ShapedRecipes)
        {
            return new ShapedRecipesRenderer((ShapedRecipes) recipe);
        }
        else if (recipe instanceof ShapelessRecipes)
        {
            return new ShapelessRecipesRenderer((ShapelessRecipes) recipe);
        }
        else if (recipe instanceof ShapedOreRecipe)
        {
            return new ShapedOreRecipeRenderer((ShapedOreRecipe) recipe);
        }
        else if (recipe instanceof ShapelessOreRecipe)
        {
            return new ShapelessOreRecipeRenderer((ShapelessOreRecipe) recipe);
        }
        else
        {
            NoTreePunching.getLog().info("Couldn't get renderer!!! Problem!!!");
            return null;
        }
    }

    private final ResourceLocation recipeLookup;

    public PageIRecipeLazy(ResourceLocation recipeLookup)
    {
        super(null, null);
        this.recipeLookup = recipeLookup;
    }

    public void loadPost()
    {
        // YOU WILL OBEY ME NOW!!!
        recipe = ForgeRegistries.RECIPES.getValue(recipeLookup);
        iRecipeRenderer = getRenderer(recipe);
        isValid = recipe != null && iRecipeRenderer != null;
    }
}
