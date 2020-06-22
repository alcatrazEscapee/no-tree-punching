/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Copyright (c) 2019. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common;

import net.minecraft.item.IItemTier;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;

/**
 * Mod-usable implementation of {@link IItemTier}
 */
public class ModItemTier implements IItemTier
{
    public static final IItemTier FLINT = new ModItemTier(1, 60, 2.5f, 0.5f, 0, () -> Ingredient.fromItems(Items.FLINT));

    private final int harvestLevel;
    private final int maxUses;
    private final float efficiency;
    private final float attackDamage;
    private final int enchantability;
    private final LazyValue<Ingredient> repairMaterial;

    public ModItemTier(int harvestLevel, int maxUses, float efficiency, float attackDamage, int enchantability, java.util.function.Supplier<Ingredient> repairMaterial)
    {
        this.harvestLevel = harvestLevel;
        this.maxUses = maxUses;
        this.efficiency = efficiency;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairMaterial = new LazyValue<>(repairMaterial);
    }

    public int getMaxUses()
    {
        return maxUses;
    }

    public float getEfficiency()
    {
        return efficiency;
    }

    public float getAttackDamage()
    {
        return attackDamage;
    }

    public int getHarvestLevel()
    {
        return harvestLevel;
    }

    public int getEnchantability()
    {
        return enchantability;
    }

    public Ingredient getRepairMaterial()
    {
        return repairMaterial.getValue();
    }
}
