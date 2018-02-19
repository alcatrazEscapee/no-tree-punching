package notreepunching.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import notreepunching.item.ModItems;
import notreepunching.tile.TileEntityFirePit;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockFirePit extends BlockWithTileEntity<TileEntityFirePit> {

    private final boolean isLit;

    public BlockFirePit(String name, Material material,boolean isLit){
        super(name,material);

        this.isLit = isLit;
        if (isLit)
        {
            this.setLightLevel(1.0F);
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(!world.isRemote){
            TileEntityFirePit te = getTileEntity(world,pos);

            // check if the player is trying to light the firepit
            ItemStack stack = player.getHeldItem(hand);
            if(stack.getItem() == Items.STICK){
                // create a torch
                stack.shrink(1);
                EntityItem resultDrop = new EntityItem(world, player.posX, player.posY, player.posZ, new ItemStack(Item.getItemFromBlock(Blocks.TORCH)));
                world.spawnEntity(resultDrop);
            }

            if(stack.getItem() == Item.getItemFromBlock(Blocks.PLANKS)){
                if(te.addFuel()){
                    stack.shrink(1);
                }
            }

            if(stack.getItem() == ModItems.fireStarter){
                stack.damageItem(1,player);
                te.lightFirepit();
            }

            //player.openGui(SimpleBeginnings.instance, GuiHandler.FIREPIT, world, pos.getX(), pos.getY(), pos.getZ());

        }
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        // drop the contents of the firepit
        TileEntityFirePit te = (TileEntityFirePit) world.getTileEntity(pos);
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        Random rand = world instanceof World ? ((World)world).rand : RANDOM;

        drops.clear();
        drops.add(new ItemStack(Items.STICK, rand.nextInt(2)+1, 0));

    }

    // -------------------------- Tile Entity ------------------------------ //

    @Override
    public Class<TileEntityFirePit> getTileEntityClass() {
        return TileEntityFirePit.class;
    }

    @Nullable
    @Override
    public TileEntityFirePit createTileEntity(World world, IBlockState state) {
        return new TileEntityFirePit();
    }

    // -------------------------- Block Model ------------------------------ //

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(0.375D, 0D, 0.375D, 0.615D, 0.0625D, 0.625D);
    }
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

}
