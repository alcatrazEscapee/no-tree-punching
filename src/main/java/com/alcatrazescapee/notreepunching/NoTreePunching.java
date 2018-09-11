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
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

import com.alcatrazescapee.notreepunching.client.ModSounds;
import com.alcatrazescapee.notreepunching.common.blocks.ModBlocks;
import com.alcatrazescapee.notreepunching.common.capability.CapabilityPlayerItem;
import com.alcatrazescapee.notreepunching.common.items.ModItems;

@Mod(modid = NoTreePunching.MOD_ID, version = NoTreePunching.VERSION, dependencies = NoTreePunching.DEPENDENCIES, useMetadata = true)
public class NoTreePunching
{
    public static final String MOD_ID = "notreepunching";
    public static final String MOD_NAME = "No Tree Punching";

    // Versioning / Dependencies
    public static final String VERSION = "GRADLE:VERSION";
    public static final String FORGE_REQUIRED = "required-after:forge@[GRADLE:FORGE_VERSION,15.0.0.0);";
    public static final String ALC_CORE_REQUIRED = "required-after:alcatrazcore@[0.1.0,2.0.0);";
    public static final String DEPENDENCIES = FORGE_REQUIRED + ALC_CORE_REQUIRED;

    @Mod.Instance
    private static NoTreePunching instance;
    private static Logger logger;
    private static SimpleNetworkWrapper network;

    public static NoTreePunching getInstance()
    {
        return instance;
    }

    public static Logger getLog()
    {
        return logger;
    }

    public static SimpleNetworkWrapper getNetwork()
    {
        return network;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        logger.debug("If you can see this, debug logging is working :)");

        //NetworkRegistry.instance.registerGuiHandler(this, new ModGuiHandler());
        network = NetworkRegistry.INSTANCE.newSimpleChannel("notreepunching");

        CapabilityPlayerItem.preInit();

        // Bellows
        //network.registerMessage(PacketUpdateBellows.Handler.class, PacketUpdateBellows.class, ++id, Side.CLIENT);
        //network.registerMessage(PacketRequestBellows.Handler.class, PacketRequestBellows.class, ++id, Side.SERVER);
        // Grindstone
        //network.registerMessage(PacketUpdateGrindstone.Handler.class, PacketUpdateGrindstone.class, ++id, Side.CLIENT);
        //network.registerMessage(PacketRequestGrindstone.Handler.class, PacketRequestGrindstone.class, ++id, Side.SERVER);

        ModBlocks.preInit();
        ModItems.preInit();
        ModSounds.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        // Register World Generation
        //MinecraftForge.EVENT_BUS.register(new WorldGenDeco());
        //GameRegistry.registerWorldGenerator(new WorldGenOres(), 3);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        // Recipes Post init
        //ModEventHandler.postInit();

        //ModCreativeTabs.ITEMS_TAB.setTabItem(ItemRock.get(Stone.STONE));
        //ModCreativeTabs.TOOLS_TAB.setTabItem(ModItems.crudePick);
    }
}


