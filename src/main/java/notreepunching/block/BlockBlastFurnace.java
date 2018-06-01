package notreepunching.block;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import notreepunching.NoTreePunching;
import notreepunching.block.tile.TileEntityBlastFurnace;
import notreepunching.client.ModGuiHandler;
import notreepunching.item.ItemFirestarter;
import notreepunching.util.ItemUtil;

import javax.annotation.ParametersAreNonnullByDefault;

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
                    worldIn.setBlockState(pos, this.getDefaultState().withProperty(BURNING, true));
                    heldItem.damageItem(1, player);
                    return true;

                }
            }
            // Open the Firepit GUI
            if (!player.isSneaking()) {
                player.openGui(NoTreePunching.instance, ModGuiHandler.BLAST_FURNACE, worldIn, pos.getX(), pos.getY(), pos.getZ());
            }
        }
        return true;
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
