package notreepunching.util;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;

public class MiscUtil {

    // Checks if an itemstack has the ore name 'name'
    public static boolean doesStackMatchOre(@Nonnull ItemStack stack, String name){
        if(stack.isEmpty()) return false;
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
        if(stack.isEmpty()) return false;
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

    // This both checks if an ore dictionary entry exists, and it it has at least one itemstack
    public static boolean doesOreHaveStack(String ore){
        if(!OreDictionary.doesOreNameExist(ore)) return false;
        NonNullList<ItemStack> stacks = OreDictionary.getOres(ore);
        return !stacks.isEmpty();
    }

    // Gets the temperature for different metals (ore dictionary name)
    // This is roughly based on actual melting temperature (in C)
    // Actual melting temperature (via google) is in comment
    public static int getMetalForgeTemperature(String name){
        switch(name.toLowerCase()){
            case "tin":
                return 300; // 231.9
            case "lead":
                return 400; // 327.5
            case "zinc":
                return 400; // 419.5
            case "aluminium":
                return 700; // 660.3
            case "silver":
                return 900; // 961.8
            case "copper":
                return 1000; // 1085.0
            case "gold":
                return 1100; // 1064.0
            case "nickel":
                return 1400; // 1455.0
            case "iron":
                return 1500; // 1538.0
            case "bronze":
                return 950; // This one is made up for balance :)
            default:
                return 1500;
        }
    }
}
