package notreepunching.client.tesr;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import notreepunching.NoTreePunching;
import notreepunching.block.tile.TileEntityGrindstone;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;

public class TESRGrindstone extends TESRBase<TileEntityGrindstone> {

    private static final ResourceLocation texture = new ResourceLocation(NoTreePunching.MODID+":textures/tesr/grindstone.png");

    @Override
    public void render(@Nonnull TileEntityGrindstone tile, double x, double y, double z, float partialTicks, int blockDamageProgress, float alpha) {

        try {
            GlStateManager.pushMatrix();
            GlStateManager.color(1, 1, 1, 1);
            this.bindTexture(texture);
            GlStateManager.disableLighting();

            GlStateManager.translate(x + 0.5d, y, z + 0.5d);
            GL11.glRotatef(tile.getTimer(), 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(-0.5d, 0.0d, -0.5d);

            Tessellator t = Tessellator.getInstance();
            BufferBuilder b = t.getBuffer();

            b.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            drawStone(b);
            drawHandle(b);
            t.draw();
        }
        finally {
            GlStateManager.popMatrix();
        }

    }

    private void drawStone(BufferBuilder b){
        double[][] tops = getVerticesBySide(0.1875, 0.5, 0.1875, 0.8125, 0.6875, 0.8125, "y");
        double[][] sides = getVerticesBySide(0.1875, 0.5, 0.1875, 0.8125, 0.6875, 0.8125, "xz");

        for (double [] v : tops) {
            b.pos(v[0], v[1], v[2]).tex(v[3]*0.625, v[4]*0.625).endVertex();
        }
        for (double [] v : sides) {
            b.pos(v[0], v[1], v[2]).tex(v[3]*0.625, v[4]*0.1875+0.625).endVertex();
        }
    }

    private void drawHandle(BufferBuilder b){
        double[][] tops = getVerticesBySide(0.25, 0.6875, 0.25, 0.3125, 0.8125, 0.3125, "y");
        double[][] sides = getVerticesBySide(0.25, 0.6875, 0.25, 0.3125, 0.8125, 0.3125, "xz");

        for (double [] v : tops) {
            b.pos(v[0], v[1], v[2]).tex(v[3]*0.1875+0.625, v[4]*0.1875).endVertex();
        }
        for (double [] v : sides) {
            b.pos(v[0], v[1], v[2]).tex(v[3]*0.1875+0.625, v[4]*0.375).endVertex();
        }
    }

    @Override
    public boolean isGlobalRenderer(TileEntityGrindstone te) {
        return false;
    }
}
