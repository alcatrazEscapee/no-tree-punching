package com.alcatrazescapee.notreepunching.platform;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

import com.alcatrazescapee.notreepunching.common.recipes.RecipeSerializerImpl;

public record FabricRecipeSerializer<T extends Recipe<?>>(RecipeSerializerImpl<T> impl) implements RecipeSerializer<T>
{
    @Override
    public T fromJson(ResourceLocation recipeId, JsonObject json)
    {
        return impl.fromJson(recipeId, json);
    }

    @Nullable
    @Override
    public T fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer)
    {
        return impl.fromNetwork(recipeId, buffer);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, T recipe)
    {
        impl.toNetwork(buffer, recipe);
    }
}
