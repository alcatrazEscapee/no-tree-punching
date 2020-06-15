/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Copyright (c) 2019. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.util;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.registries.ForgeRegistries;

import com.alcatrazescapee.notreepunching.Config;
import com.alcatrazescapee.notreepunching.common.ModTags;

/**
 * Hack all the materials so every material says it requires a tool
 * We then intercept the break check via typical forge methods (that wouldn't've been reached otherwise)
 */
public class MaterialHacks
{
    private static final Set<Material> WIMPY_MATERIALS = new HashSet<>();

    public static void setup()
    {
        ForgeRegistries.BLOCKS.getValues()
            .stream()
            .flatMap(block -> block.getStateContainer()
                .getValidStates()
                .stream()
                .map(BlockState::getMaterial))
            .collect(Collectors.toSet())
            .forEach(material -> {
                // Save it so we can refer to this later
                if (material.isToolNotRequired())
                {
                    WIMPY_MATERIALS.add(material);
                }
                material.requiresNoTool = false;
            });
    }

    /**
     * This is a better version of {@link net.minecraftforge.common.ForgeHooks#canHarvestBlock(BlockState, PlayerEntity, IBlockReader, BlockPos)}
     */
    public static boolean canHarvest(BlockState state, PlayerEntity player)
    {
        if ((Config.SERVER.noBlockDropsWithoutCorrectTool.get() || !WIMPY_MATERIALS.contains(state.getMaterial())) && !ModTags.Blocks.ALWAYS_DROPS.contains(state.getBlock()))
        {
            ItemStack stack = player.getHeldItemMainhand();
            ToolType tool = state.getHarvestTool();
            if (tool != null)
            {
                if (!stack.isEmpty())
                {
                    int toolLevel = stack.getItem().getHarvestLevel(stack, tool, player, state);
                    if (toolLevel >= 0)
                    {
                        return false;
                    }
                }
                return state.getMaterial().isToolNotRequired() || player.inventory.canHarvestBlock(state);
            }
            // No tool that can harvest this block, must always return true
        }
        return true;
    }
}
