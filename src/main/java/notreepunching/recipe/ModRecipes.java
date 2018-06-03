package notreepunching.recipe;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryModifiable;
import notreepunching.NoTreePunching;
import notreepunching.block.ModBlocks;
import notreepunching.config.ModConfig;
import notreepunching.item.ModItems;
import notreepunching.recipe.forge.BlastRecipeHandler;
import notreepunching.recipe.grindstone.GrindstoneRecipeHandler;
import notreepunching.recipe.knife.KnifeRecipeHandler;
import notreepunching.recipe.firepit.FirepitRecipeHandler;
import notreepunching.recipe.forge.ForgeRecipeHandler;
import notreepunching.util.ItemUtil;
import notreepunching.util.MiscUtil;
import notreepunching.util.RecipeUtil;

import java.util.Iterator;
import java.util.Map;

public class ModRecipes {

    private static int magic = OreDictionary.WILDCARD_VALUE;

    public static void init(){
        FirepitRecipeHandler.init();
        KnifeRecipeHandler.init();
        ForgeRecipeHandler.init();
        GrindstoneRecipeHandler.init();
        BlastRecipeHandler.init();

        initCraftingRecipes();
        initSmeltingRecipes();
    }

    public static void postInit(){
        FirepitRecipeHandler.postInit();
        ForgeRecipeHandler.postInit();
        KnifeRecipeHandler.postInit();
        GrindstoneRecipeHandler.postInit();
        BlastRecipeHandler.postInit();

        postInitSmeltingRecipes();
        postInitCraftingRecipes();
    }

    // *************************** SMELTING ************************ //

    private static void initSmeltingRecipes(){
        if(ModConfig.VanillaTweaks.STONE_DROPS_ROCKS){
            RecipeUtil.addSmelting(ModBlocks.andesiteCobble,new ItemStack(Blocks.STONE,1,5));
            RecipeUtil.addSmelting(ModBlocks.dioriteCobble,new ItemStack(Blocks.STONE,1,3));
            RecipeUtil.addSmelting(ModBlocks.graniteCobble,new ItemStack(Blocks.STONE,1,1));
        }

        if(!ModConfig.VanillaTweaks.DISABLE_SMELTING_ORE){
            RecipeUtil.addSmelting(ModBlocks.oreCopper,new ItemStack(ModItems.ingotCopper));
            RecipeUtil.addSmelting(ModBlocks.oreTin,new ItemStack(ModItems.ingotTin));

            RecipeUtil.addSmelting(ModItems.dustTin,new ItemStack(ModItems.ingotTin));
            RecipeUtil.addSmelting(ModItems.dustCopper,new ItemStack(ModItems.ingotCopper));
            RecipeUtil.addSmelting(ModItems.dustSteel,new ItemStack(ModItems.ingotSteel));
            RecipeUtil.addSmelting(ModItems.dustIron,new ItemStack(Items.IRON_INGOT));
            RecipeUtil.addSmelting(ModItems.dustBronze,new ItemStack(ModItems.ingotBronze));
        }
    }

    private static void postInitSmeltingRecipes(){

        if(NoTreePunching.replaceQuarkStones){
            RecipeUtil.addSmelting(ModBlocks.marbleCobble, ItemUtil.getSafeItem("quark:marble",0,1));
            RecipeUtil.addSmelting(ModBlocks.limestoneCobble,ItemUtil.getSafeItem("quark:limestone",0,1));
        }
        if(NoTreePunching.replaceRusticStone){
            RecipeUtil.addSmelting(ModBlocks.slateCobble,ItemUtil.getSafeItem("rustic:slate",0,1));
        }

        if(ModConfig.VanillaTweaks.DISABLE_SMELTING_ORE) {
            Map<ItemStack, ItemStack> recipes = FurnaceRecipes.instance().getSmeltingList();
            Iterator<ItemStack> iterator = recipes.keySet().iterator();
            while (iterator.hasNext()) {
                ItemStack stack1 = iterator.next();
                ItemStack stack = recipes.get(stack1);
                if(MiscUtil.doesStackMatchOrePrefix(stack,"ingot")){
                    iterator.remove();
                }
                else if(ItemUtil.areStacksEqual(stack1, new ItemStack(Items.COAL, 1, 1))){
                    iterator.remove();
                }
            }
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
        if(ModConfig.VanillaTweaks.STONE_DROPS_ROCKS){
            registerShaped(new ItemStack(ModBlocks.andesiteCobble),"SS","SS",'S', new ItemStack(ModItems.rockStone,1,1));
            registerShaped(new ItemStack(ModBlocks.dioriteCobble),"SS","SS",'S', new ItemStack(ModItems.rockStone,1,2));
            registerShaped(new ItemStack(ModBlocks.graniteCobble),"SS","SS",'S', new ItemStack(ModItems.rockStone,1,3));
        }
        if(NoTreePunching.replaceQuarkStones){
            registerShaped(new ItemStack(ModBlocks.marbleCobble),"SS","SS",'S',new ItemStack(ModItems.rockStone,1,4));
            registerShaped(new ItemStack(ModBlocks.limestoneCobble),"SS","SS",'S',new ItemStack(ModItems.rockStone,1,5));
        }
        if(NoTreePunching.replaceRusticStone){
            registerShaped(new ItemStack(ModBlocks.slateCobble),"SS","SS",'S', new ItemStack(ModItems.rockStone,1,6));
        }

        if(!ModConfig.VanillaTweaks.GOLD_TOOLS_DISABLE){
            registerShaped(new ItemStack(ModItems.goldMattock),"III","IS "," S ",'I',"ingotGold",'S',"stickWood");
            registerShaped(new ItemStack(ModItems.goldSaw)," SI","SI ","S  ",'I',"ingotGold",'S',"stickWood");
            registerShaped(new ItemStack(ModItems.goldKnife),"I","S",'I',"ingotGold",'S',"stickWood");
        }

        // Cooler recipes
        if(ModConfig.VanillaTweaks.COOLER_RECIPES){
            registerShaped(new ItemStack(Blocks.LEVER), "RGS",'S',"stickWood",'G',"gearWood",'R',"cobblestone");
            registerShaped(new ItemStack(Blocks.STONE_BUTTON),"GS",'G',"gearWood",'S',"stone");
            registerShaped(new ItemStack(Blocks.WOODEN_BUTTON), "GS",'G',"gearWood",'S',"plankWood");
            registerShaped(new ItemStack(Blocks.WOODEN_PRESSURE_PLATE), "SGS",'G',"gearWood",'S',"plankWood");
            registerShaped(new ItemStack(Blocks.STONE_PRESSURE_PLATE), "SGS",'G',"gearWood",'S',"cobblestone");

            registerShapeless(new ItemStack(Items.FLINT_AND_STEEL), new ItemStack(Items.FLINT), "ingotSteel");
            registerShaped(new ItemStack(Blocks.ANVIL), "BBB"," I ","III",'B',"blockSteel",'I',"ingotSteel");
        }

    }

    private static void postInitCraftingRecipes(){

        if(Loader.isModLoaded("rustic")){
            registerShaped(ItemUtil.getSafeItem("rustic:planks",2), "A","P",'P',ItemUtil.getSafeItem("rustic:log"), 'A', new ItemStack(ModItems.crudeHatchet,1,magic));
            registerShaped(ItemUtil.getSafeItem("rustic:planks",4), "A","P",'P',ItemUtil.getSafeItem("rustic:log"), 'A', "toolSaw");

            registerShaped(ItemUtil.getSafeItem("rustic:planks",1,2), "A","P",'P',ItemUtil.getSafeItem("rustic:log",1,1), 'A', new ItemStack(ModItems.crudeHatchet,1,magic));
            registerShaped(ItemUtil.getSafeItem("rustic:planks",1,4), "A","P",'P',ItemUtil.getSafeItem("rustic:log",1,1), 'A', "toolSaw");
        }
        if(Loader.isModLoaded("traverse")){
            registerShaped(ItemUtil.getSafeItem("traverse:fir_planks",2), "A","P",'P',ItemUtil.getSafeItem("traverse:fir_log"), 'A', new ItemStack(ModItems.crudeHatchet,1,magic));
            registerShaped(ItemUtil.getSafeItem("traverse:fir_planks",4), "A","P",'P',ItemUtil.getSafeItem("traverse:fir_log"), 'A', "toolSaw");
        }
        if(Loader.isModLoaded("biomesoplenty")){
            for(int i = 0;i<16;i++){
                // I'm proud of these two lines :)
                String logName = "biomesoplenty:log_"+i/4;
                registerShaped(ItemUtil.getSafeItem("biomesoplenty:planks_0",i,2),"A","P",'P',ItemUtil.getSafeItem(logName,4+i%4,1),'A', new ItemStack(ModItems.crudeHatchet,1,magic));
                registerShaped(ItemUtil.getSafeItem("biomesoplenty:planks_0",i,4),"A","P",'P',ItemUtil.getSafeItem(logName,4+i%4,1),'A',"toolSaw");
            }
        }
    }

    private static void registerShaped(ItemStack output, Object... inputs) {
        RecipeUtil.addShapedOreRecipe(output, inputs);
    }
    private static void registerShapeless(ItemStack output, Object... inputs) {
        RecipeUtil.addShapelessOreRecipe(output, inputs);
    }

    public static void removeVanillaRecipes(IForgeRegistry<IRecipe> reg){
        IForgeRegistryModifiable modRegistry = (IForgeRegistryModifiable) reg;

        // Wooden Tools
        if(ModConfig.VanillaTweaks.WOOD_TOOLS_DISABLE) {
            modRegistry.remove(new ResourceLocation("minecraft:wooden_pickaxe"));
            modRegistry.remove(new ResourceLocation("minecraft:wooden_shovel"));
            modRegistry.remove(new ResourceLocation("minecraft:wooden_hoe"));
            modRegistry.remove(new ResourceLocation("minecraft:wooden_sword"));
            modRegistry.remove(new ResourceLocation("minecraft:wooden_axe"));
        }

        // Stone Tools
        if(ModConfig.VanillaTweaks.STONE_TOOLS_DISABLE) {
            modRegistry.remove(new ResourceLocation("minecraft:stone_pickaxe"));
            modRegistry.remove(new ResourceLocation("minecraft:stone_shovel"));
            modRegistry.remove(new ResourceLocation("minecraft:stone_hoe"));
            modRegistry.remove(new ResourceLocation("minecraft:stone_sword"));
            modRegistry.remove(new ResourceLocation("minecraft:stone_axe"));
        }

        // Gold Tools
        if(ModConfig.VanillaTweaks.GOLD_TOOLS_DISABLE) {
            modRegistry.remove(new ResourceLocation("minecraft:golden_pickaxe"));
            modRegistry.remove(new ResourceLocation("minecraft:golden_shovel"));
            modRegistry.remove(new ResourceLocation("minecraft:golden_hoe"));
            modRegistry.remove(new ResourceLocation("minecraft:golden_sword"));
            modRegistry.remove(new ResourceLocation("minecraft:golden_axe"));

            modRegistry.remove(new ResourceLocation("minecraft:golden_helmet"));
            modRegistry.remove(new ResourceLocation("minecraft:golden_chestplate"));
            modRegistry.remove(new ResourceLocation("minecraft:golden_leggings"));
            modRegistry.remove(new ResourceLocation("minecraft:golden_boots"));
        }

        if(ModConfig.VanillaTweaks.WOOD_RECIPE_DISABLE) {
            modRegistry.remove(new ResourceLocation("minecraft:oak_planks"));
            modRegistry.remove(new ResourceLocation("minecraft:spruce_planks"));
            modRegistry.remove(new ResourceLocation("minecraft:birch_planks"));
            modRegistry.remove(new ResourceLocation("minecraft:jungle_planks"));
            modRegistry.remove(new ResourceLocation("minecraft:acacia_planks"));
            modRegistry.remove(new ResourceLocation("minecraft:dark_oak_planks"));
            modRegistry.remove(new ResourceLocation("minecraft:stick"));

            // Mod Wood Recipes
            if (Loader.isModLoaded("traverse")) {
                modRegistry.remove(new ResourceLocation("traverse:fir_planks"));
            }
            if (Loader.isModLoaded("rustic")) {
                modRegistry.remove(new ResourceLocation("rustic:olive_planks"));
                modRegistry.remove(new ResourceLocation("rustic:ironwood_planks"));
            }
            if (Loader.isModLoaded("biomesoplenty")) {
                modRegistry.remove(new ResourceLocation("biomesoplenty:sacred_oak_planks"));
                modRegistry.remove(new ResourceLocation("biomesoplenty:cherry_planks"));
                modRegistry.remove(new ResourceLocation("biomesoplenty:umbran_planks"));
                modRegistry.remove(new ResourceLocation("biomesoplenty:fir_planks"));
                modRegistry.remove(new ResourceLocation("biomesoplenty:ethereal_planks"));
                modRegistry.remove(new ResourceLocation("biomesoplenty:magic_planks"));
                modRegistry.remove(new ResourceLocation("biomesoplenty:mangrove_planks"));
                modRegistry.remove(new ResourceLocation("biomesoplenty:palm_planks"));
                modRegistry.remove(new ResourceLocation("biomesoplenty:redwood_planks"));
                modRegistry.remove(new ResourceLocation("biomesoplenty:willow_planks"));
                modRegistry.remove(new ResourceLocation("biomesoplenty:pine_planks"));
                modRegistry.remove(new ResourceLocation("biomesoplenty:hellbark_planks"));
                modRegistry.remove(new ResourceLocation("biomesoplenty:jacaranda_planks"));
                modRegistry.remove(new ResourceLocation("biomesoplenty:mahogany_planks"));
                modRegistry.remove(new ResourceLocation("biomesoplenty:ebony_planks"));
                modRegistry.remove(new ResourceLocation("biomesoplenty:eucalyptus_planks"));
            }
        }

        if(ModConfig.VanillaTweaks.COOLER_RECIPES){
            modRegistry.remove(new ResourceLocation("minecraft:lever"));
            modRegistry.remove(new ResourceLocation("minecraft:wooden_button"));
            modRegistry.remove(new ResourceLocation("minecraft:stone_button"));
            modRegistry.remove(new ResourceLocation("minecraft:wooden_pressure_plate"));
            modRegistry.remove(new ResourceLocation("minecraft:stone_pressure_plate"));

            modRegistry.remove(new ResourceLocation("minecraft:flint_and_steel"));
            modRegistry.remove(new ResourceLocation("minecraft:anvil"));
        }
    }
}
