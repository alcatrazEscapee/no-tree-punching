package com.alcatrazescapee.notreepunching.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PotteryBlock extends Block
{
    public static final Material BREAKABLE_CLAY = new Material(MaterialColor.CLAY, false, false, true, true, false, false, PushReaction.DESTROY);

    private static final VoxelShape[] SHAPES = new VoxelShape[] {
        // Worked - the only simple one here
        box(1, 0, 1, 15, 15, 15),
        // Large vessel
        Shapes.join(
            Shapes.or(
                box(2, 0, 2, 14, 14, 14),
                box(1, 2, 1, 15, 12, 15)
            ),
            box(4, 2, 4, 12, 14, 12),
            BooleanOp.ONLY_FIRST
        ),
        // Small vessel
        Shapes.join(
            Shapes.or(
                box(4, 0, 4, 12, 10, 12),
                box(3, 2, 4, 13, 8, 12),
                box(4, 2, 3, 12, 8, 13),
                box(5, 9, 5, 11, 11, 11)
            ),
            Shapes.or(
                box(5, 0, 5, 10, 10, 10),
                box(6, 9, 6, 10, 11, 10)
            ),
            BooleanOp.ONLY_FIRST
        ),
        // Bucket
        Shapes.join(
            Shapes.or(
                box(6, 0, 6, 10, 1, 10),
                box(5, 1, 5, 11, 7, 11),
                box(5, 4, 4, 11, 10, 12),
                box(4, 4, 5, 12, 10, 11)
            ),
            Shapes.or(
                box(6, 3, 6, 10, 10, 10),
                box(5, 7, 5, 11, 10, 11)
            ),
            BooleanOp.ONLY_FIRST
        ),
        // Flower Pot
        Shapes.join(
            box(5, 0, 5, 11, 6, 11),
            box(6, 2, 6, 10, 6, 10),
            BooleanOp.ONLY_FIRST
        )
    };

    private final VoxelShape shape;

    public PotteryBlock(Variant variant)
    {
        super(Properties.of(BREAKABLE_CLAY).strength(0.8f - 0.1f * variant.ordinal()).sound(SoundType.GRAVEL));

        this.shape = SHAPES[variant.ordinal()];
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return shape;
    }

    public enum Variant
    {
        WORKED,
        LARGE_VESSEL,
        SMALL_VESSEL,
        BUCKET,
        FLOWER_POT
    }
}