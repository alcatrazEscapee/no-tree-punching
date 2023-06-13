package com.alcatrazescapee.notreepunching.common.recipes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.alcatrazescapee.notreepunching.Config;
import com.alcatrazescapee.notreepunching.common.ModTags;
import com.alcatrazescapee.notreepunching.mixin.RecipeManagerAccessor;
import com.alcatrazescapee.notreepunching.util.Helpers;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import com.alcatrazescapee.notreepunching.platform.RegistryHolder;
import com.alcatrazescapee.notreepunching.platform.RegistryInterface;
import com.alcatrazescapee.notreepunching.platform.XPlatform;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class ModRecipes
{
    public static final RegistryInterface<RecipeSerializer<?>> RECIPE_SERIALIZERS = XPlatform.INSTANCE.registryInterface(BuiltInRegistries.RECIPE_SERIALIZER);
    public static final RegistryInterface<RecipeType<?>> RECIPE_TYPES = XPlatform.INSTANCE.registryInterface(BuiltInRegistries.RECIPE_TYPE);

    // Forge requires us to implement IShapedRecipe<> on one of these
    // Otherwise we could get away with only one delegate
    public static final RegistryHolder<RecipeSerializer<?>> SHAPED_TOOL_DAMAGING = RECIPE_SERIALIZERS.register("tool_damaging_shaped", () -> XPlatform.INSTANCE.recipeSerializer(new ToolDamagingRecipe.Serializer<>(XPlatform.INSTANCE::shapedToolDamagingRecipe)));
    public static final RegistryHolder<RecipeSerializer<?>> SHAPELESS_TOOL_DAMAGING = RECIPE_SERIALIZERS.register("tool_damaging_shapeless", () -> XPlatform.INSTANCE.recipeSerializer(new ToolDamagingRecipe.Serializer<>(XPlatform.INSTANCE::shapelessToolDamagingRecipe)));

    public static final RegistryHolder<RecipeSerializer<?>> EMPTY_SERIALIZER = RECIPE_SERIALIZERS.register("empty", () -> XPlatform.INSTANCE.recipeSerializer(EmptyRecipe.Serializer.INSTANCE));
    public static final RegistryHolder<RecipeType<?>> EMPTY_TYPE = RECIPE_TYPES.register("empty", () -> new RecipeType<>() {});

    public static void injectRecipes(ReloadableServerResources resources, RegistryAccess registryAccess)
    {
        if (!Config.INSTANCE.enableDynamicRecipeReplacement.getAsBoolean()) return;

        final Set<Item> logItems = new HashSet<>();
        final Set<Item> plankItems = new HashSet<>();

        BuiltInRegistries.ITEM.getTagOrEmpty(ItemTags.LOGS).forEach(holder -> logItems.add(holder.value()));
        BuiltInRegistries.ITEM.getTagOrEmpty(ItemTags.PLANKS).forEach(holder -> plankItems.add(holder.value()));

        final RecipeManagerAccessor recipeManager = (RecipeManagerAccessor) resources.getRecipeManager();

        // Mutability hacks
        final Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> recipes = new HashMap<>(recipeManager.accessor$getRecipes());
        final Map<ResourceLocation, Recipe<?>> byName = new HashMap<>(recipeManager.accessor$getByName());

        recipeManager.accessor$setRecipes(recipes);
        recipeManager.accessor$setByName(byName);

        final Map<ResourceLocation, Recipe<?>> crafting = new HashMap<>(recipes.get(RecipeType.CRAFTING));
        recipes.put(RecipeType.CRAFTING, crafting);

        final List<Recipe<?>> injectedRecipes = new ArrayList<>();

        for (Recipe<?> recipe : crafting.values())
        {
            if (recipe.getSerializer() != RecipeSerializer.SHAPED_RECIPE && recipe.getSerializer() != RecipeSerializer.SHAPELESS_RECIPE) continue; // Only pure shaped + shapeless recipes
            if (recipe.getIngredients().size() != 1) continue; // With a single ingredient (the log(s) in question)

            final Ingredient log = recipe.getIngredients().get(0);
            final ItemStack[] values = log.getItems();

            if (Arrays.stream(values).anyMatch(item -> !logItems.contains(item.getItem()))) continue; // Where all items in the ingredient belong to the log tag

            final ItemStack result = recipe.getResultItem(registryAccess);

            if (result.isEmpty() || !plankItems.contains(result.getItem())) continue; // Where the output is one of the plank items

            final Item plank = result.getItem();
            final ResourceLocation plankName = BuiltInRegistries.ITEM.getKey(plank);

            // One recipe must use the same ID as the original recipe, so we can override/replace it
            // This avoids any dangling references to the original recipe ID
            // The other one we just add in our namespace

            injectedRecipes.add(sawLogToPlankRecipe(recipe.getId(), ModTags.Items.SAWS, log, plank, 4));
            injectedRecipes.add(sawLogToPlankRecipe(Helpers.identifier("generated/%s_%s".formatted(plankName.getNamespace(), plankName.getPath())), ModTags.Items.WEAK_SAWS, log, plank, 2));
        }

        for (Recipe<?> recipe : injectedRecipes)
        {
            byName.put(recipe.getId(), recipe);
            crafting.put(recipe.getId(), recipe);
        }
    }

    private static Recipe<?> sawLogToPlankRecipe(ResourceLocation id, TagKey<Item> saw, Ingredient log, Item plank, int count)
    {
        return XPlatform.INSTANCE.shapedToolDamagingRecipe(
            id,
            new ShapedRecipe(id, "", CraftingBookCategory.BUILDING, 1, 2, NonNullList.of(
                Ingredient.EMPTY,
                Ingredient.of(saw),
                log
            ), new ItemStack(plank, count)), Ingredient.of(saw));
    }
}
