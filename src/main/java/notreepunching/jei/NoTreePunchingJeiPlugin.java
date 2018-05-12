package notreepunching.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import notreepunching.NoTreePunching;
import notreepunching.block.ModBlocks;
import notreepunching.client.gui.GuiFirepit;
import notreepunching.item.ModItems;
import notreepunching.jei.firepit.FirepitRecipeCategory;
import notreepunching.jei.firepit.FirepitRecipeWrapper;
import notreepunching.jei.knife.KnifeRecipeCategory;
import notreepunching.jei.knife.KnifeRecipeWrapper;
import notreepunching.recipe.CuttingRecipe;
import notreepunching.recipe.FirepitRecipe;
import notreepunching.recipe.ModRecipes;

@JEIPlugin
public class NoTreePunchingJeiPlugin implements IModPlugin {

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(
                new KnifeRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
                new FirepitRecipeCategory(registry.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void register(IModRegistry registry) {

        // Material Info:
        registry.addIngredientInfo(new ItemStack(ModItems.rockStone),ItemStack.class,"jei.description.rock");
        registry.addIngredientInfo(new ItemStack(ModItems.grassFiber),ItemStack.class,"jei.description.grass_fiber");
        registry.addIngredientInfo(new ItemStack(ModItems.flintShard),ItemStack.class,"jei.description.flint_shard");
        registry.addIngredientInfo(new ItemStack(Items.STICK),ItemStack.class,"jei.description.stick");

        // Tools Info
        registry.addIngredientInfo(ModItems.listAllMattocks(),ItemStack.class,"jei.description.mattock");
        registry.addIngredientInfo(ModItems.listAllKnives(),ItemStack.class,"jei.description.knife");
        registry.addIngredientInfo(ModItems.listAllSaws(),ItemStack.class,"jei.description.saw");

        registry.addIngredientInfo(new ItemStack(ModBlocks.firepit),ItemStack.class,"jei.description.firepit");

        // Blacklist Ingredients

        IIngredientBlacklist blacklist = registry.getJeiHelpers().getIngredientBlacklist();
        for(int i=0; i<7; i++) {
            if(!NoTreePunching.replaceQuarkStones && (i == 4 || i == 5)) { continue; }
            if(!NoTreePunching.replaceRusticStone && (i == 6)) { continue; }
            blacklist.addIngredientToBlacklist(new ItemStack(ModBlocks.looseRock,1,i));
        }

        // Knife / Cutting Recipes
        registry.handleRecipes(CuttingRecipe.class, KnifeRecipeWrapper::new, KnifeRecipeCategory.UID);
        registry.addRecipes(ModRecipes.CUTTING_RECIPES, KnifeRecipeCategory.UID);

        // Firepit Recipes
        registry.handleRecipes(FirepitRecipe.class, FirepitRecipeWrapper::new, FirepitRecipeCategory.UID);
        registry.addRecipes(ModRecipes.FIREPIT_RECIPES, FirepitRecipeCategory.UID);

        registry.addRecipeClickArea(GuiFirepit.class, 75, 22, 26, 19, FirepitRecipeCategory.UID);
    }

}
