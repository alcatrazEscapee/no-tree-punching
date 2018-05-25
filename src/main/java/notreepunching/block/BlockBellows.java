package notreepunching.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import notreepunching.block.tile.IHasTileEntity;
import notreepunching.block.tile.TileEntityBellows;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class BlockBellows extends BlockWithTileEntity<TileEntityBellows> implements IHasTileEntity<TileEntityBellows> {

    public BlockBellows(String name){
        super(name, Material.WOOD);
    }

    @Override
    public Class<TileEntityBellows> getTileEntityClass() {
        return TileEntityBellows.class;
    }

    @Nullable
    @Override
    public TileEntityBellows createTileEntity(World world, IBlockState state) {
        return new TileEntityBellows();
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        updateRedstone(world, pos, state);
    }
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighborBlock, BlockPos neighborPos) {
        updateRedstone(world, pos, state);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        updateRedstone(world, pos, state);
    }

    private void updateRedstone(World world, BlockPos pos, IBlockState state) {
        //if (world.isRemote) return;

        TileEntityBellows te = (TileEntityBellows) world.getTileEntity(pos);
        if (te != null){
            te.updatePower(world.isBlockPowered(pos));
        }
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
    @Override
    @Nonnull
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
}
