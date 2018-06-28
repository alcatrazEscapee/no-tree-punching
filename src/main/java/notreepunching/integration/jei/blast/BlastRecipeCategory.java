/*
 *
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 *
 */

package notreepunching.integration.jei.blast;

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

import static notreepunching.integration.jei.JEIPluginHelper.BLAST_UID;

public class BlastRecipeCategory implements IRecipeCategory{

    private String localizedName;
    static final ResourceLocation LOC = new ResourceLocation(NoTreePunching.MODID,"textures/jei/blast_furnace.png");
    private final IDrawable background;
    private final IDrawable icon;

    private final IDrawableAnimated animatedFlame;
    private final IDrawableAnimated animatedArrow;
    private final IDrawableAnimated animatedCharcoal;

    public BlastRecipeCategory(IGuiHelper guiHelper){
        background = guiHelper.createDrawable(LOC,0,0,112,50);
        localizedName = NoTreePunching.proxy.localize("jei.category.blast_furnace_recipe");
        icon = guiHelper.createDrawable(LOC,112,0,16,16);

        IDrawableStatic staticFlame = guiHelper.createDrawable(LOC, 128, 0, 14, 14);
        animatedFlame = guiHelper.createAnimatedDrawable(staticFlame, 300, IDrawableAnimated.StartDirection.TOP, true);

        IDrawableStatic staticArrow = guiHelper.createDrawable(LOC, 152, 0, 16, 23);
        animatedArrow = guiHelper.createAnimatedDrawable(staticArrow, 200, IDrawableAnimated.StartDirection.LEFT, false);

        IDrawableStatic staticCharcoal = guiHelper.createDrawable(LOC, 175, 0, 8, 48);
        animatedCharcoal = guiHelper.createAnimatedDrawable(staticCharcoal, 500, IDrawableAnimated.StartDirection.TOP, true);

    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper, @Nonnull IIngredients ingredients) {
        if(!(recipeWrapper instanceof BlastRecipeWrapper)) {
            return;
        }

        int index = 0;
        /*
         * Initialize the itemStack at slotIndex.
         *
         * @param slotIndex the slot index of this itemStack
         * @param input     whether this slot is an input. Used for the recipe-fill feature.
         * @param xPosition x position of the slot relative to the recipe background
         * @param yPosition y position of the slot relative to the recipe background
         */
        recipeLayout.getItemStacks().init(index, true, 38, 7);
        recipeLayout.getItemStacks().set(index, ingredients.getInputs(ItemStack.class).get(0));

        index++;
        recipeLayout.getItemStacks().init(index, false, 94, 16);
        recipeLayout.getItemStacks().set(index, ingredients.getOutputs(ItemStack.class).get(0));

    }


    @Nonnull
    @Override
    public String getUid() {
        return BLAST_UID;
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
        animatedFlame.draw(minecraft, 39, 27);
        animatedArrow.draw(minecraft, 64, 17);
        animatedCharcoal.draw(minecraft, 25, 1);
    }
}
