package notreepunching.block;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import notreepunching.NoTreePunching;
import notreepunching.block.tile.TileEntityWoodPile;
import notreepunching.client.ModGuiHandler;
import notreepunching.item.ModItems;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.Random;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("deprecation")
public class BlockWoodPile extends BlockWithTileEntity<TileEntityWoodPile> {

    private static final PropertyBool AXIS = PropertyBool.create("axis");
    public static final PropertyBool ONFIRE = PropertyBool.create("onfire");

    BlockWoodPile(String name){
        super(name, Material.WOOD);

        setHardness(2.0F);
        setSoundType(SoundType.WOOD);
        setTickRandomly(true);
        setHarvestLevel("axe",0);
        this.setDefaultState(this.getDefaultState().withProperty(AXIS, false).withProperty(ONFIRE, false));
    }
    @Override
    public void register(){
        ModBlocks.addBlockToRegistry(this, new ItemBlock(this), name);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            TileEntityWoodPile te = (TileEntityWoodPile) world.getTileEntity(pos);
            if(te == null){ return true; }

            // Special Interactions
            ItemStack stack = player.getHeldItem(hand);
            if((stack.getItem() == Items.FLINT_AND_STEEL || stack.getItem() == ModItems.firestarter) && ! state.getValue(ONFIRE) && side == EnumFacing.UP){
                // Light the Pile
                if(world.getBlockState(pos.up()).getBlock().isReplaceable(world, pos)){
                    te.burning = true;
                    world.setBlockState(pos, state.withProperty(ONFIRE, true));
                    te.tryLightNearby(world, pos);
                    world.setBlockState(pos.up(), Blocks.FIRE.getDefaultState());
                    world.playSound(null,pos,SoundEvents.ITEM_FLINTANDSTEEL_USE,SoundCategory.PLAYERS,1.0F,1.0F);
                }
            }

            if (!player.isSneaking() && !state.getValue(ONFIRE)) {
                player.openGui(NoTreePunching.instance, ModGuiHandler.WOODPILE, world, pos.getX(), pos.getY(), pos.getZ());
            }
        }
        return true;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        drops.clear();
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(Blocks.LOG);
    }

    private boolean isValidCoverBlock(World world, BlockPos pos){
        IBlockState state = world.getBlockState(pos);
        if(state.getBlock() == ModBlocks.woodPile || state.getBlock() == ModBlocks.charcoalPile){ return true; }
        if(state.getMaterial().getCanBurn()){ return false; }
        return state.isNormalCube();
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
        if(!worldIn.isRemote && te != null){
            TileEntityWoodPile tile = (TileEntityWoodPile) te;
            IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
            if (itemHandler != null) {
                for (int i = 0; i < itemHandler.getSlots(); i++) {
                    ItemStack stack2 = itemHandler.getStackInSlot(i);
                    if (!stack.isEmpty()) {
                        EntityItem item = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack2);
                        worldIn.spawnEntity(item);
                    }
                }
            }
        }
        super.harvestBlock(worldIn, player, pos, state, te, stack);
    }

    @Override
    public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return 60;
    }

    @Override
    public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return 30;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand){
        if(stateIn.getValue(ONFIRE)) {
            NoTreePunching.proxy.generateParticle(worldIn, pos, 3);
            if(rand.nextDouble() < 0.4D){
                worldIn.playSound((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 0.5F, 0.6F, false);
            }
        }
    }

    @Override
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random){
        if(!worldIn.isRemote && state.getValue(ONFIRE)){
            for(EnumFacing side : EnumFacing.values()){
                if(!isValidCoverBlock(worldIn, pos.offset(side))){
                    worldIn.setBlockState(pos, Blocks.FIRE.getDefaultState());
                }
                IBlockState state2 = worldIn.getBlockState(pos.offset(side));
                if(state2.getBlock() == ModBlocks.woodPile){
                    if(!state2.getValue(ONFIRE)){
                        worldIn.setBlockState(pos.offset(side),state2.withProperty(ONFIRE,true));
                    }
                }
            }
        }
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        if(placer.getHorizontalFacing().getAxis() == EnumFacing.Axis.Z) {return this.getDefaultState().withProperty(AXIS, true); }
        return this.getDefaultState();
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(AXIS, meta == 0).withProperty(ONFIRE, meta >= 2);
    }
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(AXIS) ? 0 : 1) + (state.getValue(ONFIRE) ? 2 : 0);
    }
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AXIS, ONFIRE);
    }

    @Nullable
    @Override
    public TileEntityWoodPile createTileEntity(World world, IBlockState state){
        return new TileEntityWoodPile();
    }

    public Class<TileEntityWoodPile> getTileEntityClass(){
        return TileEntityWoodPile.class;
    }
}
