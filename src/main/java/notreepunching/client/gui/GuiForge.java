package notreepunching.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import notreepunching.NoTreePunching;
import notreepunching.block.ModBlocks;
import notreepunching.block.forge.TileEntityForge;

public class GuiForge extends GuiContainer {

    private InventoryPlayer playerInv;
    private final TileEntityForge te;

    private static final ResourceLocation BG_TEXTURE = new ResourceLocation(NoTreePunching.MODID, "textures/gui/forge.png");

    public GuiForge(Container container, InventoryPlayer playerInv, TileEntityForge te) {
        super(container);
        this.playerInv = playerInv;
        this.te = te;
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
        int burnTime = this.te.getScaledFuelTicks();
        int temp = this.te.getScaledTemp();
        int fuel = (this.te.getBlockMetadata() % 8 + 1)*2;
        int cookTime = this.te.getScaledCookTicks();
        if(burnTime>0){
            drawTexturedModalRect(x+80,y+56-burnTime,176,14-burnTime,14,burnTime);
        }
        if(temp>0){
            drawTexturedModalRect(x+24,y+55-temp,190,30-temp,10,temp);
        }
        if(fuel > 0){
            drawTexturedModalRect(x+80, y+75-fuel,223,16-fuel,16,fuel);
        }
        if(cookTime > 0){
            drawTexturedModalRect(x+77,y+24,200,0,cookTime, 16);
        }

    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String name = NoTreePunching.proxy.localize(ModBlocks.charcoalPile.getUnlocalizedName() + ".name");
        fontRenderer.drawString(name, xSize / 2 - fontRenderer.getStringWidth(name) / 2, 6, 0x404040);
        fontRenderer.drawString(playerInv.getDisplayName().getUnformattedText(), 8, ySize - 94, 0x404040);
    }
}
