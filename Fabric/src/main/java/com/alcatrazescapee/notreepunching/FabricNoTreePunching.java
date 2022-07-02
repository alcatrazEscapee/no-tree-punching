package com.alcatrazescapee.notreepunching;

import net.fabricmc.api.ModInitializer;

public final class FabricNoTreePunching implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        NoTreePunching.earlySetup();
        NoTreePunching.lateSetup();

        // todo: event handlers
    }
}
