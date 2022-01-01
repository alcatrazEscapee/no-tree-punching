/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.world;


import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.common.util.Lazy;

import com.alcatrazescapee.notreepunching.common.ModTags;
import com.alcatrazescapee.notreepunching.common.blocks.ModBlocks;

public class LooseRocksFeature extends Feature<NoneFeatureConfiguration>
{
    private static final Lazy<Map<Block, Supplier<? extends Block>>> LOOSE_ROCK_STONE_LOOKUP = Lazy.of(() -> new ImmutableMap.Builder<Block, Supplier<? extends Block>>()
        .put(Blocks.STONE, ModBlocks.STONE_LOOSE_ROCK)
        .put(Blocks.ANDESITE, ModBlocks.ANDESITE_LOOSE_ROCK)
        .put(Blocks.DIORITE, ModBlocks.DIORITE_LOOSE_ROCK)
        .put(Blocks.GRANITE, ModBlocks.GRANITE_LOOSE_ROCK)
        .put(Blocks.SANDSTONE, ModBlocks.SANDSTONE_LOOSE_ROCK)
        .put(Blocks.RED_SANDSTONE, ModBlocks.RED_SANDSTONE_LOOSE_ROCK)
        .put(Blocks.TERRACOTTA, ModBlocks.RED_SANDSTONE_LOOSE_ROCK)
        .put(Blocks.SAND, ModBlocks.SANDSTONE_LOOSE_ROCK)
        .put(Blocks.RED_SAND, ModBlocks.RED_SANDSTONE_LOOSE_ROCK)
        .build()
    );

    public LooseRocksFeature()
    {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean place(WorldGenLevel worldIn, ChunkGenerator chunkGenerator, Random random, BlockPos pos, NoneFeatureConfiguration config)
    {
        BlockState stateAt = worldIn.getBlockState(pos);
        BlockState stateDown = worldIn.getBlockState(pos.below());
        if (stateAt.isAir() && ModTags.Blocks.LOOSE_ROCK_PLACEABLE_ON.contains(stateDown.getBlock()))
        {
            for (int y = 1; y <= 8; y++)
            {
                BlockPos stonePos = pos.below(y);
                BlockState stoneState = worldIn.getBlockState(stonePos);
                if (LOOSE_ROCK_STONE_LOOKUP.get().containsKey(stoneState.getBlock()))
                {
                    Block looseRockBlock = LOOSE_ROCK_STONE_LOOKUP.get().get(stoneState.getBlock()).get();
                    worldIn.setBlock(pos, looseRockBlock.defaultBlockState(), 3);
                    return true;
                }
            }
        }
        return true;
    }
}