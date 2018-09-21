/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.world;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import com.alcatrazescapee.alcatrazcore.util.CoreHelpers;
import com.alcatrazescapee.notreepunching.ModConfig;
import com.alcatrazescapee.notreepunching.common.blocks.BlockRock;
import com.alcatrazescapee.notreepunching.util.types.Stone;

public class WorldGenRocks implements IWorldGenerator
{
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
        if (world.provider != null && world.provider.getDimension() == 0 && ModConfig.GENERAL.looseRocksGeneration)
        {
            for (int i = 0; i < ModConfig.GENERAL.looseRocksFrequency; i++)
            {
                int xCoord = chunkX * 16 + random.nextInt(16) + 8;
                int zCoord = chunkZ * 16 + random.nextInt(16) + 8;
                if (random.nextFloat() < 0.5)
                {
                    Stone stone = Stone.getRandom(random);
                    placeRock(world, CoreHelpers.getTopSolidBlock(world, new BlockPos(xCoord, 0, zCoord)).up(), stone);
                }
            }
        }
    }

    private void placeRock(World world, BlockPos pos, Stone stone)
    {
        IBlockState state = world.getBlockState(pos);
        if (!state.getMaterial().isLiquid() && state.getBlock().isReplaceable(world, pos) && !state.isOpaqueCube() && world.getBlockState(pos.down()).isOpaqueCube())
            world.setBlockState(pos, BlockRock.get(stone).getDefaultState());
    }
}
