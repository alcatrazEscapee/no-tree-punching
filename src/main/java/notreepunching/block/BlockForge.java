package notreepunching.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import notreepunching.NoTreePunching;
import notreepunching.block.tile.TileEntityForge;
import notreepunching.client.ModGuiHandler;
import notreepunching.item.ModItems;
import notreepunching.util.ItemUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public class BlockForge extends BlockWithTileEntity<TileEntityForge> {

    public static final PropertyInteger LAYERS = PropertyInteger.create("type",1,8);
    public static final PropertyBool BURNING = PropertyBool.create("burning");
    private static final AxisAlignedBB[] PILE_AABB = new AxisAlignedBB[]{
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.375D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.625D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)};

    BlockForge(String name){
        super(name, Material.GROUND);

        setSoundType(SoundType.GROUND);
        setHardness(1.0F);
        setHarvestLevel("shovel",0);
        setTickRandomly(true);
        this.setDefaultState(this.blockState.getBaseState().withProperty(LAYERS, 1).withProperty(BURNING, true));
    }

    @Override
    public void register(){
        ModBlocks.addBlockToRegistry(this, new ItemBlock(this), name);
    }

    @Override
    public void addModelToRegistry(){
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {

            ItemStack stack = player.getHeldItem(hand);

            // Special Interactions
            if(stack.getItem() == Items.COAL && stack.getMetadata() == 1){
                if(state.getValue(LAYERS) < 8){
                    world.setBlockState(pos,state.withProperty(LAYERS,state.getValue(LAYERS)+1).withProperty(BURNING,state.getValue(BURNING)));
                    if(!player.isCreative()) {
                        player.setHeldItem(hand, ItemUtil.consumeItem(stack));
                    }
                    world.playSound(null,pos, SoundEvents.BLOCK_GRAVEL_PLACE, SoundCategory.BLOCKS,1.0F,0.5F);
                    return true;
                }
            }
            if(stack.getItem() == Items.FLINT_AND_STEEL || stack.getItem() == ModItems.firestarter){
                world.setBlockState(pos,state.withProperty(BURNING, !state.getValue(BURNING)));
                stack.damageItem(1, player);
                world.playSound(null,pos, SoundEvents.ITEM_FLINTANDSTEEL_USE,SoundCategory.PLAYERS,1.0F,1.0F);

                TileEntity te = world.getTileEntity(pos);
                if(te instanceof TileEntityForge) {
                    ((TileEntityForge) te).closed = updateSideBlocks(world, pos);
                }

                return true;
            }
            if (!player.isSneaking()) {
                player.openGui(NoTreePunching.instance, ModGuiHandler.FORGE, world, pos.getX(), pos.getY(), pos.getZ());
            }
        }
        return true;
    }

    @Nonnull
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.COAL;
    }
    @Override
    public int damageDropped(IBlockState state) {
        return 1;
    }
    public int quantityDropped(IBlockState state, int fortune, @Nonnull Random random) {
        return state.getValue(LAYERS);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (!worldIn.isRemote) {
            // Breaks rock if the block under it breaks.
            IBlockState stateUnder = worldIn.getBlockState(pos.down());
            if(!stateUnder.isNormalCube()){
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
                return;
            }
            TileEntity te = worldIn.getTileEntity(pos);
            if(te instanceof TileEntityForge) {
                ((TileEntityForge) te).closed = updateSideBlocks(worldIn, pos);
            }
        }
    }

    @Override
    @Nonnull
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(Items.COAL, 1, 1);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntityForge tile = getTileEntity(world, pos);
        IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
        if(itemHandler!=null) {
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                ItemStack stack = itemHandler.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
                    world.spawnEntity(item);
                }
            }
        }
        super.breakBlock(world, pos, state);
    }
    public static boolean updateSideBlocks(World world, BlockPos pos) {
        if (!world.isRemote){
            IBlockState state = world.getBlockState(pos.north());
            if (!state.getBlock().isNormalCube(state, world, pos) || !state.getBlock().getMaterial(state).equals(Material.ROCK)) {
                return false;
            }
            state = world.getBlockState(pos.east());
            if (!state.getBlock().isNormalCube(state, world, pos) || !state.getBlock().getMaterial(state).equals(Material.ROCK)) {
                return false;
            }
            state = world.getBlockState(pos.west());
            if (!state.getBlock().isNormalCube(state, world, pos) || !state.getBlock().getMaterial(state).equals(Material.ROCK)) {
                return false;
            }
            state = world.getBlockState(pos.south());
            if (!state.getBlock().isNormalCube(state, world, pos) || !state.getBlock().getMaterial(state).equals(Material.ROCK)) {
                return false;
            }
        }
        return true;
    }


    // **************** TILE ENTITY METHODS ************************************* //

    @Override
    public Class<TileEntityForge> getTileEntityClass() {
        return TileEntityForge.class;
    }

    @Nullable
    @Override
    public TileEntityForge createTileEntity(World world, IBlockState state) {
        return new TileEntityForge();
    }

    // *************** APPEARANCE AND BLOCK STATE METHODS ********************** //

    @Nonnull
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return PILE_AABB[state.getValue(LAYERS)];
    }
    public boolean isTopSolid(IBlockState state) {
        return state.getValue(LAYERS) == 8;
    }
    @Nonnull
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return face == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        int i = blockState.getValue(LAYERS) - 1;
        AxisAlignedBB axisalignedbb = blockState.getBoundingBox(worldIn, pos);
        return new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.maxX, (double)((float)i * 0.125F), axisalignedbb.maxZ);
    }
    public boolean isOpaqueCube(IBlockState state) {
        return state.getValue(LAYERS) == 8  && !state.getValue(BURNING);
    }
    public boolean isFullCube(IBlockState state) {
        return state.getValue(LAYERS) == 8 && !state.getValue(BURNING);
    }
    @Nonnull
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(LAYERS, meta % 8 + 1).withProperty(BURNING,meta>7);
    }
    public int getMetaFromState(IBlockState state) {
        return state.getValue(LAYERS) + (state.getValue(BURNING) ? 7 : -1);
    }
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, LAYERS, BURNING);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand){
        if(stateIn.getValue(BURNING)){
            worldIn.playSound((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0F, 1.0F, false);

            if(rand.nextFloat() <= 0.3){
                NoTreePunching.proxy.generateParticle(worldIn, pos, 1);
            }
        }
    }
    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(BURNING) ? 15 : 0;
    }


}
