/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
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
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;
import com.alcatrazescapee.notreepunching.common.items.ModItems;

public class PotteryBlock extends Block
{
    // todo: make these actually represent the block shape instead of not
    private static final VoxelShape[] SHAPES = new VoxelShape[] {
        makeCuboidShape(1, 0, 1, 15, 15, 15),
        makeCuboidShape(2, 0, 2, 14, 14, 14),
        makeCuboidShape(4, 0, 4, 8, 6, 8),
        makeCuboidShape(5, 0, 5, 7, 6, 7),
        makeCuboidShape(5, 0, 5, 7, 3, 7)
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
