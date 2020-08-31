/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.items;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IForgeShearable;
import net.minecraftforge.common.ToolType;

import com.alcatrazescapee.notreepunching.Config;
import com.alcatrazescapee.notreepunching.util.Helpers;

public class KnifeItem extends SwordItem
{
    public KnifeItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, Properties builder)
    {
        super(tier, attackDamageIn, attackSpeedIn, builder.setNoRepair().addToolType(ToolType.get("sword"), tier.getHarvestLevel()));
    }

    @Override
    public boolean hasContainerItem(ItemStack stack)
    {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack)
    {
        return Helpers.damageItem(stack.copy(), 1);
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving)
    {
        if (!worldIn.isRemote && (state.getBlockHardness(worldIn, pos) != 0.0F || Config.SERVER.doInstantBreakBlocksDamageKnives.get()))
        {
            stack.damageItem(1, entityLiving, entityIn -> entityIn.sendBreakAnimation(EquipmentSlotType.MAINHAND));
        }
        return true;
    }

    /**
     * Gross copy pasta from {@link net.minecraft.item.ShearsItem}
     */
    @Override
    @SuppressWarnings("ALL")
    public ActionResultType itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity entity, Hand hand)
    {
        if (entity.world.isRemote) return net.minecraft.util.ActionResultType.PASS;
        if (entity instanceof IForgeShearable)
        {
            IForgeShearable target = (IForgeShearable) entity;
            BlockPos pos = new BlockPos(entity.getX(), entity.getY(), entity.getZ());
            if (target.isShearable(stack, entity.world, pos))
            {
                List<ItemStack> drops = target.onSheared(playerIn, stack, entity.world, pos, EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack));
                Random rand = new Random();
                drops.forEach(d -> {
                    ItemEntity ent = entity.entityDropItem(d, 1.0F);
                    ent.setMotion(ent.getMotion().add((double) ((rand.nextFloat() - rand.nextFloat()) * 0.1F), (double) (rand.nextFloat() * 0.05F), (double) ((rand.nextFloat() - rand.nextFloat()) * 0.1F)));
                });
                stack.damageItem(1, entity, e -> e.sendBreakAnimation(hand));
            }
            return net.minecraft.util.ActionResultType.SUCCESS;
        }
        return net.minecraft.util.ActionResultType.PASS;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
    {
        return enchantment.type == EnchantmentType.BREAKABLE || enchantment.type == EnchantmentType.WEAPON;
    }
}
