/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.common.items;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import com.alcatrazescapee.alcatrazcore.item.tool.ItemToolCore;
import com.alcatrazescapee.alcatrazcore.util.CoreHelpers;
import com.alcatrazescapee.notreepunching.common.recipe.KnifeRecipe;
import com.alcatrazescapee.notreepunching.common.recipe.ModRecipes;

@ParametersAreNonnullByDefault
public class ItemKnife extends ItemToolCore
{
    // todo: grass drops
    private static final List<ItemStack> grassDrops = new ArrayList<>();

    static
    {
        ItemKnife.addGrassDrop(new ItemStack(ModItems.GRASS_FIBER));
    }

    public static void addGrassDrop(ItemStack stack)
    {
        grassDrops.add(stack);
    }

    public ItemKnife(ToolMaterial material)
    {
        super(material, material.getAttackDamage() + 1.0f, -2.4f);
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        // Get Opposite Hand to Knife
        EnumHand handOther = handIn == EnumHand.MAIN_HAND ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;

        // Get Items in each hand:
        ItemStack knifeStack = playerIn.getHeldItem(handIn);
        ItemStack cutStack = playerIn.getHeldItem(handOther);

        KnifeRecipe recipe = ModRecipes.KNIFE.get(cutStack);
        if (recipe != null)
        {
            if (!worldIn.isRemote)
            {
                for (ItemStack stack : recipe.getOutput())
                {
                    stack = stack.copy();
                    if (!playerIn.addItemStackToInventory(stack))
                    {
                        CoreHelpers.dropItemInWorldExact(worldIn, playerIn.getPosition(), stack);
                    }
                }
                if (!playerIn.isCreative())
                {
                    knifeStack.damageItem(3, playerIn);
                    playerIn.setHeldItem(handOther, recipe.consumeInput(cutStack));
                }
            }

            // Play a cool sound effect:
            worldIn.playSound(playerIn, playerIn.getPosition(), SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.PLAYERS, 1.0F, 1.0F);
            return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
