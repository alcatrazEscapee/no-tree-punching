package notreepunching.block;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import notreepunching.NoTreePunching;
import notreepunching.block.tile.TileEntityBlastFurnace;
import notreepunching.client.ModGuiHandler;
import notreepunching.item.ItemFirestarter;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public class BlockBlastFurnace extends BlockWithTE<TileEntityBlastFurnace> {

    public static final IProperty<Boolean> BURNING = PropertyBool.create("burning");

    BlockBlastFurnace(String name){
        super(name, Material.ROCK);

        setHardness(4.0F);
        setHarvestLevel("pickaxe",1);
        setDefaultState(this.blockState.getBaseState().withProperty(BURNING,false));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote){

            ItemStack heldItem = player.getHeldItem(hand);

            // Special Interactions
            if(!state.getValue(BURNING)) {
                if (heldItem.getItem() instanceof ItemFirestarter || heldItem.getItem() == Items.FLINT_AND_STEEL) {
                    // Light blast furnace
                    TileEntityBlastFurnace te = (TileEntityBlastFurnace) worldIn.getTileEntity(pos);
                    if(te != null){
                        te.light();
                        worldIn.setBlockState(pos, this.getDefaultState().withProperty(BURNING, true));
                    }
                    heldItem.damageItem(1, player);
                    return true;
                }
            }
            // Open the GUI
            if (!player.isSneaking()) {
                player.openGui(NoTreePunching.instance, ModGuiHandler.BLAST_FURNACE, worldIn, pos.getX(), pos.getY(), pos.getZ());
            }
        }
        return true;
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand){
        if(stateIn.getValue(BURNING)){
            worldIn.playSound((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0F, 0.6F, false);
        }
    }


    // ********************* Tile Entity Methods ****************** //

    @Override
    public TileEntityBlastFurnace createTileEntity(World world, IBlockState state) {
        return new TileEntityBlastFurnace();
    }

    @Override
    public Class<TileEntityBlastFurnace> getTileEntityClass() {
        return TileEntityBlastFurnace.class;
    }

    // ******************* IBlockState Methods ******************** //

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(BURNING, meta == 1);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(BURNING) ? 1 : 0;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, BURNING);
    }

}
