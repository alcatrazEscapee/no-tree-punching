package notreepunching.block;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import notreepunching.block.tile.TileEntityForge;
import notreepunching.block.tile.TileEntityTuyere;
import notreepunching.item.ModItems;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("deprecation")
public class BlockTuyere extends BlockWithTE<TileEntityTuyere> {

    private static final IProperty<BlockRock.EnumMineralType> TYPE = PropertyEnum.create("type",BlockRock.EnumMineralType.class);

    BlockTuyere(String name){
        super(name, Material.ROCK);

        setHardness(3.0F);
        setSoundType(SoundType.STONE);
        setDefaultState(this.blockState.getBaseState().withProperty(TYPE, BlockRock.EnumMineralType.STONE));
    }

    @Override
    public void register() {
        ModBlocks.addBlockToRegistry(this, new ItemBlock(this), name, null);
    }

    @Override
    public void addModelToRegistry(){
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        drops.clear();
        drops.add(new ItemStack(ModItems.rockStone,3,this.getMetaFromState(state)));
        drops.add(new ItemStack(ModItems.tuyere));

    }
    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(ModItems.tuyere);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        BlockRock.EnumMineralType mineral = BlockRock.EnumMineralType.byMetadata(meta);
        return this.getDefaultState().withProperty(TYPE,mineral);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        BlockRock.EnumMineralType type = state.getValue(TYPE);
        return type.getMetadata();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, TYPE);
    }

    @Nullable
    @Override
    public TileEntityTuyere createTileEntity(World world, IBlockState state) {
        return new TileEntityTuyere();
    }

    @Override
    public Class<TileEntityTuyere> getTileEntityClass() {
        return TileEntityTuyere.class;
    }
}
