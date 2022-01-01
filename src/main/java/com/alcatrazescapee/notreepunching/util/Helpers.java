/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.util;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.util.Unit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullFunction;

/**
 * A collection of helper methods and utilities
 */
public final class Helpers
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Random RANDOM = new Random();

    /**
     * Gets a tile entity and casts it to the required type
     */
    @SuppressWarnings("unchecked")
    public static <T> Optional<T> getTE(BlockGetter world, BlockPos pos, Class<T> tileClass)
    {
        BlockEntity tile = world.getBlockEntity(pos);
        if (tileClass.isInstance(tile))
        {
            return Optional.of((T) tile);
        }
        return Optional.empty();
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
     * This is useful for when we want to use {@link LazyOptional#map(NonNullFunction)} but not require a return value.
     */
    public static <T> void ifPresentOrElse(LazyOptional<T> lazyOptional, Consumer<T> ifPresent, Runnable orElse)
    {
        lazyOptional.map(internal -> {
            ifPresent.accept(internal);
            return Unit.INSTANCE;
        }).orElseGet(() -> {
            orElse.run();
            return Unit.INSTANCE;
        });
    }

    /**
     * Java 9 {@code Optional#ifPresentOrElse}
     */
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static <T> void ifPresentOrElse(Optional<T> optional, Consumer<T> ifPresent, Runnable orElse)
    {
        optional.map(internal -> {
            ifPresent.accept(internal);
            return Unit.INSTANCE;
        }).orElseGet(() -> {
            orElse.run();
            return Unit.INSTANCE;
        });
    }

    /**
     * Like {@link Capability#orEmpty(Capability, LazyOptional)} except it properly checks for nulls
     * The method is annotated as {@link Nonnull}, but since there *may* be a case where it is called very early, incorrect handling may permit the capability to be null. So instead of crashing, we gracefully log an error message.
     */
    public static <T, R> LazyOptional<R> getCapabilityOrElse(@Nullable Capability<R> capability, @Nullable Capability<T> capabilityToCheck, LazyOptional<T> instance)
    {
        if (capabilityToCheck == null || capability == null)
        {
            LOGGER.warn("Something called getCapability with a null capability! This is illegal");
            LOGGER.debug(new RuntimeException("Stacktrace"));
            return LazyOptional.empty();
        }
        return capabilityToCheck.orEmpty(capability, instance);
    }

    /**
     * Used for public static final fields that are injected by forge
     * Stops IDE warnings
     */
    @Nonnull
    @SuppressWarnings("ConstantConditions")
    public static <T> T getNull()
    {
        return null;
    }

    public static <K, V> void putAll(Map<K, V> map, V value, K... keys)
    {
        for (K k : keys)
        {
            map.put(k, value);
        }
    }

    public static Field findUnobfField(Class<?> clazz, String fieldName)
    {
        try
        {
            final Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        }
        catch (NoSuchFieldException e)
        {
            throw new RuntimeException("Unable to find unobf field for " + clazz.getSimpleName() + "#" + fieldName + ", this is a bug!", e);
        }
    }
}