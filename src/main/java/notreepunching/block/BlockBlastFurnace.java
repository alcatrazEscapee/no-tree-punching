package notreepunching.block;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import notreepunching.block.tile.TileEntityBlastFurnace;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public class BlockBlastFurnace extends BlockWithTE<TileEntityBlastFurnace> {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final IProperty<Boolean> BURNING = PropertyBool.create("burning");

    BlockBlastFurnace(String name){
        super(name, Material.ROCK);

        setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(BURNING,false));
    }

    // ********************* Tile Entity Methods ****************** //

    @Override
    public TileEntityBlastFurnace createTileEntity(World world, IBlockState state) {
        return new TileEntityBlastFurnace();
    }

    @Override
    public Class<TileEntityBlastFurnace> getTileEntityClass() {
        return TileEntityBlastFurnace.class;
    }

    // ******************* IBlockState Methods ******************** //

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta%4)).withProperty(BURNING, meta>=4);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex() + (state.getValue(BURNING) ? 4 : 0);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, BURNING);
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(BURNING, false);
    }
}
