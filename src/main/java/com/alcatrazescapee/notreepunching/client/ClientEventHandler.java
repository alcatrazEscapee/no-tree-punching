/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.client;

import net.minecraft.client.gui.screens.MenuScreens;

import com.alcatrazescapee.notreepunching.client.screen.LargeVesselScreen;
import com.alcatrazescapee.notreepunching.client.screen.SmallVesselScreen;
import com.alcatrazescapee.notreepunching.common.container.ModContainers;

public final class ClientEventHandler
{
    public static void clientSetup()
    {
        MenuScreens.register(ModContainers.LARGE_VESSEL.get(), LargeVesselScreen::new);
        MenuScreens.register(ModContainers.SMALL_VESSEL.get(), SmallVesselScreen::new);
    }
}