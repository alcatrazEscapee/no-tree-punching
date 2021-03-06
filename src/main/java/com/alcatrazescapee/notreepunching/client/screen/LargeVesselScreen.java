/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.client.screen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import com.alcatrazescapee.notreepunching.common.container.LargeVesselContainer;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

public class LargeVesselScreen extends ModContainerScreen<LargeVesselContainer>
{
    private static final ResourceLocation LARGE_VESSEL_BACKGROUND = new ResourceLocation(MOD_ID, "textures/gui/large_vessel.png");

    public LargeVesselScreen(LargeVesselContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
    {
        super(screenContainer, inv, titleIn, LARGE_VESSEL_BACKGROUND);
    }
}