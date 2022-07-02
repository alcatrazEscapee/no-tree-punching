package com.alcatrazescapee.notreepunching.client;

import net.fabricmc.api.ClientModInitializer;

public final class FabricNoTreePunchingClient implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        ClientEventHandler.clientSetup();
    }
}
