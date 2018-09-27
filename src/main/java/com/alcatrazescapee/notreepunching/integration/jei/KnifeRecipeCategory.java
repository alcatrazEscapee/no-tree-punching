/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.integration.jei;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import com.alcatrazescapee.notreepunching.ModConstants;
import com.alcatrazescapee.notreepunching.common.items.ModItems;
import com.alcatrazescapee.notreepunching.common.recipe.KnifeRecipe;
import com.alcatrazescapee.notreepunching.util.types.Metal;
import com.alcatrazescapee.notreepunching.util.types.ToolType;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;

import static com.alcatrazescapee.notreepunching.ModConstants.MOD_ID;
import static com.alcatrazescapee.notreepunching.integration.jei.JeiPlugin.KNIFE_UID;

@ParametersAreNonnullByDefault
public class KnifeRecipeCategory implements IRecipeCategory<KnifeRecipeCategory.Wrapper>
{
    private final IDrawable background;
    private final IDrawable icon;
    private final String localizedName;

    public KnifeRecipeCategory(IGuiHelper guiHelper)
    {
        final ResourceLocation location = new ResourceLocation(MOD_ID, "textures/jei/knife.png");
        background = guiHelper.createDrawable(location, 0, 0, 135, 18);
        // todo: check on server
        localizedName = I18n.format("jei.category.knife_recipe");
        icon = guiHelper.createDrawable(location, 135, 0, 16, 16);
    }

    @Nonnull
    @Override
    public String getUid()
    {
        return KNIFE_UID;
    }

    @Nonnull
    @Override
    public String getTitle()
    {
        return localizedName;
    }

    @Nonnull
    @Override
    public String getModName()
    {
        return ModConstants.MOD_NAME;
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
            builder.add(recipe.getInput());
            builder.add(ImmutableList.of(new ItemStack(ModItems.getTool(ToolType.KNIFE, Metal.DIAMOND))));

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
        public void getIngredients(@Nonnull IIngredients ingredients)
        {
            ingredients.setInputLists(ItemStack.class, input);
            ingredients.setOutputLists(ItemStack.class, output);
        }
    }
}
