package notreepunching.jei.firepit;

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

public class FirepitRecipeCategory implements IRecipeCategory {

    public static final String UID = "notreepunching.firepit";
    private String localizedName;
    private final IDrawable background;
    private final IDrawable icon;

    public FirepitRecipeCategory(IGuiHelper guiHelper){
        background = guiHelper.createDrawable(new ResourceLocation("notreepunching","textures/jei/firepit_recipe_background.png"),0,0,164,32);
        localizedName = NoTreePunching.proxy.localize("notreepunching.jei.category.firepit_recipe");
        icon = guiHelper.createDrawable(new ResourceLocation("notreepunching","textures/jei/firepit_recipe_background.png"),164,0,16,16);

    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper, @Nonnull IIngredients ingredients) {
        if(!(recipeWrapper instanceof FirepitRecipeWrapper)) {
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
        recipeLayout.getItemStacks().init(index, true, 57, 8);
        recipeLayout.getItemStacks().set(index, ingredients.getInputs(ItemStack.class).get(0));

        index++;
        recipeLayout.getItemStacks().init(index, false, 115, 8);
        recipeLayout.getItemStacks().set(index, ingredients.getOutputs(ItemStack.class).get(0));

        //index++;
        //recipeLayout.getItemStacks().init(index, false, 131, 8);
        //recipeLayout.getItemStacks().set(index, ingredients.getOutputs(ItemStack.class).get(0));
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
