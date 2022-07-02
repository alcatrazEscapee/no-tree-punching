package com.alcatrazescapee.notreepunching.common.items;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;

import com.alcatrazescapee.notreepunching.common.ModTags;

public class MattockItem extends DiggerItem
{
    private static InteractionResult onAxeItemUse(UseOnContext context)
    {
        return Items.DIAMOND_AXE.useOn(context);
    }

    private static InteractionResult onHoeItemUse(UseOnContext context)
    {
        return Items.DIAMOND_HOE.useOn(context);
    }

    private static InteractionResult onShovelItemUse(UseOnContext context)
    {
        return Items.DIAMOND_SHOVEL.useOn(context);
    }

    public MattockItem(Tier tier, float attackDamageIn, float attackSpeedIn, Properties builder)
    {
        super(attackDamageIn, attackSpeedIn, tier, ModTags.Blocks.MINEABLE_WITH_MATTOCK, builder);
    }

    /**
     * If not sneaking, use in order of Axe -> Shovel -> Hoe
     * Otherwise, use in order of Axe -> Hoe -> Shovel
     * This is done as hoe and shovel have a possibility of conflicting (within vanilla)
     */
    @Override
    public InteractionResult useOn(UseOnContext context)
    {
        InteractionResult result = onAxeItemUse(context);
        if (result == InteractionResult.PASS)
        {
            if (context.getPlayer() != null && context.getPlayer().isShiftKeyDown())
            {
                result = onHoeItemUse(context);
                if (result == InteractionResult.PASS)
                {
                    result = onShovelItemUse(context);
                }
            }
            else
            {
                result = onShovelItemUse(context);
                if (result == InteractionResult.PASS)
                {
                    result = onHoeItemUse(context);
                }
            }
        }
        return result;
    }
}