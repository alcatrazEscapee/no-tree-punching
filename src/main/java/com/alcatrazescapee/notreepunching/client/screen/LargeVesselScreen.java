package com.alcatrazescapee.notreepunching.client.screen;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import com.alcatrazescapee.notreepunching.common.container.LargeVesselContainer;
import com.alcatrazescapee.notreepunching.util.Helpers;

public class LargeVesselScreen extends ModContainerScreen<LargeVesselContainer>
{
    private static final ResourceLocation LARGE_VESSEL_BACKGROUND = Helpers.identifier("textures/gui/large_vessel.png");

    public LargeVesselScreen(LargeVesselContainer container, Inventory inventory, Component title)
    {
        super(container, inventory, title, LARGE_VESSEL_BACKGROUND);
    }
}