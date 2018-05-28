package notreepunching.recipe.grindstone;

import com.google.common.collect.LinkedListMultimap;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import notreepunching.config.ModConfig;
import notreepunching.item.ModItems;
import notreepunching.recipe.ModRecipes;
import notreepunching.recipe.forge.ForgeRecipe;
import notreepunching.util.ItemUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class GrindstoneRecipeHandler {

    private static List<GrindstoneRecipe> GRINDSTONE_RECIPES = new ArrayList<GrindstoneRecipe>();
    private static LinkedListMultimap<Boolean, GrindstoneRecipe> CT_ENTRY = LinkedListMultimap.create();

    public static void init(){
        GRINDSTONE_RECIPES.add(new GrindstoneRecipe(new ItemStack(ModItems.dustTin), "oreTin", ModConfig.Balance.DUST_PER_ORE));
        GRINDSTONE_RECIPES.add(new GrindstoneRecipe(new ItemStack(ModItems.dustCopper), "oreCopper", ModConfig.Balance.DUST_PER_ORE));
        GRINDSTONE_RECIPES.add(new GrindstoneRecipe(new ItemStack(ModItems.dustIron), "oreIron", ModConfig.Balance.DUST_PER_ORE));

        // Dyes from plants and other things
        GRINDSTONE_RECIPES.add(new GrindstoneRecipe(new ItemStack(Items.DYE,2, EnumDyeColor.RED.getDyeDamage()), new ItemStack(Blocks.RED_FLOWER, 1, BlockFlower.EnumFlowerType.POPPY.getMeta())));
        GRINDSTONE_RECIPES.add(new GrindstoneRecipe(new ItemStack(Items.DYE,2, EnumDyeColor.RED.getDyeDamage()), new ItemStack(Blocks.RED_FLOWER,1, BlockFlower.EnumFlowerType.RED_TULIP.getMeta())));
        GRINDSTONE_RECIPES.add(new GrindstoneRecipe(new ItemStack(Items.DYE,4, EnumDyeColor.RED.getDyeDamage()), new ItemStack(Blocks.DOUBLE_PLANT,1, BlockDoublePlant.EnumPlantType.ROSE.getMeta())));
        GRINDSTONE_RECIPES.add(new GrindstoneRecipe(new ItemStack(Items.DYE,2, EnumDyeColor.YELLOW.getDyeDamage()), new ItemStack(Blocks.YELLOW_FLOWER,1, BlockFlower.EnumFlowerType.DANDELION.getMeta())));
        GRINDSTONE_RECIPES.add(new GrindstoneRecipe(new ItemStack(Items.DYE,4, EnumDyeColor.YELLOW.getDyeDamage()), new ItemStack(Blocks.DOUBLE_PLANT,1, BlockDoublePlant.EnumPlantType.SUNFLOWER.getMeta())));
        GRINDSTONE_RECIPES.add(new GrindstoneRecipe(new ItemStack(Items.DYE,2, EnumDyeColor.ORANGE.getDyeDamage()), new ItemStack(Blocks.RED_FLOWER,1, BlockFlower.EnumFlowerType.ORANGE_TULIP.getMeta())));
        GRINDSTONE_RECIPES.add(new GrindstoneRecipe(new ItemStack(Items.DYE,2, EnumDyeColor.GREEN.getDyeDamage()), new ItemStack(Blocks.CACTUS)));
        GRINDSTONE_RECIPES.add(new GrindstoneRecipe(new ItemStack(Items.DYE,2, EnumDyeColor.LIGHT_BLUE.getDyeDamage()), new ItemStack(Blocks.RED_FLOWER,1, BlockFlower.EnumFlowerType.BLUE_ORCHID.getMeta())));
        GRINDSTONE_RECIPES.add(new GrindstoneRecipe(new ItemStack(Items.DYE,2, EnumDyeColor.MAGENTA.getDyeDamage()), new ItemStack(Blocks.RED_FLOWER,1, BlockFlower.EnumFlowerType.ALLIUM.getMeta())));
        GRINDSTONE_RECIPES.add(new GrindstoneRecipe(new ItemStack(Items.DYE,4, EnumDyeColor.MAGENTA.getDyeDamage()), new ItemStack(Blocks.DOUBLE_PLANT,1, BlockDoublePlant.EnumPlantType.SYRINGA.getMeta())));
        GRINDSTONE_RECIPES.add(new GrindstoneRecipe(new ItemStack(Items.DYE,2, EnumDyeColor.WHITE.getDyeDamage()), new ItemStack(Blocks.RED_FLOWER,1, BlockFlower.EnumFlowerType.WHITE_TULIP.getMeta())));
        GRINDSTONE_RECIPES.add(new GrindstoneRecipe(new ItemStack(Items.DYE,2, EnumDyeColor.WHITE.getDyeDamage()), new ItemStack(Blocks.RED_FLOWER,1, BlockFlower.EnumFlowerType.OXEYE_DAISY.getMeta())));
        GRINDSTONE_RECIPES.add(new GrindstoneRecipe(new ItemStack(Items.DYE,2, EnumDyeColor.WHITE.getDyeDamage()), new ItemStack(Blocks.RED_FLOWER,1, BlockFlower.EnumFlowerType.HOUSTONIA.getMeta())));
        GRINDSTONE_RECIPES.add(new GrindstoneRecipe(new ItemStack(Items.DYE,2, EnumDyeColor.PINK.getDyeDamage()), new ItemStack(Blocks.RED_FLOWER,1, BlockFlower.EnumFlowerType.PINK_TULIP.getMeta())));
        GRINDSTONE_RECIPES.add(new GrindstoneRecipe(new ItemStack(Items.DYE,4, EnumDyeColor.PINK.getDyeDamage()), new ItemStack(Blocks.DOUBLE_PLANT,1, BlockDoublePlant.EnumPlantType.PAEONIA.getMeta())));

        // Misc recipes
        GRINDSTONE_RECIPES.add(new GrindstoneRecipe(new ItemStack(Items.SUGAR,2), new ItemStack(Items.REEDS)));
        GRINDSTONE_RECIPES.add(new GrindstoneRecipe(new ItemStack(Items.DYE, 5, EnumDyeColor.WHITE.getDyeDamage()), new ItemStack(Items.BONE)));
        GRINDSTONE_RECIPES.add(new GrindstoneRecipe(new ItemStack(Items.BLAZE_POWDER,3),new ItemStack(Items.BLAZE_ROD)));

        // Flint and things
        GRINDSTONE_RECIPES.add(new GrindstoneRecipe(new ItemStack(ModItems.flintShard,3), new ItemStack(Items.FLINT)));
        GRINDSTONE_RECIPES.add(new GrindstoneRecipe(new ItemStack(ModItems.flintShard, 2), new ItemStack(ModItems.rockStone,1,OreDictionary.WILDCARD_VALUE)));
        GRINDSTONE_RECIPES.add(new GrindstoneRecipe(new ItemStack(Items.FLINT), new ItemStack(Blocks.GRAVEL)));
    }

    public static void postInit(){
        // Do CT things

        CT_ENTRY.forEach((action, entry) -> {
            if(action){
                GRINDSTONE_RECIPES.add(entry);
            }else {
                GRINDSTONE_RECIPES.removeIf(p -> ItemUtil.areStacksEqual(entry.getOutput(),p.getOutput()));
            }
        });
    }

    public static boolean isIngredient(ItemStack stack){
        return getRecipe(stack, true) != null;
    }
    @Nullable
    public static GrindstoneRecipe getRecipe(@Nonnull ItemStack stack, boolean skipCountCheck){
        for(GrindstoneRecipe recipe : GRINDSTONE_RECIPES) {
            if (recipe.getOreInput().equals("")) {
                if(ItemUtil.areStacksEqual(stack, recipe.getInput()) && (stack.getCount() >= 1 || skipCountCheck)){
                    return recipe;
                }
            }
            else {
                NonNullList<ItemStack> oreList = OreDictionary.getOres(recipe.getOreInput());
                for (ItemStack oreStack : oreList) {
                    if (ItemUtil.areStacksEqual(stack, oreStack) && (stack.getCount() >= 1 || skipCountCheck)) {
                        return recipe;
                    }
                }
            }
        }
        return null;
    }

    public static List<GrindstoneRecipe> getAll(){
        return GRINDSTONE_RECIPES;
    }
    // Craft Tweaker
    public static void addEntry(GrindstoneRecipe recipe, boolean type){
        CT_ENTRY.put(type, recipe);
    }
}
