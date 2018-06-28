/*
 *
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 *
 */

package notreepunching.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;

public class FirepitParticle extends Particle {

    FirepitParticle(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);

        setParticleTextureIndex(48);

        this.motionX = xSpeedIn;
        this.motionY = ySpeedIn;
        this.motionZ = zSpeedIn;

        particleMaxAge += 80;
    }

    @Override
    public void onUpdate() {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;

        move(motionX, motionY, motionZ);
        this.motionY *= 0.97d;
        if (this.particleMaxAge-- <= 0) {
            this.setExpired();
        }
    }
}

