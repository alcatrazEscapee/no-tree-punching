package notreepunching.integration.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import notreepunching.block.ModBlocks;
import notreepunching.client.gui.GuiBlastFurnace;
import notreepunching.client.gui.GuiFirepit;
import notreepunching.client.gui.GuiForge;
import notreepunching.client.gui.GuiGrindstone;
import notreepunching.config.ModConfig;
import notreepunching.integration.jei.blast.BlastRecipeCategory;
import notreepunching.integration.jei.blast.BlastRecipeWrapper;
import notreepunching.integration.jei.grindstone.GrindstoneRecipeCategory;
import notreepunching.integration.jei.grindstone.GrindstoneRecipeWrapper;
import notreepunching.item.ModItems;
import notreepunching.integration.jei.firepit.FirepitRecipeCategory;
import notreepunching.integration.jei.firepit.FirepitRecipeWrapper;
import notreepunching.integration.jei.forge.ForgeRecipeCategory;
import notreepunching.integration.jei.forge.ForgeRecipeWrapper;
import notreepunching.integration.jei.knife.KnifeRecipeCategory;
import notreepunching.integration.jei.knife.KnifeRecipeWrapper;
import notreepunching.recipe.forge.BlastRecipeHandler;
import notreepunching.recipe.grindstone.GrindstoneRecipe;
import notreepunching.recipe.grindstone.GrindstoneRecipeHandler;
import notreepunching.recipe.knife.KnifeRecipe;
import notreepunching.recipe.knife.KnifeRecipeHandler;
import notreepunching.recipe.firepit.FirepitRecipe;
import notreepunching.recipe.firepit.FirepitRecipeHandler;
import notreepunching.recipe.forge.ForgeRecipe;
import notreepunching.recipe.forge.ForgeRecipeHandler;

import static notreepunching.NoTreePunching.MODID;

@mezz.jei.api.JEIPlugin
public class JEIPluginHelper implements IModPlugin {

    public static IGuiHelper helper;

    public static final String KNIFE_UID = MODID+".knife";
    public static final String FIREPIT_UID = MODID+".firepit";
    public static final String FORGE_UID = MODID+".forge";
    public static final String GRIND_UID = MODID+".grindstone";
    public static final String BLAST_UID = MODID+".blast_furnace";

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(
                new KnifeRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
                new FirepitRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
                new GrindstoneRecipeCategory(registry.getJeiHelpers().getGuiHelper())
        );
        if(ModConfig.MODULE_METALWORKING){
            registry.addRecipeCategories(
                    new ForgeRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
                    new BlastRecipeCategory(registry.getJeiHelpers().getGuiHelper())
            );
        }
    }

    @Override
    public void register(IModRegistry registry) {
        helper = registry.getJeiHelpers().getGuiHelper();

        // Material Info:
        registry.addIngredientInfo(new ItemStack(ModItems.rockStone),ItemStack.class,"jei.description.rock");
        registry.addIngredientInfo(new ItemStack(ModItems.grassFiber),ItemStack.class,"jei.description.grass_fiber");
        registry.addIngredientInfo(new ItemStack(ModItems.flintShard),ItemStack.class,"jei.description.flint_shard");
        registry.addIngredientInfo(new ItemStack(Items.STICK),ItemStack.class,"jei.description.stick");

        // Tools Info
        registry.addIngredientInfo(ModItems.listAllMattocks(),ItemStack.class,"jei.description.mattock");
        registry.addIngredientInfo(ModItems.listAllKnives(),ItemStack.class,"jei.description.knife");
        registry.addIngredientInfo(ModItems.listAllSaws(),ItemStack.class,"jei.description.saw");
        if(ModConfig.MODULE_POTTERY) {
            registry.addIngredientInfo(new ItemStack(ModItems.clayTool), ItemStack.class, "jei.description.clay_tool");
        }

        registry.addIngredientInfo(new ItemStack(ModBlocks.firepit),ItemStack.class,"jei.description.firepit");
        registry.addIngredientInfo(OreDictionary.getOres("logWood"),ItemStack.class,"jei.description.wood_pile");
        if(ModConfig.MODULE_METALWORKING) {
            registry.addIngredientInfo(new ItemStack(Items.COAL, 1, 1), ItemStack.class, "jei.description.forge","jei.description.wood_pile");
            registry.addIngredientInfo(new ItemStack(ModItems.tuyere), ItemStack.class, "jei.description.tuyere");
            registry.addIngredientInfo(new ItemStack(ModBlocks.blastFurnace), ItemStack.class, "jei.description.blast_furnace");
        }

        // Blacklist Ingredients

        IIngredientBlacklist blacklist = registry.getJeiHelpers().getIngredientBlacklist();
        blacklist.addIngredientToBlacklist(new ItemStack(ModBlocks.looseRock));
        blacklist.addIngredientToBlacklist(new ItemStack(ModBlocks.charcoalPile));
        blacklist.addIngredientToBlacklist(new ItemStack(ModBlocks.woodPile));
        if(ModConfig.MODULE_METALWORKING) {
            blacklist.addIngredientToBlacklist(new ItemStack(ModBlocks.forge));
            blacklist.addIngredientToBlacklist(new ItemStack(ModBlocks.blockTuyere));
        }

        // Knife / Cutting Recipes
        registry.handleRecipes(KnifeRecipe.class, KnifeRecipeWrapper::new, KNIFE_UID);
        registry.addRecipes(KnifeRecipeHandler.getAll(), KNIFE_UID);

        // Firepit Recipes
        registry.handleRecipes(FirepitRecipe.class, FirepitRecipeWrapper::new, FIREPIT_UID);
        registry.addRecipes(FirepitRecipeHandler.getAll(), FIREPIT_UID);
        registry.addRecipeClickArea(GuiFirepit.class, 75, 22, 26, 19, FIREPIT_UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.firepit), FIREPIT_UID);

        // Grindstone Recipes
        registry.handleRecipes(GrindstoneRecipe.class, GrindstoneRecipeWrapper::new, GRIND_UID);
        registry.addRecipes(GrindstoneRecipeHandler.getAll(), GRIND_UID);
        registry.addRecipeClickArea(GuiGrindstone.class, 75, 29, 26, 19, GRIND_UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.grindstone), GRIND_UID);

        if(ModConfig.MODULE_METALWORKING) {
            // Forge Recipes
            registry.handleRecipes(ForgeRecipe.class, ForgeRecipeWrapper::new, FORGE_UID);
            registry.addRecipes(ForgeRecipeHandler.getAll(), FORGE_UID);
            registry.addRecipeClickArea(GuiForge.class, 75, 22, 26, 19, FORGE_UID);

            // Blast Furnace Recipes
            registry.handleRecipes(ForgeRecipe.class, BlastRecipeWrapper::new, BLAST_UID);
            registry.addRecipes(BlastRecipeHandler.getAll(), BLAST_UID);
            registry.addRecipeClickArea(GuiBlastFurnace.class, 94, 34, 26, 19, BLAST_UID);
            registry.addRecipeCatalyst(new ItemStack(ModBlocks.blastFurnace), BLAST_UID);
        }

    }

}
