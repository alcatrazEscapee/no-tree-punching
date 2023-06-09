package com.alcatrazescapee.notreepunching.world;

import java.util.Map;
import java.util.function.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import com.alcatrazescapee.notreepunching.common.ModTags;
import com.alcatrazescapee.notreepunching.common.blocks.ModBlocks;

public class LooseRocksFeature extends Feature<NoneFeatureConfiguration>
{
    private static final Supplier<Map<Block, Supplier<? extends Block>>> LOOSE_ROCK_STONE_LOOKUP = Suppliers.memoize(() -> new ImmutableMap.Builder<Block, Supplier<? extends Block>>()
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
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
    {
        final WorldGenLevel level = context.level();
        final BlockPos pos = context.origin();

        final BlockState stateAt = level.getBlockState(pos);
        final BlockState stateDown = level.getBlockState(pos.below());
        if (stateAt.isAir() && stateDown.is(ModTags.Blocks.LOOSE_ROCK_PLACEABLE_ON))
        {
            for (int y = 1; y <= 8; y++)
            {
                final BlockPos stonePos = pos.below(y);
                final BlockState stoneState = level.getBlockState(stonePos);
                if (LOOSE_ROCK_STONE_LOOKUP.get().containsKey(stoneState.getBlock()))
                {
                    final Block looseRockBlock = LOOSE_ROCK_STONE_LOOKUP.get().get(stoneState.getBlock()).get();
                    level.setBlock(pos, looseRockBlock.defaultBlockState(), 3);
                    return true;
                }
            }
        }
        return false;
    }
}