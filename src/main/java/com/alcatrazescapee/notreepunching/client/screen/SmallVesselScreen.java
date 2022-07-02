package com.alcatrazescapee.notreepunching.client.screen;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import com.alcatrazescapee.notreepunching.common.container.SmallVesselContainer;
import com.alcatrazescapee.notreepunching.util.Helpers;

public class SmallVesselScreen extends ModContainerScreen<SmallVesselContainer>
{
    private static final ResourceLocation SMALL_VESSEL_BACKGROUND = Helpers.identifier("textures/gui/small_vessel.png");

    public SmallVesselScreen(SmallVesselContainer container, Inventory inventory, Component title)
    {
        super(container, inventory, title, SMALL_VESSEL_BACKGROUND);
    }
}