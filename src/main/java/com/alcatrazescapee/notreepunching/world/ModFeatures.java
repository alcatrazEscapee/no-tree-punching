/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.world;

import java.util.function.Supplier;

import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import com.alcatrazescapee.notreepunching.util.Helpers;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

public final class ModFeatures
{
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, MOD_ID);

    public static final RegistryObject<LooseRocksFeature> LOOSE_ROCKS = FEATURES.register("loose_rocks", LooseRocksFeature::new);

    public static final Lazy<ConfiguredFeature<?, ?>> LOOSE_ROCKS_CONFIGURED = register(BuiltinRegistries.CONFIGURED_FEATURE, "loose_rocks",
        () -> LOOSE_ROCKS.get().configured(NoneFeatureConfiguration.INSTANCE));

    public static final Lazy<PlacedFeature> LOOSE_ROCKS_PLACED = register(BuiltinRegistries.PLACED_FEATURE, "loose_rocks", () -> LOOSE_ROCKS_CONFIGURED.get().placed(
        CountPlacement.of(5),
        InSquarePlacement.spread(),
        PlacementUtils.HEIGHTMAP_WORLD_SURFACE
    ));

    public static void setup()
    {
        LOOSE_ROCKS_CONFIGURED.get();
        LOOSE_ROCKS_PLACED.get();
    }

    private static <T> Lazy<T> register(Registry<? super T> registry, String name, Supplier<T> factory)
    {
        return Lazy.of(() -> Registry.register(registry, Helpers.identifier(name), factory.get()));
    }
}