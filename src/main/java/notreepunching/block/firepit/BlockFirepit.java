package notreepunching.block.firepit;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import notreepunching.NoTreePunching;
import notreepunching.block.BlockWithTileEntity;
import notreepunching.block.ModBlocks;
import notreepunching.client.NTPGuiHandler;
import notreepunching.item.ItemFirestarter;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockFirepit extends BlockWithTileEntity<TileEntityFirepit> {

    public static final IProperty<Boolean> BURNING = PropertyBool.create("burning");

    public BlockFirepit(Material material, String name) {
        super(material, name);


        setTickRandomly(true);
        setDefaultState(this.blockState.getBaseState().withProperty(BURNING,true));
    }

    @Override
    public void register(){
        ModBlocks.addBlockToRegistry(this, new ItemBlock(this), name, true);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {

            ItemStack heldItem = player.getHeldItem(hand);
            TileEntityFirepit tile = getTileEntity(world, pos);
            //IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);

            if(heldItem.getItem() instanceof ItemFirestarter || heldItem.getItem()==Items.FLINT_AND_STEEL){
                world.setBlockState(pos,this.getDefaultState().withProperty(BURNING,true));
                heldItem.damageItem(1,player); // remove for easier testing
            }else if(heldItem.getItem() == Item.getItemFromBlock(Blocks.SAND)){
                world.setBlockState(pos,this.getDefaultState().withProperty(BURNING,false));
                heldItem.shrink(1);
                if(heldItem.getCount() == 0){
                    heldItem = ItemStack.EMPTY;
                }
                player.setHeldItem(hand,heldItem);
            }
            else{
                if (!player.isSneaking()) {
                    player.openGui(NoTreePunching.instance, NTPGuiHandler.FIREPIT, world, pos.getX(), pos.getY(), pos.getZ());
                }
            }
        }
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntityFirepit tile = getTileEntity(world, pos);
        IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
        for(int i=0;i<3;i++) {
            ItemStack stack = itemHandler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
                world.spawnEntity(item);
            }
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        drops.clear();
        drops.add(new ItemStack(Items.STICK,2,0));

    }

    // ******************* TE / State Methods ******************** //

    @Override
    public Class<TileEntityFirepit> getTileEntityClass() {
        return TileEntityFirepit.class;
    }

    @Nullable
    @Override
    public TileEntityFirepit createTileEntity(World world, IBlockState state) {
        return new TileEntityFirepit();
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(BURNING,meta==1);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(BURNING) ? 1 : 0;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, BURNING);
    }

    // ******************** Block Appearance ************************ //

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
    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(BURNING) ? 15 : 0;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (!worldIn.isRemote) {
            // Breaks block if the block under it breaks.
            IBlockState stateUnder = worldIn.getBlockState(pos.down());
            if(!stateUnder.getBlock().isNormalCube(stateUnder,worldIn,pos.down())){
                dropBlockAsItem(worldIn,pos,state,0);
                worldIn.setBlockToAir(pos);
            }
        }
    }

    @Override
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random){
        if(!worldIn.isRemote){
            if(worldIn.canBlockSeeSky(pos) && worldIn.isRaining() && worldIn.getTopSolidOrLiquidBlock(pos).getY()<pos.getY()+2){
                worldIn.setBlockState(pos,state.withProperty(BURNING,false));
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand){
        if(stateIn.getValue(BURNING)){
            NoTreePunching.proxy.generateParticle(worldIn, pos, EnumParticleTypes.FLAME);
            worldIn.playSound((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            if(worldIn.canBlockSeeSky(pos) && worldIn.isRaining() && worldIn.getTopSolidOrLiquidBlock(pos).getY()<pos.getY()+2){
                if(rand.nextDouble() < 0.4D){
                    worldIn.playSound((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
                    NoTreePunching.proxy.generateParticle(worldIn, pos, EnumParticleTypes.SMOKE_LARGE);
                }
            }
        }
    }

}
