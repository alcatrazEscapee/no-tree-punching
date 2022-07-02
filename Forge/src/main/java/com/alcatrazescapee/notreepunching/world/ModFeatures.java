package com.alcatrazescapee.notreepunching.world;

import java.util.List;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import com.alcatrazescapee.notreepunching.platform.RegistryHolder;
import com.alcatrazescapee.notreepunching.platform.RegistryInterface;
import com.alcatrazescapee.notreepunching.platform.XPlatform;

public final class ModFeatures
{
    public static final RegistryInterface<Feature<?>> FEATURES = XPlatform.INSTANCE.registryInterface(Registry.FEATURE);
    public static final RegistryInterface<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = XPlatform.INSTANCE.registryInterface(BuiltinRegistries.CONFIGURED_FEATURE);
    public static final RegistryInterface<PlacedFeature> PLACED_FEATURES = XPlatform.INSTANCE.registryInterface(BuiltinRegistries.PLACED_FEATURE);

    public static final RegistryHolder<LooseRocksFeature> LOOSE_ROCKS = FEATURES.register("loose_rocks", LooseRocksFeature::new);
    public static final RegistryHolder<ConfiguredFeature<?, ?>> CONFIGURED_LOOSE_ROCKS = CONFIGURED_FEATURES.register("loose_rocks", () -> new ConfiguredFeature<>(LOOSE_ROCKS.get(), NoneFeatureConfiguration.INSTANCE));
    public static final RegistryHolder<PlacedFeature> PLACED_LOOSE_ROCKS = PLACED_FEATURES.register("loose_rocks", () -> new PlacedFeature(CONFIGURED_LOOSE_ROCKS.holder(), List.of(CountPlacement.of(5), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE)));
}