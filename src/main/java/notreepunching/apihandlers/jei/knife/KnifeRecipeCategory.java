package notreepunching.apihandlers.jei.knife;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import notreepunching.NoTreePunching;

import javax.annotation.Nonnull;

public class KnifeRecipeCategory implements IRecipeCategory {

    public static final String UID = "notreepunching.knife";
    private String localizedName;
    private final ResourceLocation LOC = new ResourceLocation(NoTreePunching.MODID,"textures/jei/knife.png");
    private final IDrawable background;
    private final IDrawable icon;

    public KnifeRecipeCategory(IGuiHelper guiHelper){
        background = guiHelper.createDrawable(LOC,0,0,164,32);
        localizedName = NoTreePunching.proxy.localize("notreepunching.jei.category.knife_recipe");
        icon = guiHelper.createDrawable(LOC,164,0,16,16);

    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper, @Nonnull IIngredients ingredients) {
        if(!(recipeWrapper instanceof KnifeRecipeWrapper)) {
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
        recipeLayout.getItemStacks().init(index, true, 14, 8);
        recipeLayout.getItemStacks().set(index, ingredients.getInputs(ItemStack.class).get(0));

        index++;
        recipeLayout.getItemStacks().init(index, true, 73, 8);
        recipeLayout.getItemStacks().set(index, ingredients.getInputs(ItemStack.class).get(1));

        index++;
        recipeLayout.getItemStacks().init(index, false, 131, 8);
        recipeLayout.getItemStacks().set(index, ingredients.getOutputs(ItemStack.class).get(0));
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
}
