/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.common.items;

import java.util.List;

import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.TieredItem;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import com.alcatrazescapee.core.util.CoreHelpers;
import com.alcatrazescapee.notreepunching.ModConfig;
import com.alcatrazescapee.notreepunching.common.ModItemGroups;
import com.alcatrazescapee.notreepunching.common.ModTags;

public class FireStarterItem extends TieredItem
{
    public FireStarterItem()
    {
        super(ItemTier.WOOD, new Properties().group(ModItemGroups.ITEMS).maxDamage(10).setNoRepair());
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        playerIn.setActiveHand(handIn);
        return new ActionResult<>(ActionResultType.PASS, playerIn.getHeldItem(handIn));
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving)
    {
        if (entityLiving instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity) entityLiving;
            RayTraceResult result = rayTrace(worldIn, player, RayTraceContext.FluidMode.NONE);

            if (result.getType() == RayTraceResult.Type.BLOCK)
            {
                // If looking at a block
                BlockPos pos = ((BlockRayTraceResult) result).getPos();
                if (!worldIn.isRemote)
                {
                    CoreHelpers.damageItem(player, player.getActiveHand(), stack, 1);
                    List<ItemEntity> entities = worldIn.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(pos.up(), pos.add(1, 2, 1)));

                    // Require 1 log, 3 kindling. Tinder is optional but makes it more efficient
                    int tinder = 3, logs = 1, kindling = 3;

                    for (ItemEntity drop : entities)
                    {
                        ItemStack dropStack = drop.getItem();
                        if (ModTags.Items.FIRE_STARTER_LOGS.contains(dropStack.getItem()))
                        {
                            logs += dropStack.getCount();
                        }
                        else if (ModTags.Items.FIRE_STARTER_KINDLING.contains(dropStack.getItem()))
                        {
                            kindling += dropStack.getCount();
                        }
                        else if (ModTags.Items.FIRE_STARTER_TINDER.contains(dropStack.getItem()))
                        {
                            tinder += dropStack.getCount();
                        }
                    }
                    if (tinder <= 0 && logs <= 0 && kindling <= 0)
                    {
                        // Commence Fire pit making
                        worldIn.setBlockState(pos.up(), Blocks.CAMPFIRE.getDefaultState());
                    }
                    else
                    {
                        // No fire pit to make, try light a fire
                        if (random.nextFloat() < ModConfig.BALANCE.fireStarterFireStartChance)
                        {
                            worldIn.setBlockState(pos.up(), Blocks.FIRE.getDefaultState());
                        }
                    }
                }
            }
        }
        return stack;
    }

    @Override
    public UseAction getUseAction(ItemStack stack)
    {
        return UseAction.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack)
    {
        return 30;
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count)
    {
        // todo: client only particles
        /*
        if (random.nextFloat() < 0.3)
        {
            World world = player.getEntityWorld();
            EntityPlayer player1 = (EntityPlayer) player;
            RayTraceResult result = rayTrace(world, player1, false);
            // noinspection ConstantConditions
            if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK)
            {
                Vec3d v = result.hitVec;
                ParticleManager.generateFireStarterSmoke(world, v);
            }
        }
        */
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
    {
        return enchantment.type == EnchantmentType.BREAKABLE;
    }
}
