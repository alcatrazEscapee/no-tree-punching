package notreepunching.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockWoodPile extends BlockBase {

    private static final PropertyBool AXIS = PropertyBool.create("axis");

    public BlockWoodPile(String name){
        super(name, Material.WOOD);

        setHardness(1.2F);
        this.setDefaultState(this.getDefaultState().withProperty(AXIS, false));
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        if(placer.getHorizontalFacing().getAxis() == EnumFacing.Axis.Z) {return this.getDefaultState().withProperty(AXIS, true); }
        return this.getDefaultState();
    }

    @Nonnull
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(AXIS, meta == 0);
    }
    public int getMetaFromState(IBlockState state) {
        return state.getValue(AXIS) ? 0 : 1;
    }
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AXIS);
    }

    public enum EnumAxis implements IStringSerializable {
        X("x", 0),
        Z("z", 1);

        private final String name;
        private final int num;

        EnumAxis(String name, int num) {
            this.name = name;
            this.num = num;
        }

        public String toString() {
            return this.name;
        }
        public String getName() {
            return this.name;
        }
        public int asInt(){
            return this.num;
        }
        public static EnumAxis fromFacing(EnumFacing facing){
            return facing.getAxis() == EnumFacing.Axis.X ? EnumAxis.X : EnumAxis.Z;
        }
        public static EnumAxis fromMeta(int meta){
            return meta == 0 ? EnumAxis.X : EnumAxis.Z;
        }
    }
}
