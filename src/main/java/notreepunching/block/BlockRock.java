package notreepunching.block;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import notreepunching.item.ModItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public class BlockRock extends BlockBase {

    public static final IProperty<EnumMineralType> TYPE = PropertyEnum.create("type",EnumMineralType.class);

    BlockRock(String name) {
        super(name, Material.ROCK);

        setHardness(0.15F);
        setSoundType(SoundType.STONE);
        setDefaultState(this.blockState.getBaseState().withProperty(TYPE,EnumMineralType.STONE));
    }

    @Override
    public void register(){
        ModBlocks.addBlockToRegistry(this, new ItemBlock(this), name, null);
    }
    @Override
    public void addModelToRegistry(){
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
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(0.375D, 0D, 0.375D, 0.615D, 0.0625D, 0.625D);
    }

    @Override
    public boolean canSpawnInBlock() {
        return true;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        drops.clear();
        drops.add(new ItemStack(ModItems.rockStone,1,this.getMetaFromState(state)));

    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(ModItems.rockStone,1,this.getMetaFromState(state));
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
            }
        }
    }

    // ************* Block State Methods ************** //

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumMineralType mineral = EnumMineralType.byMetadata(meta);
        return this.getDefaultState().withProperty(TYPE,mineral);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        EnumMineralType type = state.getValue(TYPE);
        return type.getMetadata();
    }

    @Nonnull
    private String getStoneName(ItemStack stack){
        switch(stack.getMetadata()){
            case 0:
                return "stone"; // Vanilla Stone
            case 1:
                return "andesite"; // Vanilla Stone Variants
            case 2:
                return "diorite";
            case 3:
                return "granite";
            case 4:
                return "marble"; // Quark Stone Types
            case 5:
                return "limestone";
            case 6:
                return "slate"; // Rustic Slate
            default:
                return "unknown stone name - this is a bug";
        }
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, TYPE);
    }

    public enum EnumMineralType implements IStringSerializable {
        STONE(0, "stone"),
        ANDESITE(1, "andesite"),
        DIORITE(2, "diorite"),
        GRANITE(3, "granite"),
        MARBLE(4, "marble"),
        LIMESTONE(5, "limestone"),
        SLATE(6, "slate");

        public int getMetadata()
        {
            return this.meta;
        }

        @Override
        public String toString()
        {
            return this.name;
        }

        public static EnumMineralType byMetadata(int meta) {
            if (meta < 0 || meta >= META_LOOKUP.length) {
                meta = 0;
            }
            return META_LOOKUP[meta];
        }
        public static EnumMineralType byState(IBlockState state) {
            if(state.getBlock() == ModBlocks.graniteCobble){
                return GRANITE;
            }else if(state.getBlock() == ModBlocks.dioriteCobble){
                return DIORITE;
            }else if(state.getBlock() == ModBlocks.andesiteCobble){
                return ANDESITE;
            }else if(state.getBlock() == ModBlocks.marbleCobble){
                return MARBLE;
            }else if(state.getBlock() == ModBlocks.limestoneCobble){
                return LIMESTONE;
            }else if(state.getBlock() == ModBlocks.slateCobble){
                return SLATE;
            }
            return STONE;
        }

        public String getName()
        {
            return this.name;
        }

        private final int meta;
        private final String name;
        private static final EnumMineralType[] META_LOOKUP = new EnumMineralType[values().length];

        EnumMineralType(int i_meta, String i_name) {
            this.meta = i_meta;
            this.name = i_name;
        }

        static {
            for (EnumMineralType colour : values()) {
                META_LOOKUP[colour.getMetadata()] = colour;
            }
        }
    }
}
