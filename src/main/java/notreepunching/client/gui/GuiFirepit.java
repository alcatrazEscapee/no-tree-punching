package notreepunching.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import notreepunching.NoTreePunching;
import notreepunching.block.ModBlocks;
import notreepunching.block.tile.TileEntityFirepit;

public class GuiFirepit extends GuiContainer {

    private InventoryPlayer playerInv;
    private final TileEntityFirepit firepit;

    private static final ResourceLocation BG_TEXTURE = new ResourceLocation(NoTreePunching.MODID, "textures/gui/firepit.png");

    public GuiFirepit(Container container, InventoryPlayer playerInv, TileEntityFirepit te) {
        super(container);
        this.playerInv = playerInv;
        this.firepit = te;
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

        // Draw the fire / burn time indicator
        int burnTime = this.firepit.getScaledBurnTicks();
        int cookTime = this.firepit.getScaledCookTime();
        if(burnTime>0){
            drawTexturedModalRect(x+81,y+54-burnTime,176,13-burnTime,14,burnTime);
        }
        if(cookTime>0){
            drawTexturedModalRect(x+77,y+23,190,0,cookTime,16);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String name = NoTreePunching.proxy.localize(ModBlocks.firepit.getUnlocalizedName() + ".name");
        fontRenderer.drawString(name, xSize / 2 - fontRenderer.getStringWidth(name) / 2, 6, 0x404040);
        fontRenderer.drawString(playerInv.getDisplayName().getUnformattedText(), 8, ySize - 94, 0x404040);
    }
}
