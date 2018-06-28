/*
 *
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 *
 */

package notreepunching.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import notreepunching.NoTreePunching;
import notreepunching.block.ModBlocks;
import notreepunching.block.tile.TileEntityBlastFurnace;

public class GuiBlastFurnace extends GuiContainer{

    private InventoryPlayer playerInv;
    private final TileEntityBlastFurnace te;

    private static final ResourceLocation BG_TEXTURE = new ResourceLocation(NoTreePunching.MODID, "textures/gui/blast_furnace.png");

    public GuiBlastFurnace(Container container, InventoryPlayer playerInv, TileEntityBlastFurnace te) {
        super(container);
        this.playerInv = playerInv;
        this.te = te;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1, 1, 1, 1);
        mc.getTextureManager().bindTexture(BG_TEXTURE);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        // Draw the fire / burn time indicator
        int burnTime = this.te.getScaledFuelTicks();
        int charcoal = this.te.getScaledCharcoal();
        int temp = this.te.getScaledTemp();
        int cookTime = this.te.getScaledCookTicks();
        if(burnTime>0){
            drawTexturedModalRect(x+71,y+59-burnTime,176,14-burnTime,14,burnTime);
        }
        if(charcoal>0){
            drawTexturedModalRect(x+57, y+67-charcoal, 223, 48-charcoal, 8, charcoal);
        }
        if(temp>0){
            drawTexturedModalRect(x+32,y+54-temp,190,30-temp,10,temp);
        }
        if(cookTime > 0){
            drawTexturedModalRect(x+96,y+35,200,0,cookTime, 16);
        }

    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String name = NoTreePunching.proxy.localize(ModBlocks.blastFurnace.getUnlocalizedName() + ".name");
        fontRenderer.drawString(name, xSize / 2 - fontRenderer.getStringWidth(name) / 2, 6, 0x404040);
        fontRenderer.drawString(playerInv.getDisplayName().getUnformattedText(), 8, ySize - 94, 0x404040);
    }
}
