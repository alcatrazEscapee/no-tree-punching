package notreepunching.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static notreepunching.block.BlockCharcoalPile.LAYERS;

import java.util.Random;

public class ParticleGenerator {

    public static void firepitParticle(World world, BlockPos pos){
        Random rand = new Random();
        double x = pos.getX()+0.5d+0.05d*rand.nextGaussian();
        double y = pos.getY()+0.1d;
        double z = pos.getZ()+0.5d+0.05d*rand.nextGaussian();
        Minecraft.getMinecraft().effectRenderer.addEffect(new FirepitParticle(world, x, y, z, 0d, 0.008d, 0d));
    }

    public static void forgeParticle(World world, BlockPos pos){
        Random rand = new Random();
        double x = pos.getX()+1d*rand.nextFloat();
        double y = pos.getY()+world.getBlockState(pos).getValue(LAYERS)*0.125D;
        double z = pos.getZ()+1d*rand.nextFloat();
        Minecraft.getMinecraft().effectRenderer.addEffect(new FirepitParticle(world, x, y, z, 0d, 0.008d, 0d));
    }

    public static void firestarterParticle(World world, BlockPos pos){
        Random rand = new Random();
        double x = pos.getX()+0.5d+0.05d*rand.nextGaussian();
        double y = pos.getY()+0.1d;
        double z = pos.getZ()+0.5d+0.05d*rand.nextGaussian();
        Minecraft.getMinecraft().effectRenderer.addEffect(new FirepitSmokeParticle(world,x,y,z,0.01d*rand.nextGaussian(),0.03d,0.01d*rand.nextGaussian()));
    }
}
