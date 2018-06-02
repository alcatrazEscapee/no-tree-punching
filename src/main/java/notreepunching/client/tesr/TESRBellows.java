package notreepunching.client.tesr;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import notreepunching.NoTreePunching;
import notreepunching.block.tile.TileEntityBellows;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;

public class TESRBellows extends TESRBase<TileEntityBellows> {

    private static final ResourceLocation texture = new ResourceLocation(NoTreePunching.MODID+":textures/tesr/bellows.png");

    @Override
    public void render(@Nonnull TileEntityBellows tile, double x, double y, double z, float partialTicks, int blockDamageProgress, float alpha) {

        try {
            GlStateManager.pushMatrix();
            GlStateManager.color(1, 1, 1, 1);
            this.bindTexture(texture);
            GlStateManager.disableLighting();

            GlStateManager.translate(x + 0.5d, y, z + 0.5d);
            GL11.glRotatef(180.0F-90.0F * tile.getFacing(), 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(-0.5d, 0.0d, -0.5d);

            Tessellator t = Tessellator.getInstance();
            BufferBuilder b = t.getBuffer();

            // Don't fucking touch this shit on pain of death: it works, somehow.
            // TODO: make this render less of a clusterfuck
            double tileY = 1-tile.getHeight()+0.075;

            b.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            drawMiddle(b, tileY);
            drawTop(b, tileY);
            t.draw();
        }
        finally {
            GlStateManager.popMatrix();
        }
    }

    private void drawMiddle(BufferBuilder b, double y){
        double[][] sides = getVerticesBySide(0.125, 0.875, y, 0.875, 0.125, 0.125, "xy");

        for (double [] v : sides) {
            b.pos(v[0], v[1], v[2]).tex(v[3]*0.5, v[4]*0.5).endVertex();
        }
    }

    private void drawTop(BufferBuilder b, double y){
        double[][] sides = getVerticesBySide(0, 1, 0.125+y, 1, 0, y,"xy");
        double[][] tops = getVerticesBySide(0, 1, 0.125+y, 1, 0, y, "z");

        for (double [] v : sides) {
            b.pos(v[0], v[1], v[2]).tex(v[3]*0.0625+0.5, v[4]*0.5+0.5).endVertex();
        }
        for (double [] v : tops) {
            b.pos(v[0], v[1], v[2]).tex(v[3]*0.5, v[4]*0.5+0.5).endVertex();
        }
    }

    @Override
    public boolean isGlobalRenderer(TileEntityBellows te) {
        return false;
    }
}
