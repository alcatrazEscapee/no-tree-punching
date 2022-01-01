/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.world;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

public final class ModFeatures
{
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, MOD_ID);

    public static final RegistryObject<LooseRocksFeature> LOOSE_ROCKS = FEATURES.register("loose_rocks", LooseRocksFeature::new);
    public static final Lazy<ConfiguredFeature<?, ?>> LOOSE_ROCKS_CONFIGURED = Lazy.of(() -> Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(MOD_ID, "loose_rocks"),
        LOOSE_ROCKS.get().configured(NoneFeatureConfiguration.INSTANCE)
            .decorated(FeatureDecorator.HEIGHTMAP_WORLD_SURFACE.configured(NoneDecoratorConfiguration.INSTANCE))
            .squared()
            .count(5)));

    public static void setup()
    {
        // Register configured features
        LOOSE_ROCKS_CONFIGURED.get();
    }
}