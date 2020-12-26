/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.world;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

public final class ModFeatures
{
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, MOD_ID);

    public static final RegistryObject<LooseRocksFeature> LOOSE_ROCKS = FEATURES.register("loose_rocks", LooseRocksFeature::new);
    public static final Lazy<ConfiguredFeature<?, ?>> LOOSE_ROCKS_CONFIGURED = Lazy.of(() -> Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(MOD_ID, "loose_rocks"),
        LOOSE_ROCKS.get().configured(NoFeatureConfig.INSTANCE)
            .decorated(Placement.HEIGHTMAP_WORLD_SURFACE.configured(NoPlacementConfig.INSTANCE))
            .squared()
            .count(5)));

    public static void setup()
    {
        // Register configured features
        LOOSE_ROCKS_CONFIGURED.get();
    }
}