package notreepunching.recipe.forge;

import com.google.common.collect.LinkedListMultimap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import notreepunching.block.ModBlocks;
import notreepunching.item.ModItems;
import notreepunching.util.ItemUtil;

import java.util.ArrayList;
import java.util.List;

public class BlastRecipeHandler {

    private static List<ForgeRecipe> BLAST_RECIPES = new ArrayList<>();
    private static LinkedListMultimap<Boolean, ForgeRecipe> CT_ENTRY = LinkedListMultimap.create();

    public static void init(){
        BLAST_RECIPES.add(new ForgeRecipe(new ItemStack(ModItems.ingotSteel), "dustIron", 1, 1050));
        BLAST_RECIPES.add(new ForgeRecipe(new ItemStack(ModItems.ingotSteel), "ingotIron", 1, 1050));
        BLAST_RECIPES.add(new ForgeRecipe(new ItemStack(ModItems.ingotSteel), "oreIron", 1, 1050));
        BLAST_RECIPES.add(new ForgeRecipe(new ItemStack(ModBlocks.blockSteel), "blockIron",1,1050));
    }

    public static void postInit(){
        CT_ENTRY.forEach((action, entry) -> {
            if(action){
                BLAST_RECIPES.add(entry);
            }else {
                BLAST_RECIPES.removeIf(p -> ItemUtil.areStacksEqual(entry.getOutput(),p.getOutput()));
            }
        });
    }

    public static ForgeRecipe getRecipe(ItemStack stack){ return getRecipe(stack, false); }
    public static boolean isIngredient(ItemStack stack){
        return getRecipe(stack, true) != null;
    }
    private static ForgeRecipe getRecipe(ItemStack stack, boolean skipCountCheck){
        for(ForgeRecipe recipe : BLAST_RECIPES) {
            if (recipe.getOre().equals("")) {
                if(ItemUtil.areStacksEqual(stack, recipe.getStack()) && (stack.getCount() >= recipe.getCount() || skipCountCheck)){
                    return recipe;
                }
            }
            else {
                NonNullList<ItemStack> oreList = OreDictionary.getOres(recipe.getOre());
                for (ItemStack oreStack : oreList) {
                    if (ItemUtil.areStacksEqual(stack, oreStack) && (stack.getCount() >= recipe.getCount() || skipCountCheck)) {
                        return recipe;
                    }
                }
            }
        }
        return null;
    }

    public static List<ForgeRecipe> getAll() { return BLAST_RECIPES; }

    // Craft Tweaker
    public static void addEntry(ForgeRecipe recipe, boolean type){
        CT_ENTRY.put(type, recipe);
    }
}
