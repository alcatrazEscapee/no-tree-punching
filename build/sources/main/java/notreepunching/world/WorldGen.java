package notreepunching.world;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import notreepunching.block.ModBlocks;

import java.util.Random;

public class WorldGen {

    /*@Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider){

        // Generate Surface Loose Rocks
        if(world.provider.getDimension() == 0){
            for (int i = 0; i < 3; i++)
            {
                int xCoord = chunkX*16 + random.nextInt(16) + 8;
                int zCoord = chunkZ*16 + random.nextInt(16) + 8;
                if(world.getBiome(new BlockPos(xCoord,1,zCoord))!= Biomes.OCEAN && world.getBiome(new BlockPos(xCoord,1,zCoord))!=Biomes.DEEP_OCEAN && world.getBiome(new BlockPos(xCoord,1,zCoord))!= Biomes.FROZEN_OCEAN) {
                    generateRocks(world, random, xCoord, world.getTopSolidOrLiquidBlock(new BlockPos(xCoord, 1, zCoord)).getY() - 1, zCoord);
                }
            }
        }
    }*/

    @SubscribeEvent
    public void decorateBiome(DecorateBiomeEvent.Post event) {
        World world = event.getWorld();
        Random random = event.getRand();
        int chunkX = event.getPos().getX() >> 4;
        int chunkZ = event.getPos().getZ() >> 4;
        // Generate Surface Loose Rocks
        if(world.provider.getDimension() == 0){
            for (int i = 0; i < 3; i++)
            {
                int xCoord = chunkX*16 + random.nextInt(16) + 8;
                int zCoord = chunkZ*16 + random.nextInt(16) + 8;
                if(world.getBiome(new BlockPos(xCoord,1,zCoord))!= Biomes.OCEAN && world.getBiome(new BlockPos(xCoord,1,zCoord))!=Biomes.DEEP_OCEAN && world.getBiome(new BlockPos(xCoord,1,zCoord))!= Biomes.FROZEN_OCEAN) {
                    generateRocks(world, random, xCoord, world.getTopSolidOrLiquidBlock(new BlockPos(xCoord, 1, zCoord)).getY() - 1, zCoord);
                }
            }
        }
    }

    private boolean generateRocks(World world, Random random, int i, int j, int k) {

        Block upBl = world.getBlockState(new BlockPos(i,j+1,k)).getBlock();
        Block atBl = world.getBlockState(new BlockPos(i,j,k)).getBlock();

        Material atMat = atBl.getMaterial(atBl.getBlockState().getBaseState());

        if ((world.isAirBlock(new BlockPos(i,j+1,k)) || upBl== Blocks.SNOW_LAYER || upBl == Blocks.TALLGRASS || upBl == Blocks.SNOW) &&
                (atMat == Material.GRASS || atMat == Material.ROCK) &&
                atBl.isOpaqueCube(atBl.getDefaultState()))
        {
            world.setBlockState(new BlockPos(i, j+1, k), ModBlocks.looseRock.getDefaultState());
        }
        return true;
    }
}
