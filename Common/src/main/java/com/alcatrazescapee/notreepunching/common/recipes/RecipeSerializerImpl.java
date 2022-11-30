package com.alcatrazescapee.notreepunching.common.recipes;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeSerializer;

/**
 * Platform independent implementation of {@link net.minecraft.world.item.crafting.RecipeSerializer}.
 */
public interface RecipeSerializerImpl<T extends Recipe<?>> extends RecipeSerializer<T>
{
    default T fromJson(ResourceLocation recipeId, JsonObject json)
    {
        return fromJson(recipeId, json, Context.EMPTY);
    }

    T fromJson(ResourceLocation recipeId, JsonObject json, Context context);

    T fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer);

    void toNetwork(FriendlyByteBuf recipeId, T recipe);

    interface Context
    {
        Context EMPTY = new Context() {};

        default Recipe<?> fromJson(ResourceLocation recipeId, JsonObject json)
        {
            return RecipeManager.fromJson(recipeId, json);
        }
    }
}
