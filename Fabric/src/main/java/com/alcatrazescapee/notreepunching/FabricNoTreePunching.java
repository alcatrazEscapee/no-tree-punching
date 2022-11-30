package com.alcatrazescapee.notreepunching;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.world.level.levelgen.GenerationStep;

import com.alcatrazescapee.notreepunching.world.ModFeatures;

public final class FabricNoTreePunching implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        NoTreePunching.earlySetup();
        NoTreePunching.lateSetup();

        if (Config.INSTANCE.enableLooseRocksWorldGen.getAsBoolean())
        {
            BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
                ModFeatures.PLACED_LOOSE_ROCKS.key()
            );
        }

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, dedicated) -> EventHandler.registerCommands(dispatcher));
    }
}
