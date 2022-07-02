package com.alcatrazescapee.notreepunching.common.recipes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.common.crafting.IShapedRecipe;

/**
 * Extend to implement {@link net.minecraftforge.common.crafting.IShapedRecipe}
 */
public final class ForgeShapedToolDamagingRecipe extends ShapedToolDamagingRecipe implements IShapedRecipe<CraftingContainer>
{
    public ForgeShapedToolDamagingRecipe(ResourceLocation id, Recipe<?> recipe)
    {
        super(id, recipe);
    }

    @Override
    public int getRecipeWidth()
    {
        return ((ShapedRecipe) delegate()).getWidth();
    }

    @Override
    public int getRecipeHeight()
    {
        return ((ShapedRecipe) delegate()).getHeight();
    }
}
