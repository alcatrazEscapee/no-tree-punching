package com.alcatrazescapee.notreepunching.common.items;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraftforge.common.ToolAction;

public class ForgeMattockItem extends MattockItem
{
    public ForgeMattockItem(Tier tier, float attackDamage, float attackSpeed, Properties properties)
    {
        super(tier, attackDamage, attackSpeed, properties);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction action)
    {
        return new ItemStack(Items.DIAMOND_AXE).canPerformAction(action)
            || new ItemStack(Items.DIAMOND_HOE).canPerformAction(action)
            || new ItemStack(Items.DIAMOND_SHOVEL).canPerformAction(action);
    }
}
