/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Copyright (c) 2019. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.util;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Unit;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.ISelectionContext;
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

    public static Optional<TileEntity> getTE(IBlockReader world, BlockPos pos)
    {
        return Optional.ofNullable(world.getTileEntity(pos));
    }

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
     * Simple extension of {@link Item#rayTrace(World, PlayerEntity, RayTraceContext.FluidMode)} which will return a nullable block ray trace result.
     */
    @Nullable
    @SuppressWarnings("JavadocReference")
    public static BlockRayTraceResult rayTraceBlocks(World worldIn, PlayerEntity player, RayTraceContext.FluidMode fluidMode)
    {
        RayTraceResult result = rayTrace(worldIn, player, fluidMode);
        if (result instanceof BlockRayTraceResult && result.getType() == RayTraceResult.Type.BLOCK)
        {
            return (BlockRayTraceResult) result;
        }
        return null;
    }

    /**
     * Copied from {@link Item#rayTrace(World, PlayerEntity, RayTraceContext.FluidMode)} as it's protected.
     */
    @Nullable
    @SuppressWarnings({"ConstantConditions", "JavadocReference"})
    public static RayTraceResult rayTrace(World worldIn, PlayerEntity player, RayTraceContext.FluidMode fluidMode)
    {
        Vec3d vecFrom = player.getEyePosition(1.0F);
        float cosYaw = MathHelper.cos(-player.rotationYaw * ((float) Math.PI / 180F) - (float) Math.PI);
        float sinYaw = MathHelper.sin(-player.rotationYaw * ((float) Math.PI / 180F) - (float) Math.PI);
        float cosPitch = -MathHelper.cos(-player.rotationPitch * ((float) Math.PI / 180F));
        float sinPitch = MathHelper.sin(-player.rotationPitch * ((float) Math.PI / 180F));
        float xAngle = sinYaw * cosPitch;
        float zAngle = cosYaw * cosPitch;
        double distance = player.getAttribute(PlayerEntity.REACH_DISTANCE).getValue();
        Vec3d vecTo = vecFrom.add((double) xAngle * distance, (double) sinPitch * distance, (double) zAngle * distance);
        return worldIn.rayTraceBlocks(new RayTraceContext(vecFrom, vecTo, RayTraceContext.BlockMode.OUTLINE, fluidMode, player));
    }

    /**
     * Copied from {@code BlockItem#canPlace(BlockItemUseContext, BlockState)} as it's protected, but useful for checking event-based placement logic
     */
    public static boolean canPlace(BlockItemUseContext context, BlockState stateForPlacement)
    {
        PlayerEntity player = context.getPlayer();
        ISelectionContext iselectioncontext = player == null ? ISelectionContext.dummy() : ISelectionContext.forEntity(player);
        return stateForPlacement.isValidPosition(context.getWorld(), context.getPos()) && context.getWorld().func_226663_a_(stateForPlacement, context.getPos(), iselectioncontext);
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
     * Copied from AbstractFurnaceTileEntity#func_214003_a, except public
     */
    public static void spawnExperience(PlayerEntity player, int count, float experience)
    {
        if (experience == 0.0F)
        {
            count = 0;
        }
        else if (experience < 1.0F)
        {
            int i = MathHelper.floor(count * experience);
            if (i < MathHelper.ceil(count * experience) && Math.random() < (count * experience - i))
            {
                ++i;
            }
            count = i;
        }

        while (count > 0)
        {
            int splitAmount = ExperienceOrbEntity.getXPSplit(count);
            count -= splitAmount;
            player.world.addEntity(new ExperienceOrbEntity(player.world, player.getPosX(), player.getPosY() + 0.5D, player.getPosZ() + 0.5D, splitAmount));
        }
    }

    /**
     * Gets all recipes of a given type, since {@link RecipeManager} does not have a convenient way to do this.
     */
    @SuppressWarnings("unchecked")
    public static <R extends IRecipe<C>, C extends IInventory> Collection<R> getRecipes(RecipeManager recipeManager, IRecipeType<R> type)
    {
        return recipeManager.getRecipes().stream().filter(recipe -> recipe.getType() == type).map(recipe -> (R) recipe).collect(Collectors.toList());
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
     * Like {@link Arrays#asList(Object[])} but with a non-immutable list
     */
    @SafeVarargs
    public static <T> List<T> listOf(T... elements)
    {
        List<T> list = new ArrayList<>();
        Collections.addAll(list, elements);
        return list;
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
