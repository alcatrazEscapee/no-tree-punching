/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.recipes;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import com.alcatrazescapee.notreepunching.NoTreePunching;

public class ModRecipes
{
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, NoTreePunching.MOD_ID);

    public static final RegistryObject<ShapedToolDamagingRecipe.Serializer> TOOL_DAMAGING = RECIPE_SERIALIZERS.register("tool_damaging", ShapedToolDamagingRecipe.Serializer::new);
}
