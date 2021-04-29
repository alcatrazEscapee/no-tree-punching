/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
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
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.TieredItem;
import net.minecraft.item.UseAction;
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
        super(ItemTier.WOOD, new Properties().tab(ModItemGroup.ITEMS).durability(10).setNoRepair());
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        playerIn.startUsingItem(handIn);
        return new ActionResult<>(ActionResultType.PASS, playerIn.getItemInHand(handIn));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving)
    {
        if (entityLiving instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity) entityLiving;
            BlockRayTraceResult result = getPlayerPOVHitResult(worldIn, player, RayTraceContext.FluidMode.NONE);

            if (result.getType() == RayTraceResult.Type.BLOCK)
            {
                // If looking at a block
                BlockPos pos = result.getBlockPos();
                if (!worldIn.isClientSide)
                {
                    stack = Helpers.hurtAndBreak(player, player.getUsedItemHand(), stack, 1);

                    BlockState stateAt = worldIn.getBlockState(pos);
                    if (CampfireBlock.canLight(stateAt))
                    {
                        // Light campfire
                        worldIn.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, random.nextFloat() * 0.4F + 0.8F);
                        worldIn.setBlock(pos, stateAt.setValue(BlockStateProperties.LIT, true), 11);
                    }
                    else
                    {
                        List<ItemEntity> entities = worldIn.getEntitiesOfClass(ItemEntity.class, new AxisAlignedBB(pos.above(), pos.offset(1, 2, 1)));
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
                            worldIn.setBlockAndUpdate(pos.above(), Blocks.CAMPFIRE.defaultBlockState().setValue(CampfireBlock.LIT, true));
                        }
                        else
                        {
                            // No fire pit to make, try light a fire
                            if (random.nextFloat() < Config.SERVER.fireStarterFireStartChance.get())
                            {
                                worldIn.setBlockAndUpdate(pos.above(), Blocks.FIRE.defaultBlockState());
                            }
                        }
                    }
                }
            }
        }
        return stack;
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack)
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
        if (player.level.isClientSide && player instanceof PlayerEntity)
        {
            BlockRayTraceResult result = getPlayerPOVHitResult(player.level, (PlayerEntity) player, RayTraceContext.FluidMode.NONE);
            if (random.nextInt(5) == 0)
            {
                player.level.addParticle(ParticleTypes.SMOKE, result.getLocation().x, result.getLocation().y, result.getLocation().z, 0.0F, 0.1F, 0.0F);
            }
        }
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
    {
        return enchantment.category == EnchantmentType.BREAKABLE;
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
}