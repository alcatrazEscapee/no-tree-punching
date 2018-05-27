package notreepunching.util;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;

@MethodsReturnNonnullByDefault
public class ItemUtil {


    // This will attempt to consume an amount of an item, and return the remaining stack
    public static ItemStack consumeItem(ItemStack stack){
        return consumeItem(stack, 1);
    }
    public static ItemStack consumeItem(ItemStack stack, int amount){
        if(stack.getCount()> amount){
            stack.shrink(amount);
            return stack;
        }
        return ItemStack.EMPTY;
    }

    // This will attempt to damage an item, and return the remaining stack
    public static ItemStack damageItem(ItemStack stack){ return damageItem(stack, 1); }
    public static ItemStack damageItem(ItemStack stack, int amount){
        if(stack.isItemStackDamageable()) {
            if (stack.getItemDamage() + 1 == stack.getMaxDamage()) {
                return ItemStack.EMPTY;
            }
            stack.setItemDamage(stack.getItemDamage() + 1);
        }
        return stack;
    }

    // This will attempt to get an ItemStack from a registry name, with @Nonnull
    public static ItemStack getSafeItem(String name){
        return getSafeItem(name, 0, 1);
    }
    public static ItemStack getSafeItem(String name, int count){
        return getSafeItem(name, 0, count);
    }
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
    public static ItemStack mergeStacks(ItemStack stack, ItemStack stackToAdd){
        if(!canMergeStack(stack, stackToAdd)) { return ItemStack.EMPTY; }
        if(stack.isEmpty()) { return stackToAdd.copy(); }
        if(stackToAdd.isEmpty()) { return stack.copy(); }
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
        // Case metadata is wildcard on either:
        if(stack1.getMetadata() == OreDictionary.WILDCARD_VALUE || stack2.getMetadata() == OreDictionary.WILDCARD_VALUE){ return true; }
        // Check if meta values match
        return stack1.getMetadata() == stack2.getMetadata();
    }

    public static ItemStack copyStack(ItemStack stack, int count){
        ItemStack stack2 = stack.copy();
        stack2.setCount(count);
        return stack2;
    }
}
