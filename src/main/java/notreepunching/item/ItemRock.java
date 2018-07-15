/*
 *
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 *
 */

package notreepunching.item;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import notreepunching.NoTreePunching;
import notreepunching.block.BlockRock;
import notreepunching.block.ModBlocks;
import notreepunching.client.ModTabs;
import notreepunching.util.ItemUtil;

import javax.annotation.ParametersAreNonnullByDefault;

import static notreepunching.NoTreePunching.MODID;
import static notreepunching.block.BlockRock.TYPE;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ItemRock extends ItemBase {

    ItemRock(String name){
        super(name);

        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public void register(){
        ModItems.addItemToRegistry(this,name, ModTabs.ITEMS_TAB);
        for (int i = 0; i < 7; i++) {
            if(!NoTreePunching.hasQuark && (i == 4 || i == 5)) { continue; }
            if(!NoTreePunching.hasRustic && (i == 6)) { continue; }
            NoTreePunching.proxy.addModelToRegistry(new ItemStack(this,1,i),
                    new ResourceLocation(MODID, name + "_" + this.getStoneName(i)), "inventory");
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "_" + getStoneName(stack);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (getCreativeTab() != tab) return;
        //super.getSubItems(tab, items);
        for (int i = 0; i < 7; ++i) {
            if(!NoTreePunching.hasQuark && (i == 4 || i == 5)) { continue; }
            if(!NoTreePunching.hasRustic && (i == 6)) { continue; }
            items.add(new ItemStack(this, 1, i));
        }
    }

    private String getStoneName(ItemStack stack){
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

    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        // Try and place
        System.out.println("Called onItemUse");
        IBlockState stateUnder = worldIn.getBlockState(pos);
        IBlockState stateAt = worldIn.getBlockState(pos.up());

        if (stateUnder.isNormalCube() && stateAt.getBlock().isReplaceable(worldIn, pos.up())) {
            if (!worldIn.isRemote) {
                int meta = player.getHeldItem(hand).getMetadata();
                ItemStack stack = player.getHeldItem(hand);

                player.setHeldItem(hand, ItemUtil.consumeItem(stack));

                worldIn.playSound(null, pos, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                worldIn.setBlockState(pos.up(), ModBlocks.looseRock.getDefaultState().withProperty(TYPE, BlockRock.EnumMineralType.byMetadata(meta)));
            }
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }
}
