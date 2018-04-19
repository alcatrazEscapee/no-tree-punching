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
        CUTTING_RECIPES.add(new CuttingRecipe(new ItemStack(ModItems.rock,1),new ItemStack(Items.FLINT)));
        CUTTING_RECIPES.add(new CuttingRecipe(new ItemStack(Blocks.WOOL),new ItemStack(Items.STRING,4)));

        CUTTING_RECIPES.add(new CuttingRecipe(new ItemStack(Items.REEDS),new ItemStack(ModItems.grassFiber,2)));
        CUTTING_RECIPES.add(new CuttingRecipe(new ItemStack(Items.WHEAT),new ItemStack(ModItems.grassFiber,1), new ItemStack(Items.WHEAT_SEEDS)));
        CUTTING_RECIPES.add(new CuttingRecipe(new ItemStack(Blocks.SAPLING),new ItemStack(Items.STICK),new ItemStack(ModItems.grassFiber)));

        CUTTING_RECIPES.add(new CuttingRecipe(new ItemStack(Items.LEATHER_BOOTS),new ItemStack(Items.LEATHER,2)));
        CUTTING_RECIPES.add(new CuttingRecipe(new ItemStack(Items.LEATHER_CHESTPLATE),new ItemStack(Items.LEATHER,5)));
        CUTTING_RECIPES.add(new CuttingRecipe(new ItemStack(Items.LEATHER_LEGGINGS),new ItemStack(Items.LEATHER,4)));
        CUTTING_RECIPES.add(new CuttingRecipe(new ItemStack(Items.LEATHER_HELMET),new ItemStack(Items.LEATHER,3)));

        CUTTING_RECIPES.add(new CuttingRecipe(new ItemStack(Blocks.MELON_BLOCK),new ItemStack(Items.MELON,9)));
        CUTTING_RECIPES.add(new CuttingRecipe(new ItemStack(Items.MELON),new ItemStack(Items.MELON_SEEDS,1),new ItemStack(ModItems.grassFiber)));
        CUTTING_RECIPES.add(new CuttingRecipe(new ItemStack(Blocks.PUMPKIN),new ItemStack(Items.PUMPKIN_SEEDS,4),new ItemStack(ModItems.grassFiber,2)));

        CUTTING_RECIPES.add(new CuttingRecipe(new ItemStack(Blocks.LEAVES,6,0),new ItemStack(Blocks.SAPLING,1,0),new ItemStack(ModItems.grassFiber,2)));
        CUTTING_RECIPES.add(new CuttingRecipe(new ItemStack(Blocks.LEAVES,6,1),new ItemStack(Blocks.SAPLING,1,1),new ItemStack(ModItems.grassFiber,2)));
        CUTTING_RECIPES.add(new CuttingRecipe(new ItemStack(Blocks.LEAVES,6,2),new ItemStack(Blocks.SAPLING,1,2),new ItemStack(ModItems.grassFiber,2)));
        CUTTING_RECIPES.add(new CuttingRecipe(new ItemStack(Blocks.LEAVES,6,3),new ItemStack(Blocks.SAPLING,1,3),new ItemStack(ModItems.grassFiber,2)));

        CUTTING_RECIPES.add(new CuttingRecipe(new ItemStack(Blocks.LEAVES2,6,0),new ItemStack(Blocks.SAPLING,1,4),new ItemStack(ModItems.grassFiber,2)));
        CUTTING_RECIPES.add(new CuttingRecipe(new ItemStack(Blocks.LEAVES2,6,1),new ItemStack(Blocks.SAPLING,1,5),new ItemStack(ModItems.grassFiber,2)));

        // Add Smelting
        GameRegistry.addSmelting(new ItemStack(ModItems.grassString),new ItemStack(Items.STRING),1.0F);
        GameRegistry.addSmelting(new ItemStack(ModItems.poorIron),new ItemStack(Items.IRON_NUGGET,2),1.0F);

        }

    public static boolean isCuttingRecipe(ItemStack stack){
        return getCuttingRecipe(stack)!=null;
    }

    public static CuttingRecipe getCuttingRecipe(ItemStack stack){
        for(int i=0;i<CUTTING_RECIPES.size();i++){
            ItemStack is = CUTTING_RECIPES.get(i).getInput();
            if(is.getItem() == stack.getItem() && stack.getCount()>=is.getCount() && is.getMetadata() == stack.getMetadata()){
                return CUTTING_RECIPES.get(i);
            }
        }
        return null;
    }

    public static void removeVanillaRecipes(IForgeRegistry<IRecipe> reg){
        IForgeRegistryModifiable modRegistry = (IForgeRegistryModifiable) reg;

        // Wooden Tools
        if(Config.VanillaTweaks.WOOD_TOOLS_DISABLE) {
            modRegistry.remove(new ResourceLocation("minecraft:wooden_pickaxe"));
            modRegistry.remove(new ResourceLocation("minecraft:wooden_shovel"));
            modRegistry.remove(new ResourceLocation("minecraft:wooden_hoe"));
            modRegistry.remove(new ResourceLocation("minecraft:wooden_sword"));
            modRegistry.remove(new ResourceLocation("minecraft:wooden_axe"));
        }

        // Stone Tools
        if(Config.VanillaTweaks.STONE_TOOLS_DISABLE) {
            modRegistry.remove(new ResourceLocation("minecraft:stone_pickaxe"));
            modRegistry.remove(new ResourceLocation("minecraft:stone_shovel"));
            modRegistry.remove(new ResourceLocation("minecraft:stone_hoe"));
            modRegistry.remove(new ResourceLocation("minecraft:stone_sword"));
            modRegistry.remove(new ResourceLocation("minecraft:stone_axe"));
        }

        // Furnace
        if(Config.VanillaTweaks.ALTERNATE_FURNACE_RECIPE){
            modRegistry.remove(new ResourceLocation("minecraft:furnace"));
        }
    }
}
