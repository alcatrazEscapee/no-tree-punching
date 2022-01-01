/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.items;

import java.util.List;
import java.util.Random;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.IForgeShearable;
import net.minecraftforge.common.ToolType;

import com.alcatrazescapee.notreepunching.Config;
import com.alcatrazescapee.notreepunching.util.Helpers;

import net.minecraft.world.item.Item.Properties;

public class KnifeItem extends SwordItem
{
    public KnifeItem(Tier tier, int attackDamageIn, float attackSpeedIn, Properties builder)
    {
        super(tier, attackDamageIn, attackSpeedIn, builder.setNoRepair().addToolType(ToolType.get("sword"), tier.getLevel()));
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
        if (!worldIn.isClientSide && (state.getDestroySpeed(worldIn, pos) != 0.0F || Config.SERVER.doInstantBreakBlocksDamageKnives.get()))
        {
            stack.hurtAndBreak(1, entityLiving, entityIn -> entityIn.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }
        return true;
    }

    /**
     * Gross copy pasta from {@link net.minecraft.item.ShearsItem}
     */
    @Override
    @SuppressWarnings("ALL")
    public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity entity, InteractionHand hand)
    {
        if (entity.level.isClientSide) return net.minecraft.world.InteractionResult.PASS;
        if (entity instanceof IForgeShearable)
        {
            IForgeShearable target = (IForgeShearable) entity;
            BlockPos pos = new BlockPos(entity.getX(), entity.getY(), entity.getZ());
            if (target.isShearable(stack, entity.level, pos))
            {
                List<ItemStack> drops = target.onSheared(playerIn, stack, entity.level, pos, EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, stack));
                Random rand = new Random();
                drops.forEach(d -> {
                    ItemEntity ent = entity.spawnAtLocation(d, 1.0F);
                    ent.setDeltaMovement(ent.getDeltaMovement().add((double) ((rand.nextFloat() - rand.nextFloat()) * 0.1F), (double) (rand.nextFloat() * 0.05F), (double) ((rand.nextFloat() - rand.nextFloat()) * 0.1F)));
                });
                stack.hurtAndBreak(1, entity, e -> e.broadcastBreakEvent(hand));
            }
            return net.minecraft.world.InteractionResult.SUCCESS;
        }
        return net.minecraft.world.InteractionResult.PASS;
    }
}