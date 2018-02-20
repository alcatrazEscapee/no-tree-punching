package notreepunching.item;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import notreepunching.NoTreePunching;
import notreepunching.recipe.CuttingRecipe;
import notreepunching.recipe.ModRecipes;

import java.util.Set;

public class ItemKnife extends ItemTool {

    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.TALLGRASS,Blocks.DOUBLE_PLANT,Blocks.MELON_BLOCK,Blocks.PUMPKIN,Blocks.WOOL);
    public String name;

    public ItemKnife(Item.ToolMaterial material, String name){
        super(material.getAttackDamage(),-3.0F,material,EFFECTIVE_ON);

        setUnlocalizedName(name);
        setRegistryName(name);
        this.name = name;
        setCreativeTab(NoTreePunching.NTP_Tab);
    }

    public boolean shouldBreakBlock(Block block){
        return EFFECTIVE_ON.contains(block);
    }

    public boolean shouldDamageItem(Block block){
        return (block instanceof BlockTallGrass || block instanceof BlockDoublePlant);
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        // Get Opposite Hand to Knife
        EnumHand handOther = EnumHand.MAIN_HAND;
        if(handIn == EnumHand.MAIN_HAND){
            handOther = EnumHand.OFF_HAND;
        }

        // Get Items in each hand:
        ItemStack knifeStack = playerIn.getHeldItem(handIn);
        ItemStack cutStack = playerIn.getHeldItem(handOther);

        if (ModRecipes.isCuttingRecipe(cutStack)){
            CuttingRecipe recipe = ModRecipes.getCuttingRecipe(cutStack);

            if(cutStack.getCount()>=1){
                if(!worldIn.isRemote) {
                    for (int i = 0; i < recipe.drops.length; i++) {
                        EntityItem resultDrop = new EntityItem(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ, recipe.drops[i]);
                        worldIn.spawnEntity(resultDrop);
                    }

                    // Damage the knife used
                    knifeStack.damageItem(3, playerIn);

                    // Remove one item from the hand
                    cutStack.shrink(1);
                }
                // Play a cool sound effect:
                BlockPos pos = new BlockPos(playerIn.posX,playerIn.posY,playerIn.posZ);
                worldIn.playSound(playerIn, pos, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.PLAYERS, 1.0F, 1.0F);
                return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
            }
        }
        return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }
}
