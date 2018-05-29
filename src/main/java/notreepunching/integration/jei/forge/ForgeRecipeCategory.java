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

public class ForgeRecipeCategory implements IRecipeCategory {

    public static final String UID = "notreepunching.forge";
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
        /**
         * Initialize the itemStack at slotIndex.
         *
         * @param slotIndex the slot index of this itemStack
         * @param input     whether this slot is an input. Used for the recipe-fill feature.
         * @param xPosition x position of the slot relative to the recipe background
         * @param yPosition y position of the slot relative to the recipe background
         */
        recipeLayout.getItemStacks().init(index, true, 27, 0);
        recipeLayout.getItemStacks().set(index, ingredients.getInputs(ItemStack.class).get(0));

        index++;
        recipeLayout.getItemStacks().init(index, false, 83, 0);
        recipeLayout.getItemStacks().set(index, ingredients.getOutputs(ItemStack.class).get(0));

        //temperature = ((ForgeRecipeWrapper) recipeWrapper).getTemperature() / 50;
        //temperature = ingredients.getInputs(int.class).get(0).get(0);

        //System.out.println("INIT THE RECIPE: "+ingredients.getOutputs(ItemStack.class).get(0)+" | T="+temperature);
        //drawableTemperature = guiHelper.createDrawable(LOC, 181,30-temperature,10,temperature);

    }


    @Nonnull
    @Override
    public String getUid() {
        return UID;
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
        animatedArrow.draw(minecraft, 53, 2);
        //System.out.println("DRAWING THE FKING THING!!!"+temperature);
        //drawableTemperature.draw(minecraft, 0, 33 - temperature);
    }
}
