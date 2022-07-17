package com.alcatrazescapee.notreepunching.common.recipes;

import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import com.alcatrazescapee.notreepunching.platform.RegistryHolder;
import com.alcatrazescapee.notreepunching.platform.RegistryInterface;
import com.alcatrazescapee.notreepunching.platform.XPlatform;

public class ModRecipes
{
    public static final RegistryInterface<RecipeSerializer<?>> RECIPE_SERIALIZERS = XPlatform.INSTANCE.registryInterface(Registry.RECIPE_SERIALIZER);
    public static final RegistryInterface<RecipeType<?>> RECIPE_TYPES = XPlatform.INSTANCE.registryInterface(Registry.RECIPE_TYPE);

    // Forge requires us to implement IShapedRecipe<> on one of these
    // Otherwise we could get away with only one delegate
    public static final RegistryHolder<RecipeSerializer<?>> SHAPED_TOOL_DAMAGING = RECIPE_SERIALIZERS.register("tool_damaging_shaped", () -> XPlatform.INSTANCE.recipeSerializer(new ToolDamagingRecipe.Serializer<>(XPlatform.INSTANCE::shapedToolDamagingRecipe)));
    public static final RegistryHolder<RecipeSerializer<?>> SHAPELESS_TOOL_DAMAGING = RECIPE_SERIALIZERS.register("tool_damaging_shapeless", () -> XPlatform.INSTANCE.recipeSerializer(new ToolDamagingRecipe.Serializer<>(XPlatform.INSTANCE::shapelessToolDamagingRecipe)));

    public static final RegistryHolder<RecipeSerializer<?>> EMPTY_SERIALIZER = RECIPE_SERIALIZERS.register("empty", () -> XPlatform.INSTANCE.recipeSerializer(EmptyRecipe.Serializer.INSTANCE));
    public static final RegistryHolder<RecipeType<?>> EMPTY_TYPE = RECIPE_TYPES.register("empty", () -> new RecipeType<>() {});
}
