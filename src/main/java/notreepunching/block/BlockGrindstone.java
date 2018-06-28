/*
 *
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 *
 */

package notreepunching.block;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import notreepunching.NoTreePunching;
import notreepunching.block.tile.TileEntityGrindstone;
import notreepunching.client.ModGuiHandler;
import notreepunching.item.ModItems;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public class BlockGrindstone extends BlockWithTEInventory<TileEntityGrindstone>{

    BlockGrindstone(String name){
        super(name, Material.ROCK);

        setHardness(2.5F);
        setHarvestLevel("pickaxe",0);
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
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
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
            ItemStack stack = player.getHeldItem(hand);
            TileEntityGrindstone te = (TileEntityGrindstone) world.getTileEntity(pos);
            if(te == null) return true;

            if(stack.getItem() == ModItems.grindWheel){
                if(!te.getHasWheel()){
                    te.insertWheel(stack);
                    player.setHeldItem(hand, ItemStack.EMPTY);
                    return true;
                }
            }

            // Only start grinding if there is a wheel, you clicked on it, and its not already grinding
            if(te.getHasWheel() && side == EnumFacing.UP && te.getRotation() == 0){
                if(!te.startGrinding()){
                    if(!player.isSneaking()){
                        player.openGui(NoTreePunching.instance, ModGuiHandler.GRINDSTONE, world, pos.getX(), pos.getY(), pos.getZ());
                    }
                }
            }else{
                if(!player.isSneaking()){
                    player.openGui(NoTreePunching.instance, ModGuiHandler.GRINDSTONE, world, pos.getX(), pos.getY(), pos.getZ());
                }
            }
        }
        return true;
    }
}
