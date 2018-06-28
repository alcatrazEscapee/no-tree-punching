/*
 *
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 *
 */

package notreepunching.integration.jei.forge;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import notreepunching.NoTreePunching;

import javax.annotation.Nonnull;

import static notreepunching.integration.jei.JEIPluginHelper.FORGE_UID;

public class ForgeRecipeCategory implements IRecipeCategory {

    private String localizedName;
    public static final ResourceLocation LOC = new ResourceLocation(NoTreePunching.MODID,"textures/jei/forge.png");
    private final IDrawable background;
    private final IDrawable icon;

    private final IDrawableAnimated animatedFlame;
    private final IDrawableAnimated animatedArrow;

    public ForgeRecipeCategory(IGuiHelper guiHelper){
        background = guiHelper.createDrawable(LOC,0,0,128,54);
        localizedName = NoTreePunching.proxy.localize("jei.category.forge_recipe");
        icon = guiHelper.createDrawable(LOC,128,0,16,16);

        IDrawableStatic staticFlame = guiHelper.createDrawable(LOC, 144, 0, 14, 14);
        animatedFlame = guiHelper.createAnimatedDrawable(staticFlame, 300, IDrawableAnimated.StartDirection.TOP, true);

        IDrawableStatic staticArrow = guiHelper.createDrawable(LOC, 158, 0, 16, 23);
        animatedArrow = guiHelper.createAnimatedDrawable(staticArrow, 200, IDrawableAnimated.StartDirection.LEFT, false);

    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper, @Nonnull IIngredients ingredients) {
        if(!(recipeWrapper instanceof ForgeRecipeWrapper)) {
            return;
        }

        int index = 0;
        recipeLayout.getItemStacks().init(index, true, 27, 0);
        recipeLayout.getItemStacks().set(index, ingredients.getInputs(ItemStack.class).get(0));

        index++;
        recipeLayout.getItemStacks().init(index, false, 83, 0);
        recipeLayout.getItemStacks().set(index, ingredients.getOutputs(ItemStack.class).get(0));

    }


    @Nonnull
    @Override
    public String getUid() {
        return FORGE_UID;
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

    @Override
    public void drawExtras(Minecraft minecraft) {
        animatedFlame.draw(minecraft, 56, 20);
        animatedArrow.draw(minecraft, 53, 1);
    }
}
