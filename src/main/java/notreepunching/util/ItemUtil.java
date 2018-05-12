package notreepunching.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ItemUtil {

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

    public static boolean canMergeStack(ItemStack stack1, ItemStack stack2){
        if(stack1.isEmpty() || stack2.isEmpty()) { return true; }
        if(stack1.getItem() != stack2.getItem() || stack1.getMetadata() != stack2.getMetadata()) { return false; }
        return stack1.getCount() + stack2.getCount() <= stack1.getMaxStackSize();
    }
    @Nonnull
    public static ItemStack mergeStacks(ItemStack stack, ItemStack stackToAdd){
        if(!canMergeStack(stack, stackToAdd)) { return ItemStack.EMPTY; }
        if(stack.isEmpty()) { return stackToAdd; }
        if(stackToAdd.isEmpty()) { return stack; }
        stack.grow(stackToAdd.getCount());
        return stack;
    }
}
