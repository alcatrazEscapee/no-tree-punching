/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.recipes;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SUpdateRecipesPacket;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import com.alcatrazescapee.notreepunching.util.Helpers;

public class ShapedToolDamagingRecipe implements IShapedDelegateRecipe<CraftingInventory>, ICraftingRecipe
{
    private final ResourceLocation id;
    private final IRecipe<?> recipe;

    public ShapedToolDamagingRecipe(ResourceLocation id, IRecipe<?> recipe)
    {
        this.id = id;
        this.recipe = recipe;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInventory inv)
    {
        final NonNullList<ItemStack> items = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);
        for (int i = 0; i < items.size(); i++)
        {
            ItemStack stack = inv.getItem(i);
            if (stack.hasContainerItem())
            {
                items.set(i, stack.getContainerItem());
            }
            else if (stack.isDamageableItem())
            {
                items.set(i, Helpers.hurtAndBreak(stack, 1).copy());
            }
        }
        return items;
    }

    @Override
    public ResourceLocation getId()
    {
        return id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer()
    {
        return ModRecipes.TOOL_DAMAGING.get();
    }

    @Override
    @SuppressWarnings("unchecked")
    public IRecipe<CraftingInventory> getDelegate()
    {
        return (IRecipe<CraftingInventory>) recipe;
    }

    public static class Serializer extends RecipeSerializer<ShapedToolDamagingRecipe>
    {
        @Override
        public ShapedToolDamagingRecipe fromJson(ResourceLocation recipeId, JsonObject json)
        {
            return new ShapedToolDamagingRecipe(recipeId, RecipeManager.fromJson(recipeId, JSONUtils.getAsJsonObject(json, "recipe")));
        }

        @Nullable
        @Override
        public ShapedToolDamagingRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer)
        {
            return new ShapedToolDamagingRecipe(recipeId, SUpdateRecipesPacket.fromNetwork(buffer));
        }

        @Override
        public void toNetwork(PacketBuffer buffer, ShapedToolDamagingRecipe recipe)
        {
            SUpdateRecipesPacket.toNetwork(recipe.getDelegate(), buffer);
        }
    }
}
