/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.common.ICrashCallable;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.alcatrazescapee.notreepunching.client.ModGuiHandler;
import com.alcatrazescapee.notreepunching.client.ModSounds;
import com.alcatrazescapee.notreepunching.common.blocks.ModBlocks;
import com.alcatrazescapee.notreepunching.common.capability.CapabilityPlayerItem;
import com.alcatrazescapee.notreepunching.common.items.ModItems;
import com.alcatrazescapee.notreepunching.common.recipe.ModRecipes;
import com.alcatrazescapee.notreepunching.util.HarvestBlockHandler;
import com.alcatrazescapee.notreepunching.util.WoodRecipeHandler;
import com.alcatrazescapee.notreepunching.world.WorldGenRocks;

@Mod(value = NoTreePunching.MOD_ID)
public final class NoTreePunching
{
    public static final String MOD_ID = "notreepunching";

    private static final Logger LOGGER = LogManager.getLogger();

    @Deprecated
    public static Logger getLog()
    {
        return LOGGER;
    }

    public NoTreePunching()
    {
        LOGGER.info("Hello No Tree Punching!");
        LOGGER.debug("If you can see this, debug logging is working.");

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.register(this);

        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);

        //NetworkRegistry.INSTANCE.registerGuiHandler(this, new ModGuiHandler());

        // Pre-Init Managers
        //CapabilityPlayerItem.preInit();
        //ModBlocks.preInit();
        //ModItems.preInit();
        //ModSounds.preInit();
    }

    @SubscribeEvent
    public void setup(FMLCommonSetupEvent event)
    {
        // World gen
        //GameRegistry.registerWorldGenerator(new WorldGenRocks(), 3);

        // Init Managers
        //ModRecipes.init();
        //ModItems.init();

        // Post-Init Managers
        //HarvestBlockHandler.postInit();

        //if (ModConfig.GENERAL.enableAdvancedRecipeReplacement)
        //{
        //    WoodRecipeHandler.postInit();
        //}
        //ModRecipes.postInit();
    }
}