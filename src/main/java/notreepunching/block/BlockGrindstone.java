package notreepunching.block;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import notreepunching.block.tile.IHasTESR;
import notreepunching.block.tile.TileEntityGrindstone;
import notreepunching.client.tesr.TESRGrindstone;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlockGrindstone extends BlockWithTileEntity<TileEntityGrindstone> implements IHasTESR<TileEntityGrindstone, TESRGrindstone>{

    public BlockGrindstone(String name){
        super(name, Material.ROCK);
    }

    @Override
    public Class<TileEntityGrindstone> getTileEntityClass() {
        return TileEntityGrindstone.class;
    }

    @Nullable
    @Override
    public TileEntityGrindstone createTileEntity(World world, IBlockState state) {
        return new TileEntityGrindstone();
    }

    @Override
    public TESRGrindstone getTESR(){ return new TESRGrindstone(); }

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
