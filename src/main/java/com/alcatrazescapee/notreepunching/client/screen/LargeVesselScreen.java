/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.client.screen;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

import com.alcatrazescapee.notreepunching.common.container.LargeVesselContainer;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

public class LargeVesselScreen extends ModContainerScreen<LargeVesselContainer>
{
    private static final ResourceLocation LARGE_VESSEL_BACKGROUND = new ResourceLocation(MOD_ID, "textures/gui/large_vessel.png");

    public LargeVesselScreen(LargeVesselContainer screenContainer, Inventory inv, Component titleIn)
    {
        super(screenContainer, inv, titleIn, LARGE_VESSEL_BACKGROUND);
    }
}