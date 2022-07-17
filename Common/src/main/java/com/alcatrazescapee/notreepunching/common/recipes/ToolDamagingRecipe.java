package com.alcatrazescapee.notreepunching.common.recipes;

import java.util.function.BiFunction;
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

public abstract class ToolDamagingRecipe implements DelegateRecipe<CraftingContainer>, CraftingRecipe
{
    private final ResourceLocation id;
    private final Recipe<?> recipe;

    protected ToolDamagingRecipe(ResourceLocation id, Recipe<?> recipe)
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
    @SuppressWarnings("unchecked")
    public Recipe<CraftingContainer> delegate()
    {
        return (Recipe<CraftingContainer>) recipe;
    }

    public static class Shaped extends ToolDamagingRecipe
    {
        public Shaped(ResourceLocation id, Recipe<?> recipe)
        {
            super(id, recipe);
        }

        @Override
        public RecipeSerializer<?> getSerializer()
        {
            return ModRecipes.SHAPED_TOOL_DAMAGING.get();
        }
    }

    public static class Shapeless extends ToolDamagingRecipe
    {
        public Shapeless(ResourceLocation id, Recipe<?> recipe)
        {
            super(id, recipe);
        }

        @Override
        public RecipeSerializer<?> getSerializer()
        {
            return ModRecipes.SHAPELESS_TOOL_DAMAGING.get();
        }
    }

    public record Serializer<T extends ToolDamagingRecipe>(BiFunction<ResourceLocation, Recipe<?>, T> factory) implements RecipeSerializerImpl<T>
    {
        @Override
        public T fromJson(ResourceLocation recipeId, JsonObject json, RecipeSerializerImpl.Context context)
        {
            return factory.apply(recipeId, context.fromJson(recipeId, GsonHelper.getAsJsonObject(json, "recipe")));
        }

        @Override
        public T fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer)
        {
            return factory.apply(recipeId, ClientboundUpdateRecipesPacket.fromNetwork(buffer));
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ToolDamagingRecipe recipe)
        {
            ClientboundUpdateRecipesPacket.toNetwork(buffer, recipe.delegate());
        }
    }
}
