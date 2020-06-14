/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Copyright (c) 2019. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.alcatrazescapee.notreepunching.client.ModSounds;
import com.alcatrazescapee.notreepunching.common.blocks.ModBlocks;
import com.alcatrazescapee.notreepunching.common.container.ModContainers;
import com.alcatrazescapee.notreepunching.common.items.ModItems;
import com.alcatrazescapee.notreepunching.common.tile.ModTileEntities;
import com.alcatrazescapee.notreepunching.util.MaterialHacks;

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

        ModSounds.SOUNDS.register(modEventBus);

        Config.init();
    }

    @SubscribeEvent
    public void setup(FMLCommonSetupEvent event)
    {
        LOGGER.info("Setup");

        MaterialHacks.setup();

        // World gen
        // todo: world generation register
        //GameRegistry.registerWorldGenerator(new WorldGenRocks(), 3);
    }

    @SubscribeEvent
    public void onConfigReload(ModConfig.Reloading event)
    {
        LOGGER.info("Config Reloading - Invalidating Caches");
        Config.onConfigReloading();
    }
}