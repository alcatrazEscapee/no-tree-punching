package notreepunching.recipe.forge;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import notreepunching.util.ItemUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ForgeRecipeHandler {

    private static List<ForgeRecipe> FORGE_RECIPES = new ArrayList<ForgeRecipe>();
    private static List<ItemStack> CT_REMOVE = new ArrayList<ItemStack>();

    public static void init(){
        FORGE_RECIPES.add(new ForgeRecipe(new ItemStack(Items.NETHERBRICK), new ItemStack(Blocks.NETHERRACK),400));
        FORGE_RECIPES.add(new ForgeRecipe(new ItemStack(Items.BRICK), new ItemStack(Items.CLAY_BALL),400));
    }

    public static void postInit(){
        // Add ore > ingot recipes based on ore dictionary
        String[] oreNames = OreDictionary.getOreNames();
        for(String oreName : oreNames){
            if(oreName.substring(0,3).equals("ore")){
                if(OreDictionary.doesOreNameExist("ingot"+oreName.substring(3))){
                    FORGE_RECIPES.add(new ForgeRecipe(OreDictionary.getOres("ingot"+oreName.substring(3)).get(0),oreName,1000));
                }
            }
        }

        for(ItemStack stack : CT_REMOVE){
            FORGE_RECIPES.removeIf(p -> ItemUtil.areStacksEqual(stack,p.getOutput()));
        }
    }

    public static ForgeRecipe getRecipe(ItemStack stack){ return getRecipe(stack, false); }
    private static ForgeRecipe getRecipe(ItemStack stack, boolean skipCountCheck){
        for(ForgeRecipe recipe : FORGE_RECIPES) {
            if (recipe.getOre().equals("")) {
                if(ItemUtil.areStacksEqual(stack, recipe.getStack()) && (stack.getCount() >= 1 || skipCountCheck)){
                    return recipe;
                }
            }
            else {
                NonNullList<ItemStack> oreList = OreDictionary.getOres(recipe.getOre());
                for (ItemStack oreStack : oreList) {
                    if (ItemUtil.areStacksEqual(stack, oreStack) && (stack.getCount() >= 1 || skipCountCheck)) {
                        return recipe;
                    }
                }
            }
        }
        return null;
    }

    public static boolean isRecipe(ItemStack stack){
        return getRecipe(stack) != null;
    }
    public static boolean isIngredient(ItemStack stack){
        return getRecipe(stack, true) != null;
    }

    public static List<ForgeRecipe> getAll() { return FORGE_RECIPES; }

    // Craft Tweaker
    public static void addRecipe(ForgeRecipe recipe){
        FORGE_RECIPES.add(recipe);
    }

    public static void removeRecipe(ItemStack stack){
        System.out.println("ATTEMPTING TO REMOVE!!!");
        CT_REMOVE.add(stack);
        //Iterator<ForgeRecipe> iterator = FORGE_RECIPES.iterator();
        //FORGE_RECIPES.removeIf(p -> ItemUtil.areStacksEqual(stack,p.getStack()));
        /*while(iterator.hasNext()){
            if(ItemStack.areItemStacksEqual(stack, iterator.next().getOutput())){
                iterator.remove();
                //break;
            }
        }*/
    }

}
