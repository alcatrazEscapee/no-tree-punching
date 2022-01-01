/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.recipes;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundUpdateRecipesPacket;
import net.minecraft.util.GsonHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;

import com.alcatrazescapee.notreepunching.util.Helpers;

public class ShapedToolDamagingRecipe implements IShapedDelegateRecipe<CraftingContainer>, CraftingRecipe
{
    private final ResourceLocation id;
    private final Recipe<?> recipe;

    public ShapedToolDamagingRecipe(ResourceLocation id, Recipe<?> recipe)
    {
        this.id = id;
        this.recipe = recipe;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv)
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
    public RecipeSerializer<?> getSerializer()
    {
        return ModRecipes.TOOL_DAMAGING.get();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Recipe<CraftingContainer> getDelegate()
    {
        return (Recipe<CraftingContainer>) recipe;
    }

    public static class Serializer extends RecipeSerializer<ShapedToolDamagingRecipe>
    {
        @Override
        public ShapedToolDamagingRecipe fromJson(ResourceLocation recipeId, JsonObject json)
        {
            return new ShapedToolDamagingRecipe(recipeId, RecipeManager.fromJson(recipeId, GsonHelper.getAsJsonObject(json, "recipe")));
        }

        @Nullable
        @Override
        public ShapedToolDamagingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer)
        {
            return new ShapedToolDamagingRecipe(recipeId, ClientboundUpdateRecipesPacket.fromNetwork(buffer));
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ShapedToolDamagingRecipe recipe)
        {
            ClientboundUpdateRecipesPacket.toNetwork(recipe.getDelegate(), buffer);
        }
    }
}
