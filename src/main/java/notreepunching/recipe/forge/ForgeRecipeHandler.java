package notreepunching.recipe.forge;

import com.google.common.collect.LinkedListMultimap;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import notreepunching.NoTreePunching;
import notreepunching.block.ModBlocks;
import notreepunching.item.ModItems;
import notreepunching.util.ItemUtil;
import notreepunching.util.MiscUtil;

import java.util.*;

public class ForgeRecipeHandler {

    private static List<ForgeRecipe> FORGE_RECIPES = new ArrayList<>();
    private static LinkedListMultimap<Boolean, ForgeRecipe> CT_ENTRY = LinkedListMultimap.create();

    public static void init(){
        FORGE_RECIPES.add(new ForgeRecipe(new ItemStack(Items.NETHERBRICK), new ItemStack(Blocks.NETHERRACK),400));
        FORGE_RECIPES.add(new ForgeRecipe(new ItemStack(Items.BRICK), new ItemStack(Items.CLAY_BALL),400));

        FORGE_RECIPES.add(new ForgeRecipe(new ItemStack(Blocks.GLASS), new ItemStack(Blocks.SAND,1,0), 300));
        FORGE_RECIPES.add(new ForgeRecipe(new ItemStack(Blocks.STAINED_GLASS,1, EnumDyeColor.RED.getMetadata()), new ItemStack(Blocks.SAND, 1,1), 300));

        FORGE_RECIPES.add(new ForgeRecipe(new ItemStack(Blocks.STONE,1,0), new ItemStack(Blocks.COBBLESTONE), 500));
        FORGE_RECIPES.add(new ForgeRecipe(new ItemStack(Blocks.STONE,1,1), new ItemStack(ModBlocks.graniteCobble), 500));
        FORGE_RECIPES.add(new ForgeRecipe(new ItemStack(Blocks.STONE,1,3), new ItemStack(ModBlocks.dioriteCobble), 500));
        FORGE_RECIPES.add(new ForgeRecipe(new ItemStack(Blocks.STONE,1,5), new ItemStack(ModBlocks.andesiteCobble), 500));

        if(NoTreePunching.replaceQuarkStones){
            FORGE_RECIPES.add(new ForgeRecipe(ItemUtil.getSafeItem("quark:marble"), new ItemStack(ModBlocks.marbleCobble), 500));
            FORGE_RECIPES.add(new ForgeRecipe(ItemUtil.getSafeItem("quark:limestone"), new ItemStack(ModBlocks.limestoneCobble), 500));
        }
        if(NoTreePunching.replaceRusticStone){
            FORGE_RECIPES.add(new ForgeRecipe(ItemUtil.getSafeItem("rustic:slate"), new ItemStack(ModBlocks.slateCobble), 500));
        }

    }

    public static void postInit(){
        // Add ore > ingot recipes based on ore dictionary
        String[] ingotNames = OreDictionary.getOreNames();
        for(String ingotName : ingotNames){
            if(ingotName.length()<=5) continue;
            if(ingotName.substring(0,5).equals("ingot")){
                String type = ingotName.substring(5);
                NonNullList<ItemStack> ingotList = OreDictionary.getOres("ingot"+type);
                if(ingotList.isEmpty()) continue;

                if(MiscUtil.doesOreHaveStack("ore"+type)) {
                    // Ore --> Ingot Recipe
                    FORGE_RECIPES.add(new ForgeRecipe(ingotList.get(0),"ore"+type, 1, MiscUtil.getMetalForgeTemperature(type)));
                }
                if(MiscUtil.doesOreHaveStack("dust"+type)) {
                    // Dust --> Ingot Recipe
                    FORGE_RECIPES.add(new ForgeRecipe(ingotList.get(0),"dust"+type, 1, MiscUtil.getMetalForgeTemperature(type)));
                }
            }
        }

        CT_ENTRY.forEach((action, entry) -> {
            if(action){
                FORGE_RECIPES.add(entry);
            }else {
                FORGE_RECIPES.removeIf(p -> ItemUtil.areStacksEqual(entry.getOutput(),p.getOutput()));
            }
        });
    }

    public static ForgeRecipe getRecipe(ItemStack stack){ return getRecipe(stack, false); }
    public static boolean isIngredient(ItemStack stack){
        return getRecipe(stack, true) != null;
    }
    private static ForgeRecipe getRecipe(ItemStack stack, boolean skipCountCheck){
        for(ForgeRecipe recipe : FORGE_RECIPES) {
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

    public static List<ForgeRecipe> getAll() { return FORGE_RECIPES; }

    // Craft Tweaker
    public static void addEntry(ForgeRecipe recipe, boolean type){
        CT_ENTRY.put(type, recipe);
    }

}
