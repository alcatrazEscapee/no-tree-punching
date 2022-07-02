package com.alcatrazescapee.notreepunching.common.recipes;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundUpdateRecipesPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeSerializer;

import com.alcatrazescapee.notreepunching.platform.XPlatform;
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
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer container)
    {
        final NonNullList<ItemStack> items = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);
        for (int i = 0; i < items.size(); i++)
        {
            final ItemStack stack = container.getItem(i);
            final ItemStack remainder = XPlatform.INSTANCE.getCraftingRemainder(stack);
            if (!remainder.isEmpty())
            {
                items.set(i, remainder);
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
    public Recipe<CraftingContainer> delegate()
    {
        return (Recipe<CraftingContainer>) recipe;
    }

    public enum Serializer implements RecipeSerializerImpl<ShapedToolDamagingRecipe>
    {
        INSTANCE;

        @Override
        public ShapedToolDamagingRecipe fromJson(ResourceLocation recipeId, JsonObject json, RecipeSerializerImpl.Context context)
        {
            return XPlatform.INSTANCE.shapedToolDamagingRecipe(recipeId, RecipeManager.fromJson(recipeId, GsonHelper.getAsJsonObject(json, "recipe")));
        }

        @Override
        public ShapedToolDamagingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer)
        {
            return XPlatform.INSTANCE.shapedToolDamagingRecipe(recipeId, ClientboundUpdateRecipesPacket.fromNetwork(buffer));
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ShapedToolDamagingRecipe recipe)
        {
            ClientboundUpdateRecipesPacket.toNetwork(buffer, recipe.delegate());
        }
    }
}
