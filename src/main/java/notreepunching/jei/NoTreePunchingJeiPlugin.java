package notreepunching.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.init.Items;
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

        // Material Info:
        registry.addIngredientInfo(new ItemStack(ModItems.rock),ItemStack.class,"jei.description.rock");
        registry.addIngredientInfo(new ItemStack(ModItems.poorIron),ItemStack.class,"jei.description.poor_iron");
        registry.addIngredientInfo(new ItemStack(ModItems.poorCoal),ItemStack.class,"jei.description.poor_coal");
        registry.addIngredientInfo(new ItemStack(ModItems.grassFiber),ItemStack.class,"jei.description.grass_fiber");
        registry.addIngredientInfo(new ItemStack(Items.STICK),ItemStack.class,"jei.description.stick");

        // Tools Info
        registry.addIngredientInfo(ModItems.listAllMattocks(),ItemStack.class,"jei.description.mattock");
        registry.addIngredientInfo(ModItems.listAllKnives(),ItemStack.class,"jei.description.knife");
        registry.addIngredientInfo(new ItemStack(ModItems.crudePick),ItemStack.class,"jei.description.crude_pick");
        registry.addIngredientInfo(new ItemStack(ModItems.crudeHatchet),ItemStack.class,"jei.description.crude_hatchet");

        // Knife / Cutting Recipes
        registry.handleRecipes(CuttingRecipe.class, KnifeRecipeWrapper::new, KnifeRecipeCategory.UID);
        registry.addRecipes(ModRecipes.CUTTING_RECIPES, KnifeRecipeCategory.UID);
    }

}
