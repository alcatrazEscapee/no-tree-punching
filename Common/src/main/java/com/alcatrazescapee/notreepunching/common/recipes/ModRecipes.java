package com.alcatrazescapee.notreepunching.common.recipes;

import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;

import com.alcatrazescapee.notreepunching.platform.RegistryHolder;
import com.alcatrazescapee.notreepunching.platform.RegistryInterface;
import com.alcatrazescapee.notreepunching.platform.XPlatform;

public class ModRecipes
{
    public static final RegistryInterface<RecipeSerializer<?>> RECIPE_SERIALIZERS = XPlatform.INSTANCE.registryInterface(Registry.RECIPE_SERIALIZER);

    public static final RegistryHolder<RecipeSerializer<?>> TOOL_DAMAGING = RECIPE_SERIALIZERS.register("tool_damaging", () -> XPlatform.INSTANCE.recipeSerializer(ShapedToolDamagingRecipe.Serializer.INSTANCE));
}
