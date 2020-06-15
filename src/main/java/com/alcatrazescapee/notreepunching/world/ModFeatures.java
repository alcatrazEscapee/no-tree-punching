/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Copyright (c) 2019. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.world;

import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

public final class ModFeatures
{
    public static final DeferredRegister<Feature<?>> FEATURES = new DeferredRegister<>(ForgeRegistries.FEATURES, MOD_ID);

    public static final RegistryObject<LooseRocksFeature> LOOSE_ROCKS = FEATURES.register("loose_rocks", LooseRocksFeature::new);
}
