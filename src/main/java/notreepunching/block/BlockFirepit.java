package notreepunching.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import notreepunching.NoTreePunching;
import notreepunching.item.ItemFirestarter;
import notreepunching.item.ModItems;
import notreepunching.tile.TileEntityFirepit;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockFirepit extends BlockWithTileEntity<TileEntityFirepit>{

    public static final IProperty<Boolean> BURNING = PropertyBool.create("burning");

    public BlockFirepit(Material material, String name) {
        super(material, name);

        setDefaultState(this.blockState.getBaseState().withProperty(BURNING,true));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {

            /*TileEntityFirepit tile = getTileEntity(world, pos);
            IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
            ItemStack heldItem = player.getHeldItem(hand);

            if (!player.isSneaking()) {
                if (heldItem.isEmpty()) {
                    player.setHeldItem(hand, itemHandler.extractItem(0, 64, false));
                } else {
                    player.setHeldItem(hand, itemHandler.insertItem(0, heldItem, false));
                }
                tile.markDirty();
            } else {
                ItemStack stack = itemHandler.getStackInSlot(0);
                if (!stack.isEmpty()) {
                    String localized = NoTreePunching.proxy.localize(stack.getUnlocalizedName() + ".name");
                    player.sendMessage(new TextComponentString(stack.getCount() + "x " + localized));
                } else {
                    player.sendMessage(new TextComponentString("Empty"));
                }
            }*/
            ItemStack heldItem = player.getHeldItem(hand);
            TileEntityFirepit tile = getTileEntity(world, pos);
            IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);

            if(heldItem.getItem() instanceof ItemFirestarter){
                //tile.light();
                world.setBlockState(pos,this.getDefaultState().withProperty(BURNING,!state.getValue(BURNING)));
                heldItem.damageItem(1,player); // removed for easier testing
            }
            else{
                if (!player.isSneaking()) {
                    player.setHeldItem(hand, itemHandler.insertItem(0, heldItem, false));
                }else{
                    ItemStack stack = itemHandler.getStackInSlot(0);
                    if (!stack.isEmpty()) {
                        String localized = NoTreePunching.proxy.localize(stack.getUnlocalizedName() + ".name");
                        player.sendMessage(new TextComponentString(stack.getCount() + "x " + localized));
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        /*TileEntityFirepit tile = getTileEntity(world, pos);
        IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
        ItemStack stack = itemHandler.getStackInSlot(0);
        if (!stack.isEmpty()) {
            EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
            world.spawnEntity(item);
        }*/
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

    /*@Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        TileEntity te1 = worldIn.getTileEntity(pos);
        if (te1 instanceof TileEntityFirepit) {
            TileEntityFirepit te2 = (TileEntityFirepit) te1;
            return getDefaultState().withProperty(BURNING, te2.isLit());
        }
        return state;
    }*/

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
        //TileEntityFirepit tile = (TileEntityFirepit) world.getTileEntity(pos);
        //System.out.println("Looking at: "+pos.toString()+" and we have "+world.getTileEntity(pos).getClass().toString());
        //System.out.println("This gives: "+tile.litTicks+" and then "+tile.isLit());
        //return tile.isLit() ? 15 : 0;

        //IBlockState blockState = getActualState(getDefaultState(), world, pos);
        return state.getValue(BURNING) ? 15 : 0;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (!worldIn.isRemote) {
            // Breaks block if the block under it breaks.
            IBlockState stateUnder = worldIn.getBlockState(pos.down());
            if(!stateUnder.getBlock().isNormalCube(stateUnder,worldIn,pos.down())){
                this.breakBlock(worldIn, pos, state);
                worldIn.setBlockToAir(pos);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand){
        if(stateIn.getValue(BURNING)){
            NoTreePunching.proxy.generateParticle(worldIn, pos, EnumParticleTypes.FLAME);
            worldIn.playSound((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0F, 1.0F, false);

            if(worldIn.canBlockSeeSky(pos) && worldIn.isRaining()){
                if(rand.nextDouble() < 0.4D){
                    worldIn.playSound((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
                    NoTreePunching.proxy.generateParticle(worldIn, pos, EnumParticleTypes.SMOKE_LARGE);

                    if(rand.nextDouble() < 0.05D){
                        worldIn.playSound((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
                        worldIn.setBlockState(pos,this.getDefaultState().withProperty(BURNING,false));
                    }
                }
            }
        }
    }

}
