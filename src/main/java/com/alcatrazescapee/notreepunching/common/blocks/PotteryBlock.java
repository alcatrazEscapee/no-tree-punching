/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

public class PotteryBlock extends Block
{
    public static final Material BREAKABLE_CLAY = new Material(MaterialColor.CLAY, false, false, true, true, false, false, PushReaction.DESTROY);

    private static final VoxelShape[] SHAPES = new VoxelShape[] {
        // Worked - the only simple one here
        box(1, 0, 1, 15, 15, 15),
        // Large vessel
        VoxelShapes.join(
            VoxelShapes.or(
                box(2, 0, 2, 14, 14, 14),
                box(1, 2, 1, 15, 12, 15)
            ),
            box(4, 2, 4, 12, 14, 12),
            IBooleanFunction.ONLY_FIRST
        ),
        // Small vessel
        VoxelShapes.join(
            VoxelShapes.or(
                box(4, 0, 4, 12, 10, 12),
                box(3, 2, 4, 13, 8, 12),
                box(4, 2, 3, 12, 8, 13),
                box(5, 9, 5, 11, 11, 11)
            ),
            VoxelShapes.or(
                box(5, 0, 5, 10, 10, 10),
                box(6, 9, 6, 10, 11, 10)
            ),
            IBooleanFunction.ONLY_FIRST
        ),
        // Bucket
        VoxelShapes.join(
            VoxelShapes.or(
                box(6, 0, 6, 10, 1, 10),
                box(5, 1, 5, 11, 7, 11),
                box(5, 4, 4, 11, 10, 12),
                box(4, 4, 5, 12, 10, 11)
            ),
            VoxelShapes.or(
                box(6, 3, 6, 10, 10, 10),
                box(5, 7, 5, 11, 10, 11)
            ),
            IBooleanFunction.ONLY_FIRST
        ),
        // Flower Pot
        VoxelShapes.join(
            box(5, 0, 5, 11, 6, 11),
            box(6, 2, 6, 10, 6, 10),
            IBooleanFunction.ONLY_FIRST
        )
    };

    private final VoxelShape shape;

    public PotteryBlock(Variant variant)
    {
        super(Properties.of(BREAKABLE_CLAY).strength(0.8f - 0.1f * variant.ordinal()).harvestLevel(0).harvestTool(ToolType.SHOVEL).sound(SoundType.GRAVEL));

        this.shape = SHAPES[variant.ordinal()];
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return shape;
    }

    public enum Variant
    {
        WORKED,
        LARGE_VESSEL,
        SMALL_VESSEL,
        BUCKET,
        FLOWER_POT;
    }
}