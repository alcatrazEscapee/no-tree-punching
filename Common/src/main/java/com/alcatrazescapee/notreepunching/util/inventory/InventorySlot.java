package com.alcatrazescapee.notreepunching.util.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class InventorySlot extends Slot
{
    private static final Container EMPTY = new SimpleContainer(0);

    private final ItemStackInventory inventory;
    private final int slot;

    public InventorySlot(ItemStackInventory inventory, int slot, int x, int y)
    {
        super(EMPTY, slot, x, y);

        this.inventory = inventory;
        this.slot = slot;
    }

    @Override
    public boolean mayPlace(ItemStack stack)
    {
        return inventory.canContain(stack);
    }

    @Override
    public ItemStack getItem()
    {
        return inventory.get(slot);
    }

    @Override
    public void set(ItemStack stack)
    {
        inventory.set(slot, stack);
    }

    @Override
    public void setByPlayer(ItemStack stack)
    {
        inventory.set(slot, stack);
    }

    @Override
    public void setChanged()
    {
        inventory.modified();
    }

    @Override
    public int getMaxStackSize()
    {
        return 64;
    }

    @Override
    public ItemStack remove(int amount)
    {
        return inventory.remove(slot, amount);
    }
}
