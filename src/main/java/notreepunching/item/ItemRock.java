package notreepunching.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import notreepunching.NoTreePunching;
import notreepunching.block.BlockRock;
import notreepunching.block.ModBlocks;

import javax.annotation.Nonnull;

import static notreepunching.block.BlockRock.TYPE;

public class ItemRock extends ItemBase {

    public ItemRock(String name){
        super(name);

        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public void register(){
        ModItems.addItemToRegistry(this,name,true);
        for(int i=0;i<7;i++) {
            if(!NoTreePunching.replaceQuarkStones && (i == 4 || i == 5)) { continue; }
            if(!NoTreePunching.replaceRusticStone && (i == 6)) { continue; }
            NoTreePunching.proxy.addModelToRegistry(new ItemStack(this,1,i),
                    new ResourceLocation(NoTreePunching.MODID,name + "_" +  this.getStoneName(i)),"inventory");
        }
    }

    @Override
    @Nonnull
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName() + "_" + getStoneName(stack);
    }
    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (getCreativeTab() != tab) return;
        //super.getSubItems(tab, items);
        for (int i = 0; i < 7; ++i) {
            if(!NoTreePunching.replaceQuarkStones && (i == 4 || i == 5)) { continue; }
            if(!NoTreePunching.replaceRusticStone && (i == 6)) { continue; }
            items.add(new ItemStack(this, 1, i));
        }
    }

    public String getStoneName(ItemStack stack){
        return getStoneName(stack.getMetadata());
    }
    private String getStoneName(int meta){
        switch(meta){
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
                return "";
        }
    }

    @Nonnull
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(player != null && !(player instanceof FakePlayer)){
            if(!worldIn.isRemote){
                // Check for block below
                IBlockState stateUnder = worldIn.getBlockState(pos);
                if(!stateUnder.getBlock().isNormalCube(stateUnder,worldIn,pos)){
                    return EnumActionResult.FAIL;
                }
                // Check for air block above
                IBlockState stateAt = worldIn.getBlockState(pos.up());
                if(!stateAt.getBlock().equals(Blocks.AIR)){
                    return EnumActionResult.FAIL;
                }
                // Place the rock in the world
                int meta = player.getHeldItem(hand).getMetadata();
                ItemStack stack = player.getHeldItem(hand);
                stack.shrink(1);
                if(stack.getCount() == 0){
                    stack = ItemStack.EMPTY;
                }
                player.setHeldItem(hand,stack);
                // Player is null here otherwise sound is only played to other players see: https://mcforge.readthedocs.io/en/latest/effects/sounds/
                worldIn.playSound(null, pos, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                worldIn.setBlockState(pos.up(), ModBlocks.looseRock.getDefaultState().withProperty(TYPE, BlockRock.EnumMineralType.byMetadata(meta)));
                return EnumActionResult.SUCCESS;
            }
        }

        return EnumActionResult.PASS;
    }
}
