package com.alcatrazescapee.notreepunching;

import java.util.Arrays;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;

import com.alcatrazescapee.notreepunching.world.ModFeatures;

public final class FabricNoTreePunching implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        NoTreePunching.earlySetup();
        NoTreePunching.lateSetup();

        if (Config.INSTANCE.enableLooseRocksWorldGen.get())
        {
            BiomeModifications.addFeature(
                BiomeSelectors.categories(Arrays.stream(Biome.BiomeCategory.values()).filter(EventHandler::hasLooseRocks).toArray(Biome.BiomeCategory[]::new)),
                GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
                ModFeatures.PLACED_LOOSE_ROCKS.key()
            );
        }
    }
}
