package notreepunching.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import notreepunching.block.ModBlocks;
import notreepunching.client.gui.GuiFirepit;
import notreepunching.client.gui.GuiForge;
import notreepunching.item.ModItems;
import notreepunching.jei.firepit.FirepitRecipeCategory;
import notreepunching.jei.firepit.FirepitRecipeWrapper;
import notreepunching.jei.forge.ForgeRecipeCategory;
import notreepunching.jei.forge.ForgeRecipeWrapper;
import notreepunching.jei.knife.KnifeRecipeCategory;
import notreepunching.jei.knife.KnifeRecipeWrapper;
import notreepunching.recipe.cutting.CuttingRecipe;
import notreepunching.recipe.cutting.CuttingRecipeHandler;
import notreepunching.recipe.firepit.FirepitRecipe;
import notreepunching.recipe.firepit.FirepitRecipeHandler;
import notreepunching.recipe.forge.ForgeRecipe;
import notreepunching.recipe.forge.ForgeRecipeHandler;

@JEIPlugin
public class NoTreePunchingJeiPlugin implements IModPlugin {

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(
                new KnifeRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
                new FirepitRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
                new ForgeRecipeCategory(registry.getJeiHelpers().getGuiHelper())
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
        registry.addIngredientInfo(new ItemStack(Items.COAL,1,1),ItemStack.class,"jei.description.forge");
        registry.addIngredientInfo(OreDictionary.getOres("logWood"),ItemStack.class,"jei.description.wood_pile");

        // Blacklist Ingredients

        IIngredientBlacklist blacklist = registry.getJeiHelpers().getIngredientBlacklist();
        blacklist.addIngredientToBlacklist(new ItemStack(ModBlocks.looseRock));
        blacklist.addIngredientToBlacklist(new ItemStack(ModBlocks.charcoalPile));
        blacklist.addIngredientToBlacklist(new ItemStack(ModBlocks.forge));
        blacklist.addIngredientToBlacklist(new ItemStack(ModBlocks.woodPile));

        // Knife / Cutting Recipes
        registry.handleRecipes(CuttingRecipe.class, KnifeRecipeWrapper::new, KnifeRecipeCategory.UID);
        registry.addRecipes(CuttingRecipeHandler.getAll(), KnifeRecipeCategory.UID);

        // Firepit Recipes
        registry.handleRecipes(FirepitRecipe.class, FirepitRecipeWrapper::new, FirepitRecipeCategory.UID);
        registry.addRecipes(FirepitRecipeHandler.getAll(), FirepitRecipeCategory.UID);
        registry.addRecipeClickArea(GuiFirepit.class, 75, 22, 26, 19, FirepitRecipeCategory.UID);

        // Forge Recipes
        registry.handleRecipes(ForgeRecipe.class, ForgeRecipeWrapper::new, ForgeRecipeCategory.UID);
        registry.addRecipes(ForgeRecipeHandler.getAll(), ForgeRecipeCategory.UID);
        registry.addRecipeClickArea(GuiForge.class, 75, 22, 26, 19, ForgeRecipeCategory.UID);

    }

}
