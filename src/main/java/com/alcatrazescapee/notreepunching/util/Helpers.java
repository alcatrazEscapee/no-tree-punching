/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.util;

import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullFunction;
import net.minecraftforge.items.IItemHandlerModifiable;

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
    public static <T> Optional<T> getTE(IBlockReader world, BlockPos pos, Class<T> tileClass)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (tileClass.isInstance(tile))
        {
            return Optional.of((T) tile);
        }
        return Optional.empty();
    }

    /**
     * Like {@link InventoryHelper#dropInventoryItems(World, BlockPos, IInventory)} but with item handlers
     */
    public static void dropInventoryItems(World world, BlockPos pos, IItemHandlerModifiable inventory)
    {
        for (int i = 0; i < inventory.getSlots(); i++)
        {
            ItemStack stack = inventory.getStackInSlot(i);
            inventory.setStackInSlot(i, ItemStack.EMPTY);
            if (!stack.isEmpty())
            {
                InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
            }
        }
    }

    /**
     * A simplification of {@link ItemStack#damageItem(int, LivingEntity, Consumer)} that does not require an entity
     */
    public static ItemStack damageItem(ItemStack stack, int amount)
    {
        if (stack.isDamageable())
        {
            if (stack.attemptDamageItem(amount, RANDOM, null))
            {
                stack.shrink(1);
                stack.setDamage(0);
            }
        }
        return stack;
    }

    /**
     * Default argument, and allows a null player
     */
    public static ItemStack damageItem(@Nullable PlayerEntity player, @Nullable Hand hand, ItemStack stack, int amount)
    {
        if (player != null && hand != null)
        {
            stack.damageItem(amount, player, entity -> entity.sendBreakAnimation(hand));
            return stack;
        }
        else
        {
            return Helpers.damageItem(stack, amount);
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
     * Gets a container provider for an item stack, using the item stack's display name as the default name.
     */
    public static INamedContainerProvider getStackContainerProvider(ItemStack stack, IContainerProvider simpleContainerProvider)
    {
        return new INamedContainerProvider()
        {
            @Override
            public ITextComponent getDisplayName()
            {
                return stack.getDisplayName();
            }

            @Nullable
            @Override
            public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player)
            {
                return simpleContainerProvider.createMenu(windowId, playerInventory, player);
            }
        };
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
}
