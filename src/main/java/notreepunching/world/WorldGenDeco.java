/*
 *
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 *
 */

package notreepunching.world;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import notreepunching.block.BlockRock;
import notreepunching.block.ModBlocks;
import notreepunching.config.ModConfig;

import java.util.Random;

import static notreepunching.block.BlockRock.EnumMineralType.*;
import static notreepunching.block.BlockRock.TYPE;


public class WorldGenDeco {

    @SubscribeEvent
    public void decorateBiome(DecorateBiomeEvent.Post event) {
        World world = event.getWorld();
        Random random = event.getRand();
        ChunkPos pos = event.getChunkPos();
        // Generate Surface Loose Rocks
        if(world.provider.getDimension() == 0 && ModConfig.World.LOOSE_ROCKS){

            for (int i = 0; i < ModConfig.World.LOOSE_ROCKS_FREQUENCY; i++) {
                int xCoord = pos.x * 16 + random.nextInt(16) + 8;
                int zCoord = pos.z * 16 + random.nextInt(16) + 8;
                if(world.getBiome(new BlockPos(xCoord,1,zCoord))!= Biomes.OCEAN && world.getBiome(new BlockPos(xCoord,1,zCoord))!=Biomes.DEEP_OCEAN && world.getBiome(new BlockPos(xCoord,1,zCoord))!= Biomes.FROZEN_OCEAN) {
                    generateRocks(world, random, xCoord, world.getTopSolidOrLiquidBlock(new BlockPos(xCoord, 1, zCoord)).getY() - 1, zCoord);
                }
            }
        }
    }

    private void generateRocks(World world, Random random, int i, int j, int k) {

        IBlockState stateUp = world.getBlockState(new BlockPos(i, j + 1, k));
        IBlockState stateAt = world.getBlockState(new BlockPos(i, j, k));
        Block downBl = world.getBlockState(new BlockPos(i,j-5,k)).getBlock();

        Material atMat = stateAt.getMaterial();

        if (stateUp.getBlock() == Blocks.AIR && stateAt.isNormalCube() &&
                (atMat == Material.GRASS || atMat == Material.ROCK || atMat == Material.SAND)) {
            BlockRock.EnumMineralType type;
            if(downBl == Blocks.STONE){
                switch(downBl.getMetaFromState(world.getBlockState(new BlockPos(i,j-5,k)))){
                    case 1:
                        type = GRANITE;
                        break;
                    case 3:
                        type = DIORITE;
                        break;
                    case 5:
                        type = ANDESITE;
                        break;
                    default:
                        type = STONE;
                }
            } else {
                switch(downBl.getUnlocalizedName()){
                    case "quark:marble":
                        type = MARBLE;
                        break;
                    case "quark:limestone":
                        type = LIMESTONE;
                        break;
                    case "rustic:slate":
                        type = SLATE;
                        break;
                    default:
                        type = STONE;
                }
            }
            world.setBlockState(new BlockPos(i, j+1, k), ModBlocks.looseRock.getDefaultState().withProperty(TYPE,type));
        }
    }
}
