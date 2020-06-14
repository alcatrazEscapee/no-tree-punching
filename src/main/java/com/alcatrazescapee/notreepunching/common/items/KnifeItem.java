/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Copyright (c) 2019. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.items;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

public class KnifeItem extends SwordItem
{
    public KnifeItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, Properties builder)
    {
        super(tier, attackDamageIn, attackSpeedIn, builder);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        // Get Opposite Hand to Knife
        Hand handOther = handIn == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND;

        // Get Items in each hand:
        ItemStack knifeStack = playerIn.getHeldItem(handIn);
        ItemStack cutStack = playerIn.getHeldItem(handOther);

        // todo: proper recipe handling
        /*
        KnifeRecipe recipe = ModRecipes.KNIFE.get(cutStack);
        if (recipe != null)
        {
            if (!worldIn.isRemote)
            {
                for (ItemStack stack : recipe.getOutput())
                {
                    ItemHandlerHelper.giveItemToPlayer(playerIn, stack);
                }
                if (!playerIn.isCreative())
                {
                    CoreHelpers.damageItem(playerIn, handIn, knifeStack, 3);
                    playerIn.setHeldItem(handOther, recipe.consumeInput(cutStack));
                }
            }

            // Play a cool sound effect:
            worldIn.playSound(playerIn, playerIn.getPosition(), SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.PLAYERS, 1.0F, 1.0F);
            return new ActionResult<>(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
        }
        */

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    /**
     * Gross copy pasta from {@link net.minecraft.item.ShearsItem}
     * Forge has deprecated {@link IShearable} without actually providing an alternative... sigh.
     */
    @Override
    @SuppressWarnings("ALL")
    public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity entity, Hand hand)
    {
        if (entity.world.isRemote) return false;
        if (entity instanceof net.minecraftforge.common.IShearable)
        {
            net.minecraftforge.common.IShearable target = (net.minecraftforge.common.IShearable) entity;
            BlockPos pos = new BlockPos(entity.getPosX(), entity.getPosY(), entity.getPosZ());
            if (target.isShearable(stack, entity.world, pos))
            {
                java.util.List<ItemStack> drops = target.onSheared(stack, entity.world, pos,
                    net.minecraft.enchantment.EnchantmentHelper.getEnchantmentLevel(net.minecraft.enchantment.Enchantments.FORTUNE, stack));
                java.util.Random rand = new java.util.Random();
                drops.forEach(d -> {
                    net.minecraft.entity.item.ItemEntity ent = entity.entityDropItem(d, 1.0F);
                    ent.setMotion(ent.getMotion().add((double) ((rand.nextFloat() - rand.nextFloat()) * 0.1F), (double) (rand.nextFloat() * 0.05F), (double) ((rand.nextFloat() - rand.nextFloat()) * 0.1F)));
                });
                stack.damageItem(1, entity, e -> e.sendBreakAnimation(hand));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
    {
        return enchantment.type == EnchantmentType.BREAKABLE || enchantment.type == EnchantmentType.WEAPON;
    }
}
