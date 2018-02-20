package notreepunching.recipe;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryModifiable;
import notreepunching.config.Config;
import notreepunching.item.ModItems;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ModRecipes {

    public static List<CuttingRecipe> CUTTING_RECIPES = new ArrayList<CuttingRecipe>();

    public ModRecipes(){
    }

    public static void init(){

        // Init Cutting Recipes
        CUTTING_RECIPES.add(new CuttingRecipe(ModItems.rock,new ItemStack[]{new ItemStack(Items.FLINT)}));
        CUTTING_RECIPES.add(new CuttingRecipe(Item.getItemFromBlock(Blocks.WOOL),new ItemStack[]{new ItemStack(Items.STRING,2)}));

        CUTTING_RECIPES.add(new CuttingRecipe(Items.REEDS,new ItemStack[]{new ItemStack(ModItems.grassFiber,2)}));
        CUTTING_RECIPES.add(new CuttingRecipe(Items.WHEAT,new ItemStack[]{new ItemStack(ModItems.grassFiber,1), new ItemStack(Items.WHEAT_SEEDS)}));
        CUTTING_RECIPES.add(new CuttingRecipe(Item.getItemFromBlock(Blocks.SAPLING),new ItemStack[]{new ItemStack(Items.STICK),new ItemStack(ModItems.grassFiber)}));

        CUTTING_RECIPES.add(new CuttingRecipe(Items.LEATHER_BOOTS,new ItemStack[]{new ItemStack(Items.LEATHER,2)}));
        CUTTING_RECIPES.add(new CuttingRecipe(Items.LEATHER_CHESTPLATE,new ItemStack[]{new ItemStack(Items.LEATHER,5)}));
        CUTTING_RECIPES.add(new CuttingRecipe(Items.LEATHER_LEGGINGS,new ItemStack[]{new ItemStack(Items.LEATHER,4)}));
        CUTTING_RECIPES.add(new CuttingRecipe(Items.LEATHER_HELMET,new ItemStack[]{new ItemStack(Items.LEATHER,3)}));

        CUTTING_RECIPES.add(new CuttingRecipe(Item.getItemFromBlock(Blocks.MELON_BLOCK),new ItemStack[]{new ItemStack(Items.MELON,9)}));
        }

    public static boolean isCuttingRecipe(ItemStack stack){
        return getCuttingRecipe(stack)!=null;
    }

    public static CuttingRecipe getCuttingRecipe(ItemStack stack){
        for(int i=0;i<CUTTING_RECIPES.size();i++){
            if(CUTTING_RECIPES.get(i).getInputItem() == stack.getItem()){
                return CUTTING_RECIPES.get(i);
            }
        }
        return null;
    }

    public static void removeVanillaRecipes(IForgeRegistry<IRecipe> reg){
        IForgeRegistryModifiable modRegistry = (IForgeRegistryModifiable) reg;

        // Wooden Tools
        if(Config.CFG_WOOD_TOOLS_DISABLE) {
            modRegistry.remove(new ResourceLocation("minecraft:wooden_pickaxe"));
            modRegistry.remove(new ResourceLocation("minecraft:wooden_shovel"));
            modRegistry.remove(new ResourceLocation("minecraft:wooden_hoe"));
            modRegistry.remove(new ResourceLocation("minecraft:wooden_sword"));
            modRegistry.remove(new ResourceLocation("minecraft:wooden_axe"));
        }

        // Stone Tools
        if(Config.CFG_STONE_TOOLS_DISABLE) {
            modRegistry.remove(new ResourceLocation("minecraft:stone_pickaxe"));
            modRegistry.remove(new ResourceLocation("minecraft:stone_shovel"));
            modRegistry.remove(new ResourceLocation("minecraft:stone_hoe"));
            modRegistry.remove(new ResourceLocation("minecraft:stone_sword"));
            modRegistry.remove(new ResourceLocation("minecraft:stone_axe"));
        }
    }
}
