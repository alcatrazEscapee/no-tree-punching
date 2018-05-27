package notreepunching.block;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import notreepunching.NoTreePunching;
import notreepunching.block.tile.IHasTESR;
import notreepunching.block.tile.TileEntityGrindstone;
import notreepunching.client.ModGuiHandler;
import notreepunching.client.tesr.TESRGrindstone;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public class BlockGrindstone extends BlockWithTileEntity<TileEntityGrindstone> implements IHasTESR<TileEntityGrindstone, TESRGrindstone>{

    BlockGrindstone(String name){
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

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(0.0d,0.0d,0.0d, 1.0d, 0.5d, 1.0d);
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return this.getBoundingBox(blockState, worldIn, pos);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            if(side != EnumFacing.UP){
                if(!player.isSneaking()){
                    player.openGui(NoTreePunching.instance, ModGuiHandler.GRINDSTONE, world, pos.getX(), pos.getY(), pos.getZ());
                }
            }else{
                TileEntityGrindstone te = (TileEntityGrindstone) world.getTileEntity(pos);
                if(te != null){
                    te.startGrinding();
                }
            }
            // If the TE grind slot is empty, and the player is not shifting open it
            // If the TE grind slot is not empty, and player is shifting, open it
            // If the TE grind slot is not empty, and the player is not shifting, turn the crank
        }
        return true;
    }
}
