/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Copyright (c) 2019. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.integration.jei;

/*
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import com.alcatrazescapee.notreepunching.NoTreePunching;
import com.alcatrazescapee.notreepunching.common.recipe.KnifeRecipe;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;
import static com.alcatrazescapee.notreepunching.integration.jei.JeiPlugin.KNIFE_UID;

*/ // todo: this
public class KnifeRecipeCategory {} /* implements IRecipeCategory<KnifeRecipeCategory.Wrapper>
{
    private static final String TRANSLATION_KEY = "jei.category.knife_recipe";
    private static final ResourceLocation GUI_LOCATION = new ResourceLocation(MOD_ID, "textures/jei/knife.png");

    private final IDrawable background;
    private final IDrawable icon;

    public KnifeRecipeCategory(IGuiHelper guiHelper)
    {
        background = guiHelper.createDrawable(GUI_LOCATION, 0, 0, 135, 18);
        icon = guiHelper.createDrawable(GUI_LOCATION, 135, 0, 16, 16);
    }

    @Nonnull
    @Override
    public String getUid()
    {
        return KNIFE_UID;
    }

    @Nonnull
    @Override
    @SideOnly(Side.CLIENT)
    public String getTitle()
    {
        return I18n.format(TRANSLATION_KEY);
    }

    @Nonnull
    @Override
    public String getModName()
    {
        return NoTreePunching.MOD_NAME;
    }

    @Nonnull
    @Override
    public IDrawable getBackground()
    {
        return background;
    }

    @Override
    public IDrawable getIcon()
    {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, Wrapper wrapper, IIngredients ingredients)
    {
        int index = 0;
        recipeLayout.getItemStacks().init(index, true, 0, 0);
        recipeLayout.getItemStacks().set(index, ingredients.getInputs(ItemStack.class).get(0));

        index++;
        recipeLayout.getItemStacks().init(index, true, 59, 0);
        recipeLayout.getItemStacks().set(index, ingredients.getInputs(ItemStack.class).get(1));

        index++;
        recipeLayout.getItemStacks().init(index, false, 117, 0);
        recipeLayout.getItemStacks().set(index, ingredients.getOutputs(ItemStack.class).get(0));
    }

    public static class Wrapper implements IRecipeWrapper
    {
        private final List<List<ItemStack>> input;
        private final List<List<ItemStack>> output;

        public Wrapper(KnifeRecipe recipe)
        {
            ImmutableList.Builder<List<ItemStack>> builder = ImmutableList.builder();

            // Add the main ingredient and knife items
            builder.add(recipe.getInput().getStacks());
            List<ItemStack> knives = new ArrayList<>(OreDictionary.getOres("toolKnife"));
            knives.addAll(OreDictionary.getOres("toolWeakKnife"));
            builder.add(knives);

            // Set the input
            input = builder.build();

            // Reset builder and add output
            builder = ImmutableList.builder();
            builder.add(Arrays.asList(recipe.getOutput()));

            // Set the output
            output = builder.build();
        }

        @Override
        public void getIngredients(@Nonnull IIngredients ingredients)
        {
            ingredients.setInputLists(ItemStack.class, input);
            ingredients.setOutputLists(ItemStack.class, output);
        }
    }
}
*/