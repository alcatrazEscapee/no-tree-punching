package notreepunching.recipe;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryModifiable;
import notreepunching.NoTreePunching;
import notreepunching.block.ModBlocks;
import notreepunching.config.Config;
import notreepunching.item.ModItems;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ModRecipes {

    public static int magic = OreDictionary.WILDCARD_VALUE;

    public static List<CuttingRecipe> CUTTING_RECIPES = new ArrayList<CuttingRecipe>();
    public static List<FirepitRecipe> FIREPIT_RECIPES = new ArrayList<FirepitRecipe>();

    public static void init(){

        initCuttingRecipes();
        initFirepitRecipes();
        initCraftingRecipes();
        initSmeltingRecipes();

    }

    // *************************** KNIFE ************************ //

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

    private static void addCuttingRecipe(ItemStack inputStack, ItemStack... outputs){
        CUTTING_RECIPES.add(new CuttingRecipe(inputStack,outputs));
    }
    private static void addCuttingRecipe(Item input, ItemStack... outputs){
        addCuttingRecipe(new ItemStack(input,1,0),outputs);
    }
    private static void addCuttingRecipe(Block input, ItemStack... outputs){
        addCuttingRecipe(new ItemStack(input,1,0),outputs);
    }

    private static void initCuttingRecipes(){
        for(int i=0;i<7;i++) {
            if(!NoTreePunching.replaceQuarkStones && (i == 4 || i == 5)) { continue; }
            if(!NoTreePunching.replaceRusticStone && (i == 6)) { continue; }
            addCuttingRecipe(new ItemStack(ModItems.rockStone, 1, i), new ItemStack(ModItems.flintShard, 1));
        }
        addCuttingRecipe(Items.FLINT, new ItemStack(ModItems.flintShard,2));

        addCuttingRecipe(Blocks.WOOL,new ItemStack(Items.STRING,4));
        addCuttingRecipe(Items.REEDS,new ItemStack(ModItems.grassFiber,2));
        addCuttingRecipe(Items.WHEAT,new ItemStack(ModItems.grassFiber,1), new ItemStack(Items.WHEAT_SEEDS));
        addCuttingRecipe(Blocks.SAPLING,new ItemStack(Items.STICK),new ItemStack(ModItems.grassFiber));

        addCuttingRecipe(Items.LEATHER_BOOTS,new ItemStack(Items.LEATHER,2));
        addCuttingRecipe(Items.LEATHER_CHESTPLATE,new ItemStack(Items.LEATHER,5));
        addCuttingRecipe(Items.LEATHER_LEGGINGS,new ItemStack(Items.LEATHER,4));
        addCuttingRecipe(Items.LEATHER_HELMET,new ItemStack(Items.LEATHER,3));

        addCuttingRecipe(Blocks.MELON_BLOCK,new ItemStack(Items.MELON,9));
        addCuttingRecipe(Items.MELON,new ItemStack(Items.MELON_SEEDS,1),new ItemStack(ModItems.grassFiber));
        addCuttingRecipe(Blocks.PUMPKIN,new ItemStack(Items.PUMPKIN_SEEDS,4),new ItemStack(ModItems.grassFiber,2));

        for(int i = 0; i<6; i++){
            addCuttingRecipe(new ItemStack(i<4 ? Blocks.LEAVES : Blocks.LEAVES2,6,i%4),new ItemStack(Blocks.SAPLING,1,i),new ItemStack(ModItems.grassFiber,2));
        }

    }

    // *************************** FIREPIT ************************ //

    public static boolean isFirepitRecipe(ItemStack stack) { return getFirepitRecipe(stack) != null; }

    public static FirepitRecipe getFirepitRecipe(ItemStack stack){
        for(int i=0;i<FIREPIT_RECIPES.size();i++){
            ItemStack is = FIREPIT_RECIPES.get(i).getInput();
            if(is.getItem().getUnlocalizedName().equals(stack.getItem().getUnlocalizedName()) && is.getMetadata() == stack.getMetadata() && stack.getCount()>=is.getCount()){
                return FIREPIT_RECIPES.get(i);
            }
        }
        return null;
    }
    private static void addFirepitRecipe(ItemStack input, int cookTime, ItemStack output){
        FIREPIT_RECIPES.add(new FirepitRecipe(input,cookTime,output));
    }
    private static void addFirepitRecipe(ItemStack input, ItemStack output){
        addFirepitRecipe(input,Config.Firepit.COOK_MULT,output);
    }

    private static void initFirepitRecipes(){
        addFirepitRecipe(new ItemStack(Items.STICK,1),40,new ItemStack(Blocks.TORCH,2));

        Map<ItemStack, ItemStack>  map = FurnaceRecipes.instance().getSmeltingList();

        for(Map.Entry<ItemStack, ItemStack> m : map.entrySet()){
            if(m.getValue().getItem() instanceof ItemFood){
                int meta1 = m.getKey().getMetadata()==magic ? 0 : m.getKey().getMetadata();
                int meta2 = m.getValue().getMetadata()==magic ? 0 : m.getValue().getMetadata();

                addFirepitRecipe(new ItemStack(m.getKey().getItem(),1,meta1),new ItemStack(m.getValue().getItem(),1,meta2));
            }
        }
    }

    // *************************** SMELTING ************************ //

    private static void initSmeltingRecipes(){
        RecipeHelper.addSmelting(ModItems.grassString,new ItemStack(Items.STRING));

        if(Config.VanillaTweaks.STONE_DROPS_ROCKS){
            RecipeHelper.addSmelting(ModBlocks.andesiteCobble,new ItemStack(Blocks.STONE,1,5));
            RecipeHelper.addSmelting(ModBlocks.dioriteCobble,new ItemStack(Blocks.STONE,1,3));
            RecipeHelper.addSmelting(ModBlocks.graniteCobble,new ItemStack(Blocks.STONE,1,1));
        }
        if(NoTreePunching.replaceQuarkStones){
            RecipeHelper.addSmelting(ModBlocks.marbleCobble,new ItemStack(Item.getByNameOrId("quark:marble"),1,0));
            RecipeHelper.addSmelting(ModBlocks.limestoneCobble,new ItemStack(Item.getByNameOrId("quark:limestone"),1,0));
        }
        if(NoTreePunching.replaceRusticStone){
            RecipeHelper.addSmelting(ModBlocks.slateCobble,new ItemStack(Item.getByNameOrId("rustic:slate"),1,0));
        }
    }

    // *************************** CRAFTING ************************ //

    private static void initCraftingRecipes(){

        // Planks + sticks from crude axe + saw items
        for(int i=0; i<6;i++) {
            registerShaped(new ItemStack(Blocks.PLANKS, 2, i), "A","P",'P',new ItemStack(i<4 ? Blocks.LOG : Blocks.LOG2, 1, i%4), 'A', new ItemStack(ModItems.crudeHatchet,1,magic));
            registerShaped(new ItemStack(Blocks.PLANKS,4,i), "A","P",'P',new ItemStack(i<4 ? Blocks.LOG : Blocks.LOG2,1,i%4), 'A', "toolSaw");
        }
        registerShaped(new ItemStack(Items.STICK,1), "A", "P", 'P', "plankWood",'A',new ItemStack(ModItems.crudeHatchet,1,magic));
        registerShaped(new ItemStack(Items.STICK,2),"A", "P", 'P', "plankWood", 'A', "toolSaw");
        registerShaped(new ItemStack(Items.STICK,4), "AP",'P', "logWood", 'A', new ItemStack(ModItems.crudeHatchet,1,magic));
        registerShaped(new ItemStack(Items.STICK,8), "AP", 'P', "logWood", 'A', "toolSaw");

        // Cobblestone recipes
        if(NoTreePunching.replaceQuarkStones){
            registerShaped(new ItemStack(ModBlocks.marbleCobble),"SS","SS",'S',new ItemStack(ModItems.rockStone,1,4));
            registerShaped(new ItemStack(ModBlocks.limestoneCobble),"SS","SS",'S',new ItemStack(ModItems.rockStone,1,5));
        }
        if(NoTreePunching.replaceRusticStone){
            registerShaped(new ItemStack(ModBlocks.slateCobble),"SS","SS",'S', new ItemStack(ModItems.rockStone,1,6));
        }

        // Tool Types
        if(NoTreePunching.addCopperTools){
            registerShaped(new ItemStack(ModItems.copperKnife),"I","H",'I',"ingotCopper",'H',"stickWood");
            registerShaped(new ItemStack(ModItems.copperMattock),"III","IH "," H ",'I',"ingotCopper",'H',"stickWood");
            registerShaped(new ItemStack(ModItems.copperSaw)," HI","HI ","H  ",'I',"ingotCopper",'H',"stickWood");
        }

    }

    private static void registerShaped(ItemStack output, Object... inputs) {
        RecipeHelper.addShapedOreRecipe(output, inputs);
    }

    private static void registerShapeless(ItemStack output, Object... inputs) {
        RecipeHelper.addShapelessOreRecipe(output, inputs);
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

        if(Config.VanillaTweaks.WOOD_RECIPE_DISABLE){
            modRegistry.remove(new ResourceLocation("minecraft:oak_planks"));
            modRegistry.remove(new ResourceLocation("minecraft:spruce_planks"));
            modRegistry.remove(new ResourceLocation("minecraft:birch_planks"));
            modRegistry.remove(new ResourceLocation("minecraft:jungle_planks"));
            modRegistry.remove(new ResourceLocation("minecraft:acacia_planks"));
            modRegistry.remove(new ResourceLocation("minecraft:dark_oak_planks"));
            modRegistry.remove(new ResourceLocation("minecraft:stick"));

        }
    }
}
