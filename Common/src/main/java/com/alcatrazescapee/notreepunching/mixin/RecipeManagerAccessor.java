package com.alcatrazescapee.notreepunching.mixin;

import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RecipeManager.class)
public interface RecipeManagerAccessor
{
    @Accessor("recipes") Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> accessor$getRecipes();
    @Accessor("recipes") @Mutable void accessor$setRecipes(Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> recipes);

    @Accessor("byName") Map<ResourceLocation, Recipe<?>> accessor$getByName();
    @Accessor("byName") @Mutable void accessor$setByName(Map<ResourceLocation, Recipe<?>> byName);
}
