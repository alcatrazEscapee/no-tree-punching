/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.integration.jei;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.alcatrazescapee.notreepunching.ModConfig;
import com.alcatrazescapee.notreepunching.client.gui.GuiFirePit;
import com.alcatrazescapee.notreepunching.common.items.ModItems;
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
public final class JeiPlugin implements IModPlugin
{
    static final String KNIFE_UID = MOD_ID + ".knife";
    static final String FIREPIT_UID = MOD_ID + ".fire_pit";

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
        // Material Info:
        registry.addIngredientInfo(OreDictionary.getOres("rock"), ItemStack.class, "jei.description.rock");
        registry.addIngredientInfo(new ItemStack(ModItems.GRASS_FIBER), ItemStack.class, "jei.description.grass_fiber");
        registry.addIngredientInfo(new ItemStack(ModItems.FLINT_SHARD), ItemStack.class, "jei.description.flint_shard");
        registry.addIngredientInfo(new ItemStack(Items.STICK), ItemStack.class, "jei.description.stick");

        // Tools Info
        registry.addIngredientInfo(OreDictionary.getOres("toolKnife"), ItemStack.class, "jei.description.knife");
        registry.addIngredientInfo(OreDictionary.getOres("toolMattock"), ItemStack.class, "jei.description.mattock");
        registry.addIngredientInfo(OreDictionary.getOres("toolSaw"), ItemStack.class, "jei.description.saw");
        registry.addIngredientInfo(new ItemStack(ModItems.CLAY_TOOL), ItemStack.class, "jei.description.clay_tool");

        // Blacklist Ingredients
        if (ModConfig.GENERAL.replaceVanillaRecipes)
        {
            IIngredientBlacklist blacklist = registry.getJeiHelpers().getIngredientBlacklist();

            blacklist.addIngredientToBlacklist(new ItemStack(Items.WOODEN_AXE));
            blacklist.addIngredientToBlacklist(new ItemStack(Items.WOODEN_HOE));
            blacklist.addIngredientToBlacklist(new ItemStack(Items.WOODEN_PICKAXE));
            blacklist.addIngredientToBlacklist(new ItemStack(Items.WOODEN_SHOVEL));
            blacklist.addIngredientToBlacklist(new ItemStack(Items.WOODEN_SWORD));

            blacklist.addIngredientToBlacklist(new ItemStack(Items.STONE_AXE));
            blacklist.addIngredientToBlacklist(new ItemStack(Items.STONE_HOE));
            blacklist.addIngredientToBlacklist(new ItemStack(Items.STONE_PICKAXE));
            blacklist.addIngredientToBlacklist(new ItemStack(Items.STONE_SHOVEL));
            blacklist.addIngredientToBlacklist(new ItemStack(Items.STONE_SWORD));
        }

        // Knife Recipes
        registry.handleRecipes(KnifeRecipe.class, KnifeRecipeCategory.Wrapper::new, KNIFE_UID);
        registry.addRecipes(ModRecipes.KNIFE.getAll(), KNIFE_UID);

        // Firepit Recipes
        registry.handleRecipes(FirePitRecipe.class, FirePitRecipeCategory.Wrapper::new, FIREPIT_UID);
        registry.addRecipes(ModRecipes.FIRE_PIT.getAll(), FIREPIT_UID);
        registry.addRecipeClickArea(GuiFirePit.class, 75, 22, 26, 19, FIREPIT_UID);
    }
}
