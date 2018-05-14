package notreepunching.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ItemUtil {


    // This will attempt to consume an amount of an item, and return the remaining stack
    @Nonnull
    public static ItemStack consumeItem(ItemStack stack){
        return consumeItem(stack, 1);
    }
    @Nonnull
    public static ItemStack consumeItem(ItemStack stack, int amount){
        if(stack.getCount()> amount){
            stack.shrink(amount);
            return stack;
        }
        return ItemStack.EMPTY;
    }

    // This will attempt to get an ItemStack from a registry name, with @Nonnull
    @Nonnull
    public static ItemStack getSafeItem(String name){
        return getSafeItem(name, 0, 1);
    }
    @Nonnull
    public static ItemStack getSafeItem(String name, int count){
        return getSafeItem(name, 0, count);
    }
    @Nonnull
    public static ItemStack getSafeItem(String name, int meta, int count){
        Item item = Item.getByNameOrId(name);
        return item == null ? ItemStack.EMPTY : new ItemStack(item, count, meta);
    }

    // This will check if two stacks can be merged.
    public static boolean canMergeStack(ItemStack stack1, ItemStack stack2){
        if(stack1.isEmpty() || stack2.isEmpty()) { return true; }
        if(stack1.getItem() != stack2.getItem() || stack1.getMetadata() != stack2.getMetadata()) { return false; }
        return stack1.getCount() + stack2.getCount() <= stack1.getMaxStackSize();
    }

    // This will take two stacks and merge them, then return the merged stack. If they can't be merged, ItemStack.EMPTY will be returned.
    @Nonnull
    public static ItemStack mergeStacks(ItemStack stack, ItemStack stackToAdd){
        if(!canMergeStack(stack, stackToAdd)) { return ItemStack.EMPTY; }
        if(stack.isEmpty()) { return stackToAdd; }
        if(stackToAdd.isEmpty()) { return stack; }
        stack.grow(stackToAdd.getCount());
        return stack;
    }

    // This will compare if two stacks are equal in Item and metadata
    public static boolean areStacksEqual(ItemStack stack1, ItemStack stack2){
        // Case both are empty
        if(stack1.isEmpty() && stack2.isEmpty()){ return true; }
        // Case one is empty
        if(stack1.isEmpty() || stack2.isEmpty()){ return false; }
        // Case items are different
        if(stack1.getItem() != stack2.getItem()){ return false; }
        // Check if meta values match
        return stack1.getMetadata() == stack2.getMetadata();
    }
}
