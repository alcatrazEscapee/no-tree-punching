/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
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
    public static final IItemTier FLINT = new ModItemTier(1, 60, 2.5f, 0.5f, 0, () -> Ingredient.of(Items.FLINT));

    private final int harvestLevel;
    private final int uses;
    private final float speed;
    private final float attackDamage;
    private final int enchantability;
    private final LazyValue<Ingredient> repairMaterial;

    public ModItemTier(int harvestLevel, int uses, float speed, float attackDamage, int enchantability, java.util.function.Supplier<Ingredient> repairMaterial)
    {
        this.harvestLevel = harvestLevel;
        this.uses = uses;
        this.speed = speed;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairMaterial = new LazyValue<>(repairMaterial);
    }

    public int getUses()
    {
        return uses;
    }

    public float getSpeed()
    {
        return speed;
    }

    public float getAttackDamageBonus()
    {
        return attackDamage;
    }

    public int getLevel()
    {
        return harvestLevel;
    }

    public int getEnchantmentValue()
    {
        return enchantability;
    }

    public Ingredient getRepairIngredient()
    {
        return repairMaterial.get();
    }
}