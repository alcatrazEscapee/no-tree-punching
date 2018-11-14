/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.alcatrazescapee.notreepunching.util.Util;

@SideOnly(Side.CLIENT)
public class ParticleManager
{
    public static void generateFirePitFlame(World world, BlockPos pos)
    {
        if (Util.RNG.nextFloat() < 0.3)
        {
            generateFirePitSmoke(world, pos);
        }
        Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleFire(world,
                pos.getX() + 0.5d + 0.15d * fastGaussian(),
                pos.getY() + 0.1d,
                pos.getZ() + 0.5d + 0.15d * fastGaussian(),
                0d,
                0.008d,
                0d));
    }

    public static void generateFirePitSmoke(World world, BlockPos pos)
    {
        Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleSmoke(world,
                pos.getX() + 0.5d + 0.1d * fastGaussian(),
                pos.getY() + 0.1d,
                pos.getZ() + 0.5d + 0.1d * fastGaussian(),
                0.01d * fastGaussian(),
                0.05d,
                0.01d * fastGaussian()));
    }

    public static void generateFireStarterSmoke(World world, Vec3d vec)
    {
        Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleSmoke(world,
                vec.x + 0.05d * fastGaussian(),
                vec.y,
                vec.z + 0.05d * fastGaussian(),
                0.01d * fastGaussian(),
                0.03d,
                0.01d * fastGaussian()));
    }

    private static double fastGaussian()
    {
        return Util.RNG.nextFloat() - Util.RNG.nextFloat();
    }
}
