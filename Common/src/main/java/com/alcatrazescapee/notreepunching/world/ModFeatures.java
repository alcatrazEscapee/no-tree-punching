package com.alcatrazescapee.notreepunching.world;

import com.alcatrazescapee.notreepunching.util.Helpers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.Feature;

import com.alcatrazescapee.notreepunching.platform.RegistryHolder;
import com.alcatrazescapee.notreepunching.platform.RegistryInterface;
import com.alcatrazescapee.notreepunching.platform.XPlatform;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

@SuppressWarnings("unused")
public final class ModFeatures
{
    public static final RegistryInterface<Feature<?>> FEATURES = XPlatform.INSTANCE.registryInterface(BuiltInRegistries.FEATURE);

    public static final RegistryHolder<LooseRocksFeature> LOOSE_ROCKS = FEATURES.register("loose_rocks", LooseRocksFeature::new);
    public static final ResourceKey<PlacedFeature> LOOSE_ROCKS_KEY = ResourceKey.create(Registries.PLACED_FEATURE, LOOSE_ROCKS.id());
}