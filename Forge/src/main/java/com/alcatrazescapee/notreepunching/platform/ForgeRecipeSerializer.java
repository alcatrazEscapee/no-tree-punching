package com.alcatrazescapee.notreepunching.platform;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.conditions.ICondition;

import com.alcatrazescapee.notreepunching.common.recipes.RecipeSerializerImpl;

public record ForgeRecipeSerializer<T extends Recipe<?>>(RecipeSerializerImpl<T> impl) implements RecipeSerializer<T>
{
    @Override
    public T fromJson(ResourceLocation recipeId, JsonObject json)
    {
        return impl.fromJson(recipeId, json, Context.EMPTY);
    }

    @Override
    public T fromJson(ResourceLocation recipeId, JsonObject json, ICondition.IContext context)
    {
        return impl.fromJson(recipeId, json, new Context(context));
    }

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

    public record Context(ICondition.IContext context) implements RecipeSerializerImpl.Context
    {
        @Override
        public Recipe<?> fromJson(ResourceLocation recipeId, JsonObject json)
        {
            return RecipeManager.fromJson(recipeId, json, context);
        }
    }
}
