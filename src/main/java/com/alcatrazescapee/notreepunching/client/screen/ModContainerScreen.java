/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.client.screen;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import com.mojang.blaze3d.systems.RenderSystem;

/**
 * Simple screen for a container
 */
public class ModContainerScreen<C extends Container> extends ContainerScreen<C>
{
    protected final ResourceLocation texture;

    public ModContainerScreen(C screenContainer, PlayerInventory inv, ITextComponent titleIn, ResourceLocation texture)
    {
        super(screenContainer, inv, titleIn);
        this.texture = texture;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        drawDefaultBackground();
    }

    @SuppressWarnings("ConstantConditions")
    protected void drawDefaultBackground()
    {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bindTexture(texture);
        drawRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    /**
     * {@link net.minecraft.client.gui.AbstractGui#blit(int, int, int, int, int, int)} except with sane parameter names
     */
    protected void drawRect(int x, int y, int u, int v, int width, int height)
    {
        blit(x, y, u, v, width, height);
    }
}
