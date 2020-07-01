/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.blocks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

import com.alcatrazescapee.notreepunching.common.items.ModItems;

public class PotteryBlock extends Block
{
    private static final VoxelShape[] SHAPES = new VoxelShape[] {
        // Worked - the only simple one here
        makeCuboidShape(1, 0, 1, 15, 15, 15),
        // Large vessel
        VoxelShapes.combineAndSimplify(
            VoxelShapes.or(
                makeCuboidShape(2, 0, 2, 14, 14, 14),
                makeCuboidShape(1, 2, 1, 15, 12, 15)
            ),
            makeCuboidShape(4, 2, 4, 12, 14, 12),
            IBooleanFunction.ONLY_FIRST
        ),
        // Small vessel
        VoxelShapes.combineAndSimplify(
            VoxelShapes.or(
                makeCuboidShape(4, 0, 4, 12, 10, 12),
                makeCuboidShape(3, 2, 4, 13, 8, 12),
                makeCuboidShape(4, 2, 3, 12, 8, 13),
                makeCuboidShape(5, 9, 5, 11, 11, 11)
            ),
            VoxelShapes.or(
                makeCuboidShape(5, 0, 5, 10, 10, 10),
                makeCuboidShape(6, 9, 6, 10, 11, 10)
            ),
            IBooleanFunction.ONLY_FIRST
        ),
        // Bucket
        VoxelShapes.combineAndSimplify(
            VoxelShapes.or(
                makeCuboidShape(6, 0, 6, 10, 1, 10),
                makeCuboidShape(5, 1, 5, 11, 7, 11),
                makeCuboidShape(5, 4, 4, 11, 10, 12),
                makeCuboidShape(4, 4, 5, 12, 10, 11)
            ),
            VoxelShapes.or(
                makeCuboidShape(6, 3, 6, 10, 10, 10),
                makeCuboidShape(5, 7, 5, 11, 10, 11)
            ),
            IBooleanFunction.ONLY_FIRST
        ),
        VoxelShapes.combineAndSimplify(
            makeCuboidShape(5, 0, 5, 11, 6, 11),
            makeCuboidShape(6, 2, 6, 10, 6, 10),
            IBooleanFunction.ONLY_FIRST
        )
    };

    private final Variant variant;

    public PotteryBlock(Variant variant)
    {
        super(Properties.create(Material.CLAY).hardnessAndResistance(0.8f - 0.1f * variant.ordinal()).harvestLevel(0).harvestTool(ToolType.SHOVEL).sound(SoundType.GROUND));

        this.variant = variant;
    }

    @Nonnull
    public Variant getVariant()
    {
        return variant;
    }

    @Nonnull
    public ItemStack getFiredType()
    {
        switch (variant)
        {
            case LARGE_VESSEL:
                return new ItemStack(ModBlocks.CERAMIC_LARGE_VESSEL.get());
            case BUCKET:
                return new ItemStack(ModItems.CERAMIC_BUCKET.get());
            case SMALL_VESSEL:
                return new ItemStack(ModItems.CERAMIC_SMALL_VESSEL.get());
            case FLOWER_POT:
                return new ItemStack(Items.FLOWER_POT);
            default:
                return ItemStack.EMPTY;
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return SHAPES[variant.ordinal()];
    }

    public enum Variant
    {
        WORKED,
        LARGE_VESSEL,
        SMALL_VESSEL,
        BUCKET,
        FLOWER_POT;

        private static final Variant[] VALUES = Variant.values();

        @Nullable
        public Variant next()
        {
            if (this == FLOWER_POT)
            {
                return null;
            }
            return VALUES[this.ordinal() + 1];
        }
    }
}
