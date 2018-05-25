package notreepunching.client.tesr;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import notreepunching.NoTreePunching;
import notreepunching.block.tile.TileEntityBellows;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;

public class TESRBellows extends TileEntitySpecialRenderer<TileEntityBellows> {

    private static final ResourceLocation texture = new ResourceLocation(NoTreePunching.MODID+":textures/tesr/bellows.png");
    //private static final ResourceLocation texture_2 = new ResourceLocation(NoTreePunching.MODID,"blocks/bellows_bottom.png");

    @Override
    public void render(@Nonnull TileEntityBellows tile, double x, double y, double z, float partialTicks, int blockDamageProgress, float alpha) {

        try {
            //GL11.glPushMatrix();
            //GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
            GlStateManager.pushMatrix();
            GlStateManager.color(1, 1, 1, 1);
            //Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
            this.bindTexture(texture);
            GlStateManager.disableLighting();
            //GlStateManager.enableBlend();
            //GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);


            GlStateManager.translate(x, y, z);

            Tessellator t = Tessellator.getInstance();
            BufferBuilder b = t.getBuffer();

            double tileY = tile.getHeight();

            b.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            drawMiddle(b, tileY);
            drawTop(b, tileY);
            t.draw();
        }
        finally {
            //GL11.glPopAttrib();
            //GL11.glPopMatrix();
            GlStateManager.popMatrix();
        }
    }

    private void drawMiddle(BufferBuilder b, double y){
        double[][] sides = drawBoxByVertex(0.125, 0.125, 0.125, 0.875, y, 0.875);

        for (double [] v : sides) {
            b.pos(v[0], v[1], v[2]).tex(v[3]*0.5, v[4]*0.5).endVertex();
        }
    }

    private void drawTop(BufferBuilder b, double y){
        double[][] sides = drawBoxByVertex(0, y, 0, 1, y + 0.125, 1);
        double[][] tops = drawCubeByVertex(0, y, 0, 1, y + 0.125, 1);

        for (double [] v : sides) {
            b.pos(v[0], v[1], v[2]).tex(v[3]*0.5+0.5, v[4]*0.0625+0.5).endVertex();
        }
        for (double [] v : tops) {
            b.pos(v[0], v[1], v[2]).tex(v[3]*0.5, v[4]*0.5+0.5).endVertex();
        }
    }

    @Override
    public boolean isGlobalRenderer(TileEntityBellows te) {
        return false;
    }

    private double[][] drawBoxByVertex(double minX, double minY, double minZ, double maxX, double maxY, double maxZ){
        return new double[][] {
                {maxX, minY, minZ, 0, 1}, // Main +Z Side
                {minX, minY, minZ, 1, 1},
                {minX, maxY, minZ, 1, 0},
                {maxX, maxY, minZ, 0, 0},

                {minX, minY, minZ, 0, 1}, // Main +X Side
                {minX, minY, maxZ, 1, 1},
                {minX, maxY, maxZ, 1, 0},
                {minX, maxY, minZ, 0, 0},

                {minX, minY, maxZ, 0, 1}, // Main -Z Side
                {maxX, minY, maxZ, 1, 1},
                {maxX, maxY, maxZ, 1, 0},
                {minX, maxY, maxZ, 0, 0},

                {maxX, minY, maxZ, 0, 1}, // Main -X Side
                {maxX, minY, minZ, 1, 1},
                {maxX, maxY, minZ, 1, 0},
                {maxX, maxY, maxZ, 0, 0}
        };
    }

    private double[][] drawCubeByVertex(double minX, double minY, double minZ, double maxX, double maxY, double maxZ){
        return new double[][] {
                {minX, maxY, minZ, 0, 0}, // Top
                {minX, maxY, maxZ, 0, 1},
                {maxX, maxY, maxZ, 1, 1},
                {maxX, maxY, minZ, 1, 0},

                {minX, minY, maxZ, 1, 1}, // Bottom
                {minX, minY, minZ, 1, 0},
                {maxX, minY, minZ, 0, 0},
                {maxX, minY, maxZ, 0, 1}
        };
    }
}
