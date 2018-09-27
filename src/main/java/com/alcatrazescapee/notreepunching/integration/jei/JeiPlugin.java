/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.integration.jei;

import com.alcatrazescapee.notreepunching.client.gui.GuiFirePit;
import com.alcatrazescapee.notreepunching.common.recipe.FirePitRecipe;
import com.alcatrazescapee.notreepunching.common.recipe.KnifeRecipe;
import com.alcatrazescapee.notreepunching.common.recipe.ModRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;

import static com.alcatrazescapee.notreepunching.ModConstants.MOD_ID;

@JEIPlugin
public class JeiPlugin implements IModPlugin
{
    static final String KNIFE_UID = MOD_ID + ".knife";
    static final String FIREPIT_UID = MOD_ID + ".firepit";

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry)
    {
        registry.addRecipeCategories(
                new KnifeRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
                new FirePitRecipeCategory(registry.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void register(IModRegistry registry)
    {
        // todo: replace with a guidebook
        /*// Material Info:
        registry.addIngredientInfo(new ItemStack(ModItems.rockStone), ItemStack.class,"jei.description.rock");
        registry.addIngredientInfo(new ItemStack(ModItems.grassFiber), ItemStack.class,"jei.description.grass_fiber");
        registry.addIngredientInfo(new ItemStack(ModItems.flintShard), ItemStack.class,"jei.description.flint_shard");
        registry.addIngredientInfo(new ItemStack(Items.STICK), ItemStack.class,"jei.description.stick");

        // Tools Info
        registry.addIngredientInfo(ModItems.getTools(ToolType.MATTOCK),ItemStack.class,"jei.description.mattock");
        registry.addIngredientInfo(ModItems.getTools(ToolType.KNIFE),ItemStack.class,"jei.description.knife");
        registry.addIngredientInfo(ModItems.getTools(ToolType.SAW),ItemStack.class,"jei.description.saw");
        registry.addIngredientInfo(new ItemStack(ModItems), ItemStack.class, "jei.description.clay_tool");

        registry.addIngredientInfo(new ItemStack(ModBlocks.firepit),ItemStack.class,"jei.description.firepit");
        registry.addIngredientInfo(OreDictionary.getOres("logWood"),ItemStack.class,"jei.description.wood_pile");
        */

        // Blacklist Ingredients
        IIngredientBlacklist blacklist = registry.getJeiHelpers().getIngredientBlacklist();
        // todo: check blacklist
        //blacklist.addIngredientToBlacklist(new ItemStack(ModBlocks.looseRock));

        // Knife / Cutting Recipes
        registry.handleRecipes(KnifeRecipe.class, KnifeRecipeCategory.Wrapper::new, KNIFE_UID);
        registry.addRecipes(ModRecipes.KNIFE.getAll(), KNIFE_UID);

        // Firepit Recipes
        registry.handleRecipes(FirePitRecipe.class, FirePitRecipeCategory.Wrapper::new, FIREPIT_UID);
        registry.addRecipes(ModRecipes.FIRE_PIT.getAll(), FIREPIT_UID);
        registry.addRecipeClickArea(GuiFirePit.class, 75, 22, 26, 19, FIREPIT_UID);
        //registry.addRecipeCatalyst(new ItemStack(ModBlocks.FIRE_PIT), FIREPIT_UID); // since it doesn't have an itemblock
    }
}
