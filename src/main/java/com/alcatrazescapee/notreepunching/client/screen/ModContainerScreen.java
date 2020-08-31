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

import com.mojang.blaze3d.matrix.MatrixStack;
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
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks)
    {
        drawBackground(stack, partialTicks, mouseX, mouseY);
        super.render(stack, mouseX, mouseY, partialTicks);
        drawMouseoverTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(MatrixStack stack, float partialTicks, int mouseX, int mouseY)
    {
        drawDefaultBackground(stack);
    }

    @SuppressWarnings({"ConstantConditions", "deprecation"})
    protected void drawDefaultBackground(MatrixStack stack)
    {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        client.getTextureManager().bindTexture(texture);
        drawTexture(stack, guiLeft, guiTop, 0, 0, xSize, ySize);
    }
}
