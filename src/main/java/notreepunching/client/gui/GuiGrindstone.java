package notreepunching.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import notreepunching.NoTreePunching;
import notreepunching.block.ModBlocks;
import notreepunching.block.tile.TileEntityGrindstone;

public class GuiGrindstone extends GuiContainer {

    private InventoryPlayer playerInv;
    private final TileEntityGrindstone tile;

    private static final ResourceLocation BG_TEXTURE = new ResourceLocation(NoTreePunching.MODID, "textures/gui/grindstone.png");

    public GuiGrindstone(Container container, InventoryPlayer playerInv, TileEntityGrindstone te) {
        super(container);
        this.playerInv = playerInv;
        this.tile = te;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
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

        // Draw field based properties
        int scaledRotation = (int) Math.round(22d*this.tile.getRotation()/360.0d);
        if(scaledRotation > 0){
            drawTexturedModalRect(x+77,y+31,176,0,scaledRotation, 16);
        }

    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String name = NoTreePunching.proxy.localize(ModBlocks.grindstone.getUnlocalizedName() + ".name");
        fontRenderer.drawString(name, xSize / 2 - fontRenderer.getStringWidth(name) / 2, 6, 0x404040);
        fontRenderer.drawString(playerInv.getDisplayName().getUnformattedText(), 8, ySize - 94, 0x404040);
    }
}
