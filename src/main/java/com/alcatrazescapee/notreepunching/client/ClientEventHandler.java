/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.client;

import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.alcatrazescapee.notreepunching.client.screen.LargeVesselScreen;
import com.alcatrazescapee.notreepunching.client.screen.SmallVesselScreen;
import com.alcatrazescapee.notreepunching.common.container.ModContainers;

public final class ClientEventHandler
{
    public static void init()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientEventHandler::clientSetup);
    }

    public static void clientSetup(FMLClientSetupEvent event)
    {
        ScreenManager.register(ModContainers.LARGE_VESSEL.get(), LargeVesselScreen::new);
        ScreenManager.register(ModContainers.SMALL_VESSEL.get(), SmallVesselScreen::new);
    }
}