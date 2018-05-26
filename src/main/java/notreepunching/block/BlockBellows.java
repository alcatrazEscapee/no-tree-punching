package notreepunching.block;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import notreepunching.block.tile.IHasTileEntity;
import notreepunching.block.tile.TileEntityBellows;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

@MethodsReturnNonnullByDefault
public class BlockBellows extends BlockWithTileEntity<TileEntityBellows> implements IHasTileEntity<TileEntityBellows> {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public BlockBellows(String name){
        super(name, Material.WOOD);

        setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public Class<TileEntityBellows> getTileEntityClass() {
        return TileEntityBellows.class;
    }

    @Nullable
    @Override
    public TileEntityBellows createTileEntity(World world, IBlockState state) {
        return new TileEntityBellows(state.getValue(FACING));
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        updateRedstone(world, pos);
    }
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighborBlock, BlockPos neighborPos) {
        updateRedstone(world, pos);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        updateRedstone(world, pos);
    }

    private void updateRedstone(World world, BlockPos pos) {
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

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @SuppressWarnings("deprecation")
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }
}
