/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Copyright (c) 2019. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

import com.alcatrazescapee.notreepunching.client.ModSounds;
import com.alcatrazescapee.notreepunching.common.blocks.ModBlocks;
import com.alcatrazescapee.notreepunching.common.container.ModContainers;
import com.alcatrazescapee.notreepunching.common.items.ModItems;
import com.alcatrazescapee.notreepunching.common.tileentity.ModTileEntities;
import com.alcatrazescapee.notreepunching.util.MaterialHacks;
import com.alcatrazescapee.notreepunching.world.ModFeatures;

@Mod(value = NoTreePunching.MOD_ID)
public final class NoTreePunching
{
    public static final String MOD_ID = "notreepunching";

    private static final Logger LOGGER = LogManager.getLogger();

    public NoTreePunching()
    {
        LOGGER.info("Hello No Tree Punching!");
        LOGGER.debug("If you can see this, debug logging is working.");

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.register(this);

        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModTileEntities.TILE_ENTITIES.register(modEventBus);
        ModContainers.CONTAINERS.register(modEventBus);
        ModFeatures.FEATURES.register(modEventBus);

        ModSounds.SOUNDS.register(modEventBus);

        Config.init();
    }

    @SubscribeEvent
    @SuppressWarnings("deprecation")
    public void setup(FMLCommonSetupEvent event)
    {
        LOGGER.info("Setup");

        MaterialHacks.setup();

        // Forge: if you deprecate something, ya gotta provide some other solution. Like seriously.
        DeferredWorkQueue.runLater(() -> {
            if (Config.COMMON.enableLooseRockSpawning.get())
            {
                for (Biome biome : ForgeRegistries.BIOMES.getValues())
                {
                    biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, ModFeatures.LOOSE_ROCKS.get().withConfiguration(NoFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(Config.COMMON.looseRockFrequency.get()))));
                }
            }
        });
    }
}