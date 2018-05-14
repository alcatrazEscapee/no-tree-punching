package notreepunching.util;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;

public class MiscUtil {

    // Checks if an itemstack has the ore name 'name'
    public static boolean doesStackMatchOre(@Nonnull ItemStack stack, String name){
        int[] ids = OreDictionary.getOreIDs(stack);
        for(int id : ids){
            String oreName = OreDictionary.getOreName(id);
            if(name.equals(oreName)){
                return true;
            }
        }
        return false;
    }

    // Checks is an ItemStack has ore names, which have a certian prefix
    // used to search for all 'ingots' / all 'plates' etc.
    public static boolean doesStackMatchOrePrefix(@Nonnull ItemStack stack, String prefix){
        int[] ids = OreDictionary.getOreIDs(stack);
        for(int id : ids){
            String oreName = OreDictionary.getOreName(id);
            if(oreName.length() >= prefix.length()){
                if(oreName.substring(0,prefix.length()).equals(prefix)){
                    return true;
                }
            }
        }
        return false;
    }

    // Gets the opposite of EnumHand
    public static EnumHand getOtherHand(EnumHand hand){
        return hand == EnumHand.MAIN_HAND ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
    }
}
