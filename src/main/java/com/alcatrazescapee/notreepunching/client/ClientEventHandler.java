/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Copyright (c) 2019. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.client;

import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import com.alcatrazescapee.notreepunching.client.screen.LargeVesselScreen;
import com.alcatrazescapee.notreepunching.client.screen.SmallVesselScreen;
import com.alcatrazescapee.notreepunching.common.container.ModContainers;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientEventHandler
{
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event)
    {
        ScreenManager.registerFactory(ModContainers.LARGE_VESSEL.get(), LargeVesselScreen::new);
        ScreenManager.registerFactory(ModContainers.SMALL_VESSEL.get(), SmallVesselScreen::new);
    }
}
