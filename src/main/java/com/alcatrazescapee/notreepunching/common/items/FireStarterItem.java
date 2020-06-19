/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Copyright (c) 2019. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.items;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import com.alcatrazescapee.notreepunching.Config;
import com.alcatrazescapee.notreepunching.common.ModItemGroup;
import com.alcatrazescapee.notreepunching.common.ModTags;
import com.alcatrazescapee.notreepunching.util.Helpers;

public class FireStarterItem extends TieredItem
{
    public FireStarterItem()
    {
        super(ItemTier.WOOD, new Properties().group(ModItemGroup.ITEMS).maxDamage(10).setNoRepair());
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
                    Helpers.damageItem(player, player.getActiveHand(), stack, 1);

                    BlockState stateAt = worldIn.getBlockState(pos);
                    if (FlintAndSteelItem.isUnlitCampfire(stateAt))
                    {
                        // Light campfire
                        worldIn.playSound(player, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, random.nextFloat() * 0.4F + 0.8F);
                        worldIn.setBlockState(pos, stateAt.with(BlockStateProperties.LIT, true), 11);
                    }
                    else
                    {
                        List<ItemEntity> entities = worldIn.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(pos.up(), pos.add(1, 2, 1)));
                        List<ItemEntity> logEntities = new ArrayList<>(), kindlingEntities = new ArrayList<>();

                        // Require 1 log, 3 kindling
                        int logs = 0, kindling = 0;

                        for (ItemEntity drop : entities)
                        {
                            ItemStack dropStack = drop.getItem();
                            if (ModTags.Items.FIRE_STARTER_LOGS.contains(dropStack.getItem()))
                            {
                                logs += dropStack.getCount();
                                logEntities.add(drop);
                            }
                            else if (ModTags.Items.FIRE_STARTER_KINDLING.contains(dropStack.getItem()))
                            {
                                kindling += dropStack.getCount();
                                kindlingEntities.add(drop);
                            }
                        }
                        if (logs >= 1 && kindling >= 3)
                        {
                            // Commence Fire pit making
                            removeItems(logEntities, logs);
                            removeItems(kindlingEntities, kindling);
                            worldIn.setBlockState(pos.up(), Blocks.CAMPFIRE.getDefaultState().with(CampfireBlock.LIT, true));
                        }
                        else
                        {
                            // No fire pit to make, try light a fire
                            if (random.nextFloat() < Config.SERVER.fireStarterFireStartChance.get())
                            {
                                worldIn.setBlockState(pos.up(), Blocks.FIRE.getDefaultState());
                            }
                        }
                    }
                }
            }
        }
        return stack;
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count)
    {
        if (player.world.isRemote && player instanceof PlayerEntity)
        {
            RayTraceResult result = rayTrace(player.world, (PlayerEntity) player, RayTraceContext.FluidMode.NONE);
            if (result instanceof BlockRayTraceResult && random.nextInt(5) == 0)
            {
                BlockRayTraceResult blockResult = (BlockRayTraceResult) result;
                player.world.addParticle(ParticleTypes.SMOKE, blockResult.getHitVec().x, blockResult.getHitVec().y, blockResult.getHitVec().z, 0.0F, 0.1F, 0.0F);
            }
        }
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

    private void removeItems(List<ItemEntity> itemEntities, int removeAmount)
    {
        for (ItemEntity logEntity : itemEntities)
        {
            ItemStack logStack = logEntity.getItem();
            int shrink = Math.min(logStack.getCount(), removeAmount);
            removeAmount -= shrink;
            logStack.shrink(shrink);
            if (logStack.getCount() == 0)
            {
                logEntity.remove();
            }
        }
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
    {
        return enchantment.type == EnchantmentType.BREAKABLE;
    }
}
