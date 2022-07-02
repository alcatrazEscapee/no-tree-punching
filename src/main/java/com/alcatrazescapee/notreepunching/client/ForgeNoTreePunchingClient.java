package com.alcatrazescapee.notreepunching.client;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public final class ForgeNoTreePunchingClient
{
    public static void clientSetup()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener((FMLClientSetupEvent event) -> event.enqueueWork(ClientEventHandler::clientSetup));
    }
}
