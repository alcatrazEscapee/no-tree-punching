package com.alcatrazescapee.notreepunching.util;

import java.util.Random;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import com.alcatrazescapee.notreepunching.NoTreePunching;

/**
 * A collection of helper methods and utilities
 */
public final class Helpers
{
    private static final Random RANDOM = new Random();

    public static ResourceLocation identifier(String path)
    {
        return new ResourceLocation(NoTreePunching.MOD_ID, path);
    }

    /**
     * A simplification of {@link ItemStack#hurtAndBreak(int, LivingEntity, Consumer)} that does not require an entity
     */
    public static ItemStack hurtAndBreak(ItemStack stack, int amount)
    {
        if (stack.isDamageableItem())
        {
            if (stack.hurt(amount, RANDOM, null))
            {
                stack.shrink(1);
                stack.setDamageValue(0);
            }
        }
        return stack;
    }

    public static boolean isItem(Item item, TagKey<Item> tag)
    {
        return item.builtInRegistryHolder().is(tag);
    }

    public static boolean isBlock(Block block, TagKey<Block> tag)
    {
        return block.builtInRegistryHolder().is(tag);
    }

    /**
     * Default argument, and allows a null player
     */
    public static ItemStack hurtAndBreak(@Nullable Player player, @Nullable InteractionHand hand, ItemStack stack, int amount)
    {
        if (player != null && hand != null)
        {
            stack.hurtAndBreak(amount, player, entity -> entity.broadcastBreakEvent(hand));
            return stack;
        }
        else
        {
            return Helpers.hurtAndBreak(stack, amount);
        }
    }

    /**
     * Like {@link Capability#orEmpty(Capability, LazyOptional)} except it properly checks for nulls
     * The method is annotated as {@link Nonnull}, but since there *may* be a case where it is called very early, incorrect handling may permit the capability to be null. So instead of crashing, we gracefully log an error message.
     */
    public static <T, R> LazyOptional<R> getCapabilityWithNullChecks(@Nullable Capability<R> capability, @Nullable Capability<T> capabilityToCheck, LazyOptional<T> instance)
    {
        if (capabilityToCheck == null || capability == null)
        {
            return LazyOptional.empty();
        }
        return capabilityToCheck.orEmpty(capability, instance);
    }
}