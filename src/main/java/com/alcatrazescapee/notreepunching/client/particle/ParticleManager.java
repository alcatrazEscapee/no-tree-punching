/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.alcatrazescapee.notreepunching.ModConstants;

@SideOnly(Side.CLIENT)
public class ParticleManager
{
    public static void generateFirePitParticle(World world, BlockPos pos)
    {
        Minecraft.getMinecraft().effectRenderer.addEffect(
                new ParticleFirePit(world, pos.getX() + 0.5d + 0.05d * ModConstants.RNG.nextGaussian(),
                        pos.getY() + 0.1d, pos.getZ() + 0.5d + 0.05d * ModConstants.RNG.nextGaussian(),
                        0d, 0.008d, 0d));
    }

    public static void generateFireStarterParticle(World world, BlockPos pos)
    {
        Minecraft.getMinecraft().effectRenderer.addEffect(
                new ParticleFireStarter(world, pos.getX() + 0.5d + 0.05d * ModConstants.RNG.nextGaussian(),
                        pos.getY() + 0.1d, pos.getZ() + 0.5d + 0.05d * ModConstants.RNG.nextGaussian(),
                        0.01d * ModConstants.RNG.nextGaussian(), 0.03d, 0.01d * ModConstants.RNG.nextGaussian()));
    }
}
