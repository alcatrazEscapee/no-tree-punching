package com.alcatrazescapee.notreepunching.client.screen;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

/**
 * Simple screen for a container
 */
public class ModContainerScreen<C extends AbstractContainerMenu> extends AbstractContainerScreen<C>
{
    protected final ResourceLocation texture;

    public ModContainerScreen(C screenContainer, Inventory inv, Component titleIn, ResourceLocation texture)
    {
        super(screenContainer, inv, titleIn);
        this.texture = texture;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks)
    {
        renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        renderTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY)
    {
        renderDefaultBackground(stack);
    }

    protected void renderDefaultBackground(PoseStack stack)
    {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, this.texture);

        blit(stack, leftPos, topPos, 0, 0, 0, imageWidth, imageHeight, 256, 256);
    }
}