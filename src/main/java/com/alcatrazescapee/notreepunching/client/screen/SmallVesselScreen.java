package com.alcatrazescapee.notreepunching.client.screen;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import com.alcatrazescapee.notreepunching.common.container.SmallVesselContainer;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

public class SmallVesselScreen extends ModContainerScreen<SmallVesselContainer>
{
    private static final ResourceLocation SMALL_VESSEL_BACKGROUND = new ResourceLocation(MOD_ID, "textures/gui/small_vessel.png");

    public SmallVesselScreen(SmallVesselContainer screenContainer, Inventory inv, Component titleIn)
    {
        super(screenContainer, inv, titleIn, SMALL_VESSEL_BACKGROUND);
    }
}