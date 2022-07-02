package com.alcatrazescapee.notreepunching.platform;

import java.util.function.Supplier;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public record FabricTier(int uses, float speed, float attackDamageBonus, int level, int enchantmentValue, Supplier<Ingredient> repairIngredient) implements Tier
{
    @Override
    public int getUses()
    {
        return uses;
    }

    @Override
    public float getSpeed()
    {
        return speed;
    }

    @Override
    public float getAttackDamageBonus()
    {
        return attackDamageBonus;
    }

    @Override
    public int getLevel()
    {
        return level;
    }

    @Override
    public int getEnchantmentValue()
    {
        return enchantmentValue;
    }

    @Override
    public Ingredient getRepairIngredient()
    {
        return repairIngredient.get();
    }
}
