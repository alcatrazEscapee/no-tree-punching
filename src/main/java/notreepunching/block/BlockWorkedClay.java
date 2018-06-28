/*
 *
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 *
 */

package notreepunching.block;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import notreepunching.NoTreePunching;
import notreepunching.client.ModTabs;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public class BlockWorkedClay extends BlockBase {

    public static final IProperty<EnumClayType> TYPE = PropertyEnum.create("type",EnumClayType.class, EnumClayType.values());
    private static final AxisAlignedBB[] AABB = new AxisAlignedBB[]{
            new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.9375D, 0.9375D),
            new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.875D, 0.875D),
            new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.625D, 0.75D),
            new AxisAlignedBB(0.3125D, 0.0D, 0.3125D, 0.6875D, 0.625D, 0.6875D),
            new AxisAlignedBB(0.3125D, 0.0D, 0.3125D, 0.6875D, 0.375D, 0.6875D)
    };

    BlockWorkedClay(String name){
        super(name, Material.CLAY);

        setSoundType(SoundType.GROUND);
        setHarvestLevel("shovel",0);
        setHardness(0.4F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, EnumClayType.UNWORKED));
    }

    @Override
    public void register(){
        ModBlocks.addBlockToRegistry(this, new ItemMultiTexture(this,this, BlockWorkedClay::getClayType), name, ModTabs.ITEMS_TAB);
    }

    @Override
    public void addModelToRegistry() {
        for(EnumClayType c : EnumClayType.values()) {
            NoTreePunching.proxy.addModelToRegistry(new ItemStack(this,1,c.getMeta()), this.getRegistryName(), "type="+c.getName());
        }
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
        return AABB[blockState.getValue(TYPE).getMeta()];
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB[state.getValue(TYPE).getMeta()];
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        drops.clear();
        if(state.getValue(TYPE) == EnumClayType.UNWORKED){
            drops.add(new ItemStack(Items.CLAY_BALL,3));
        }else{
            drops.add(new ItemStack(this, 1, state.getValue(TYPE).getMeta()));
        }
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        if(state.getValue(TYPE) == EnumClayType.UNWORKED){
            return new ItemStack(Blocks.CLAY);
        }else{
            return new ItemStack(this, 1, state.getValue(TYPE).getMeta());
        }
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        if(tab != ModTabs.ITEMS_TAB) return;
        for(EnumClayType c : EnumClayType.values()){
            items.add(new ItemStack(this, 1, c.getMeta()));
        }
    }

    private static String getClayType(ItemStack stack) {
        return EnumClayType.byMeta(stack.getMetadata()).toString();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(TYPE, EnumClayType.byMeta(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(TYPE).getMeta();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, TYPE);
    }

    public enum EnumClayType implements IStringSerializable {
        UNWORKED(0,"unworked"),
        LARGE_VESSEL(1,"large_vessel"),
        SMALL_VESSEL(2,"small_vessel"),
        BUCKET(3,"bucket"),
        SMALL_POT(4,"small_pot");

        private final int meta;
        private final String name;

        EnumClayType(int i_meta, String i_name) {
            this.meta = i_meta;
            this.name = i_name;
        }

        @Override
        public String toString() {
            return this.name;
        }

        @Nonnull
        @Override
        public String getName() { return this.name; }

        public int getMeta(){ return this.meta; }

        public static EnumClayType byMeta(int meta){
            return EnumClayType.values()[meta];
        }

        public EnumClayType getNext(){
            int next = this.meta == EnumClayType.values().length-1 ? 0 : this.meta + 1;
            return EnumClayType.values()[next];
        }
    }
}
