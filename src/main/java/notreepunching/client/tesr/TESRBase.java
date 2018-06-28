/*
 *
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 *
 */

package notreepunching.client.tesr;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

abstract class TESRBase<TE extends TileEntity> extends TileEntitySpecialRenderer<TE> {

    // Use this to get vertices for a box from Min - Max point in 3D
    // Pass the string of the axies you want the box to render on ('xz') for no top / bottom, etc.
    // Pass 'xyz' for all vertices
    double[][] getVerticesBySide(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, String axies) {
        double[][] ret = new double[][]{};
        if(axies.contains("x")) { ret = append(ret, getXVertices(minX,minY,minZ,maxX,maxY,maxZ)); }
        if(axies.contains("y")) { ret = append(ret, getYVertices(minX,minY,minZ,maxX,maxY,maxZ)); }
        if(axies.contains("z")) { ret = append(ret, getZVertices(minX,minY,minZ,maxX,maxY,maxZ)); }
        return ret;

    }

    private double[][] getXVertices(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return new double[][] {
                {minX, minY, minZ, 0, 1}, // Main +X Side
                {minX, minY, maxZ, 1, 1},
                {minX, maxY, maxZ, 1, 0},
                {minX, maxY, minZ, 0, 0},

                {maxX, minY, maxZ, 0, 1}, // Main -X Side
                {maxX, minY, minZ, 1, 1},
                {maxX, maxY, minZ, 1, 0},
                {maxX, maxY, maxZ, 0, 0}
        };
    }

    private double[][] getYVertices(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return new double[][] {
                {minX, maxY, minZ, 0, 1}, // Top
                {minX, maxY, maxZ, 1, 1},
                {maxX, maxY, maxZ, 1, 0},
                {maxX, maxY, minZ, 0, 0},

                {minX, minY, maxZ, 0, 1}, // Bottom
                {minX, minY, minZ, 1, 1},
                {maxX, minY, minZ, 1, 0},
                {maxX, minY, maxZ, 0, 0}
        };
    }

    private double[][] getZVertices(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return new double[][] {
                {maxX, minY, minZ, 0, 1}, // Main +Z Side
                {minX, minY, minZ, 1, 1},
                {minX, maxY, minZ, 1, 0},
                {maxX, maxY, minZ, 0, 0},

                {minX, minY, maxZ, 0, 1}, // Main -Z Side
                {maxX, minY, maxZ, 1, 1},
                {maxX, maxY, maxZ, 1, 0},
                {minX, maxY, maxZ, 0, 0}
        };
    }

    private static double[][] append(double[][] a, double[][] b) {
        double[][] result = new double[a.length + b.length][];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    private double[][] drawBoxByVertex(double minX, double minY, double minZ, double maxX, double maxY, double maxZ){
        return new double[][] {
                {minX, maxY, minZ, 0, 1}, // Top
                {minX, maxY, maxZ, 1, 1},
                {maxX, maxY, maxZ, 1, 0},
                {maxX, maxY, minZ, 0, 0},

                {minX, minY, maxZ, 0, 1}, // Bottom
                {minX, minY, minZ, 1, 1},
                {maxX, minY, minZ, 1, 0},
                {maxX, minY, maxZ, 0, 0},

                {minX, minY, minZ, 0, 1}, // Main +X Side
                {minX, minY, maxZ, 1, 1},
                {minX, maxY, maxZ, 1, 0},
                {minX, maxY, minZ, 0, 0},

                {maxX, minY, maxZ, 0, 1}, // Main -X Side
                {maxX, minY, minZ, 1, 1},
                {maxX, maxY, minZ, 1, 0},
                {maxX, maxY, maxZ, 0, 0}
        };
    }

    private double[][] drawCubeByVertex(double minX, double minY, double minZ, double maxX, double maxY, double maxZ){
        return new double[][] {
                {maxX, minY, minZ, 0, 1}, // Main +Z Side
                {minX, minY, minZ, 1, 1},
                {minX, maxY, minZ, 1, 0},
                {maxX, maxY, minZ, 0, 0},

                {minX, minY, maxZ, 0, 1}, // Main -Z Side
                {maxX, minY, maxZ, 1, 1},
                {maxX, maxY, maxZ, 1, 0},
                {minX, maxY, maxZ, 0, 0}
        };
    }
}
