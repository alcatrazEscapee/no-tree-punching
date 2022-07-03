package com.alcatrazescapee.notreepunching.common.recipes;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

/**
 * A no-op recipe implementation, used to remove recipes platform independently.
 */
public record EmptyRecipe(ResourceLocation id) implements Recipe<Container>
{
    @Override
    public boolean matches(Container container, Level level)
    {
        return false;
    }

    @Override
    public ItemStack assemble(Container container)
    {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height)
    {
        return false;
    }

    @Override
    public ItemStack getResultItem()
    {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId()
    {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return ModRecipes.EMPTY_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType()
    {
        return ModRecipes.EMPTY_TYPE.get();
    }

    public enum Serializer implements RecipeSerializerImpl<EmptyRecipe>
    {
        INSTANCE;

        @Override
        public EmptyRecipe fromJson(ResourceLocation id, JsonObject json, Context context)
        {
            return new EmptyRecipe(id);
        }

        @Override
        public EmptyRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer)
        {
            return new EmptyRecipe(id);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, EmptyRecipe recipe) {}
    }
}
