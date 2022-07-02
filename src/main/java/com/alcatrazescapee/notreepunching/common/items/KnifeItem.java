/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.items;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import com.alcatrazescapee.notreepunching.Config;
import com.alcatrazescapee.notreepunching.util.Helpers;

public class KnifeItem extends SwordItem
{
    public KnifeItem(Tier tier, int attackDamageIn, float attackSpeedIn, Properties builder)
    {
        super(tier, attackDamageIn, attackSpeedIn, builder.setNoRepair());
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack)
    {
        return Helpers.hurtAndBreak(stack.copy(), 1);
    }

    @Override
    public boolean hasContainerItem(ItemStack stack)
    {
        return true;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
    {
        return enchantment.category == EnchantmentCategory.BREAKABLE || enchantment.category == EnchantmentCategory.WEAPON;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving)
    {
        if (!worldIn.isClientSide && (state.getDestroySpeed(worldIn, pos) != 0.0F || Config.INSTANCE.doInstantBreakBlocksDamageKnives.get()))
        {
            stack.hurtAndBreak(1, entityLiving, entityIn -> entityIn.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }
        return true;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity entity, InteractionHand hand)
    {
        return Items.SHEARS.interactLivingEntity(stack, playerIn, entity, hand);
    }
}