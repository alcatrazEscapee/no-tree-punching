/*
 *
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 *
 */

package notreepunching.item;

import com.google.common.collect.Sets;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import notreepunching.NoTreePunching;
import notreepunching.client.ModTabs;
import notreepunching.recipe.knife.KnifeRecipe;
import notreepunching.recipe.knife.KnifeRecipeHandler;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Set;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ItemKnife extends ItemTool {

    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.TALLGRASS,Blocks.DOUBLE_PLANT,Blocks.MELON_BLOCK,Blocks.PUMPKIN,Blocks.WOOL);
    public String name;

    ItemKnife(Item.ToolMaterial material, String name){
        super(material.getAttackDamage(),-2.0F,material,EFFECTIVE_ON);

        this.name = name;
        register();
    }

    public void register(){
        ModItems.addItemToRegistry(this,name, ModTabs.TOOLS_TAB);
        NoTreePunching.proxy.addModelToRegistry(new ItemStack(this), this.getRegistryName(), "inventory");
    }

    public boolean shouldBreakBlock(Block block){
        return EFFECTIVE_ON.contains(block);
    }

    public boolean shouldDamageItem(Block block){
        return (block instanceof BlockTallGrass || block instanceof BlockDoublePlant);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        // Get Opposite Hand to Knife
        EnumHand handOther = EnumHand.MAIN_HAND;
        if(handIn == EnumHand.MAIN_HAND){
            handOther = EnumHand.OFF_HAND;
        }

        // Get Items in each hand:
        ItemStack knifeStack = playerIn.getHeldItem(handIn);
        ItemStack cutStack = playerIn.getHeldItem(handOther);

        KnifeRecipe recipe = KnifeRecipeHandler.getRecipe(cutStack);
        if (recipe != null){

            if(cutStack.getCount()>=1){
                if(!worldIn.isRemote) {
                    for (int i = 0; i < recipe.getOutput().size(); i++) {
                        EntityItem resultDrop = new EntityItem(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ, recipe.getOutput().get(i));
                        worldIn.spawnEntity(resultDrop);
                    }

                    // Damage the knife used
                    knifeStack.damageItem(3, playerIn);

                    // Remove input item from the hand
                    cutStack.shrink(recipe.getInput().getCount());
                }
                // Play a cool sound effect:
                BlockPos pos = new BlockPos(playerIn.posX,playerIn.posY,playerIn.posZ);
                worldIn.playSound(playerIn, pos, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.PLAYERS, 1.0F, 1.0F);
                return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
            }
        }
        return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }
}
