/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.recipes;

import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import com.alcatrazescapee.notreepunching.NoTreePunching;
import com.alcatrazescapee.notreepunching.platform.RegistryHolder;
import com.alcatrazescapee.notreepunching.platform.RegistryInterface;
import com.alcatrazescapee.notreepunching.platform.XPlatform;

public class ModRecipes
{
    public static final RegistryInterface<RecipeSerializer<?>> RECIPE_SERIALIZERS = XPlatform.INSTANCE.registryInterface(Registry.RECIPE_SERIALIZER);

    public static final RegistryHolder<ShapedToolDamagingRecipe.Serializer> TOOL_DAMAGING = RECIPE_SERIALIZERS.register("tool_damaging", ShapedToolDamagingRecipe.Serializer::new);
}
