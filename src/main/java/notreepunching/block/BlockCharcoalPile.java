package notreepunching.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import notreepunching.util.ItemUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class BlockCharcoalPile extends BlockBase {

    private static final PropertyInteger LAYERS = PropertyInteger.create("type",1,8);
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

    public BlockCharcoalPile(String name){
        super(name, Material.GROUND);
        setSoundType(SoundType.GROUND);

        this.setDefaultState(this.blockState.getBaseState().withProperty(LAYERS, 1));

    }

    @Override
    public void addModelToRegistry(){
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            ItemStack stack = player.getHeldItem(hand);
            if(stack.getItem() == Items.COAL && stack.getMetadata() == 1){
                if(state.getValue(LAYERS) < 8){
                    world.setBlockState(pos,state.withProperty(LAYERS,state.getValue(LAYERS)+1));
                    player.setHeldItem(hand, ItemUtil.consumeItem(stack));
                    world.playSound(null,pos, SoundEvents.BLOCK_GRAVEL_PLACE, SoundCategory.BLOCKS,1.0F,0.5F);
                }
                return true;
            }
        }
        return false;
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
            if(!stateUnder.getBlock().isNormalCube(stateUnder,worldIn,pos.down())){
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }
        }
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
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, @Nonnull IBlockAccess worldIn, @Nonnull BlockPos pos) {
        int i = blockState.getValue(LAYERS) - 1;
        float f = 0.125F;
        AxisAlignedBB axisalignedbb = blockState.getBoundingBox(worldIn, pos);
        return new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.maxX, (double)((float)i * 0.125F), axisalignedbb.maxZ);
    }
    public boolean isOpaqueCube(IBlockState state) {
        return state.getValue(LAYERS) == 8;
    }
    public boolean isFullCube(IBlockState state) {
        return state.getValue(LAYERS) == 8;
    }
    @Nonnull
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(LAYERS, (meta & 7) + 1);
    }
    public int getMetaFromState(IBlockState state) {
        return state.getValue(LAYERS) - 1;
    }
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{LAYERS});
    }
}
