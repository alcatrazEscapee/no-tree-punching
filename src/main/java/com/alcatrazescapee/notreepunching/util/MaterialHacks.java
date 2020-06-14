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
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Hack all the materials so every material says it requires a tool
 * We then intercept the break check via typical forge methods (that wouldn't've been reached otherwise)
 */
public class MaterialHacks
{
    private static final Set<Material> ALL_MATERIALS = new HashSet<>();

    public static void setup()
    {
        ALL_MATERIALS.addAll(ForgeRegistries.BLOCKS.getValues()
            .stream()
            .flatMap(block -> block.getStateContainer()
                .getValidStates()
                .stream()
                .map(BlockState::getMaterial))
            .collect(Collectors.toSet()));
        ALL_MATERIALS.forEach(material -> material.requiresNoTool = false);
    }
}
