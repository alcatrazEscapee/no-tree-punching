/*
 *
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 *
 */

package notreepunching.integration.jei.knife;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import notreepunching.NoTreePunching;
import notreepunching.integration.jei.JEIPluginHelper;

import javax.annotation.Nonnull;

public class KnifeRecipeCategory implements IRecipeCategory {

    private String localizedName;
    private final ResourceLocation LOC = new ResourceLocation(NoTreePunching.MODID,"textures/jei/knife.png");
    private final IDrawable background;
    private final IDrawable icon;

    public KnifeRecipeCategory(IGuiHelper guiHelper){
        background = guiHelper.createDrawable(LOC,0,0,135,18);
        localizedName = NoTreePunching.proxy.localize("jei.category.knife_recipe");
        icon = guiHelper.createDrawable(LOC,135,0,16,16);

    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper, @Nonnull IIngredients ingredients) {
        if(!(recipeWrapper instanceof KnifeRecipeWrapper)) {
            return;
        }

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


    @Nonnull
    @Override
    public String getUid() {
        return JEIPluginHelper.KNIFE_UID;
    }

    @Nonnull
    @Override
    public String getTitle() {
        return localizedName;
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Nonnull
    @Override
    public String getModName() {
        return NoTreePunching.actualName;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }
}
