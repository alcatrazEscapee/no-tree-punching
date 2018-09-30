/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching;

import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.alcatrazescapee.notreepunching.client.ModGuiHandler;
import com.alcatrazescapee.notreepunching.client.ModSounds;
import com.alcatrazescapee.notreepunching.common.blocks.ModBlocks;
import com.alcatrazescapee.notreepunching.common.capability.CapabilityPlayerItem;
import com.alcatrazescapee.notreepunching.common.items.ModItems;
import com.alcatrazescapee.notreepunching.common.recipe.ModRecipes;
import com.alcatrazescapee.notreepunching.util.HarvestBlockHandler;
import com.alcatrazescapee.notreepunching.world.WorldGenRocks;

@Mod(modid = ModConstants.MOD_ID, version = ModConstants.VERSION, dependencies = ModConstants.DEPENDENCIES, useMetadata = true)
public final class NoTreePunching
{
    @Mod.Instance
    private static NoTreePunching instance;
    private static Logger logger;

    public static NoTreePunching getInstance()
    {
        return instance;
    }

    public static Logger getLog()
    {
        return logger;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        logger.debug("If you can see this, debug logging is working :)");

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new ModGuiHandler());

        // Pre-Init Managers
        CapabilityPlayerItem.preInit();
        ModBlocks.preInit();
        ModItems.preInit();
        ModSounds.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        // World gen
        GameRegistry.registerWorldGenerator(new WorldGenRocks(), 3);

        // Init Managers
        ModRecipes.init();
        ModItems.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        // Post-Init Managers
        HarvestBlockHandler.postInit();
        ModRecipes.postInit();
    }
}


