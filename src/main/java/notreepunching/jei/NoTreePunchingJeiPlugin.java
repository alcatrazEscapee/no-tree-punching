package notreepunching.jei;

import mezz.jei.api.*;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import notreepunching.item.ModItems;
import notreepunching.jei.knife.KnifeRecipeCategory;
import notreepunching.jei.knife.KnifeRecipeWrapper;
import notreepunching.recipe.CuttingRecipe;
import notreepunching.recipe.ModRecipes;

import java.util.ArrayList;
import java.util.List;

@JEIPlugin
public class NoTreePunchingJeiPlugin implements IModPlugin {

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(
                new KnifeRecipeCategory(registry.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void register(IModRegistry registry) {

        // Add Info about things:
        registry.addIngredientInfo(new ItemStack(ModItems.rock),ItemStack.class,"jei.description.rock");
        List<ItemStack> knifeList = new ArrayList<>();
        knifeList.add(new ItemStack(ModItems.stoneKnife));
        knifeList.add(new ItemStack(ModItems.ironKnife));
        knifeList.add(new ItemStack(ModItems.goldKnife));
        knifeList.add(new ItemStack(ModItems.diamondKnife));
        registry.addIngredientInfo(knifeList,ItemStack.class,"jei.description.knife");

        // Add Cutting recipes
        registry.handleRecipes(CuttingRecipe.class, KnifeRecipeWrapper::new, KnifeRecipeCategory.UID);

        registry.addRecipes(ModRecipes.CUTTING_RECIPES, KnifeRecipeCategory.UID);
    }

}
