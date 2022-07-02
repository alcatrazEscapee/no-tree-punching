package com.alcatrazescapee.notreepunching.util.inventory;

import java.util.function.Predicate;
import net.minecraft.core.NonNullList;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;

public class ItemStackAttachedInventory implements ItemStackInventory
{
    public static Factory create(int slots, Predicate<ItemStack> predicate)
    {
        return stack -> new ItemStackAttachedInventory(stack, slots, predicate);
    }

    private final ItemStack stack;
    private final Predicate<ItemStack> predicate;
    private final NonNullList<ItemStack> stacks;

    public ItemStackAttachedInventory(ItemStack stack, int slots, Predicate<ItemStack> predicate)
    {
        this.stack = stack;
        this.predicate = predicate;
        this.stacks = NonNullList.withSize(slots, ItemStack.EMPTY);

        ContainerHelper.loadAllItems(stack.getOrCreateTag(), stacks);
    }

    @Override
    public NonNullList<ItemStack> slots()
    {
        return stacks;
    }

    @Override
    public void modified()
    {
        ContainerHelper.saveAllItems(stack.getOrCreateTag(), stacks);
    }

    @Override
    public boolean canContain(ItemStack stack)
    {
        return predicate.test(stack);
    }

    @FunctionalInterface
    public interface Factory
    {
        ItemStackAttachedInventory create(ItemStack stack);
    }
}
