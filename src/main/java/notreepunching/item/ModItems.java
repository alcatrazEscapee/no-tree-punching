package notreepunching.item;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import notreepunching.NoTreePunching;
import notreepunching.block.ModBlocks;
import notreepunching.registry.RegistryHandler;

import java.util.ArrayList;
import java.util.List;

import static notreepunching.NoTreePunching.addSteelTools;
import static notreepunching.NoTreePunching.replaceQuarkStones;
import static notreepunching.NoTreePunching.replaceRusticStone;

public class ModItems {

    // Declare Instances of all items

    public static ItemRock rockStone;
    public static ItemBase grassFiber;
    public static ItemBase grassString;
    public static ItemBase flintShard;

    public static ItemKnife stoneKnife;
    public static ItemKnife ironKnife;
    public static ItemKnife goldKnife;
    public static ItemKnife diamondKnife;
    public static ItemKnife copperKnife;
    public static ItemKnife bronzeKnife;
    public static ItemKnife steelKnife;

    public static ItemSaw ironSaw;
    public static ItemSaw diamondSaw;
    public static ItemSaw goldSaw;
    public static ItemSaw copperSaw;
    public static ItemSaw bronzeSaw;
    public static ItemSaw steelSaw;

    public static ItemMattock ironMattock;
    public static ItemMattock goldMattock;
    public static ItemMattock diamondMattock;
    public static ItemMattock copperMattock;
    public static ItemMattock bronzeMattock;
    public static ItemMattock steelMattock;

    public static ItemCrudeAxe crudeHatchet;
    public static ItemCrudePick crudePick;
    public static ItemCrudeShovel crudeShovel;
    public static ItemCrudeHoe crudeHoe;

    public static ItemFirestarter firestarter;

    public static ItemBase ingotTin;
    public static ItemBase ingotCopper;
    public static ItemBase ingotBronze;
    public static ItemBase dustTin;
    public static ItemBase dustCopper;
    public static ItemBase dustBronze;
    public static ItemBase dustIron;

    public static void init(){
        rockStone = new ItemRock("rock");
        grassFiber = new ItemBase("grass_fiber");
        grassString = new ItemBase("grass_string");
        flintShard = new ItemBase("flint_shard");

        firestarter = new ItemFirestarter("firestarter");

        ingotBronze = new ItemBase("ingot_bronze");
        ingotTin = new ItemBase("ingot_tin");
        ingotCopper = new ItemBase("ingot_copper");
        dustBronze = new ItemBase("dust_bronze");
        dustCopper = new ItemBase("dust_copper");
        dustTin = new ItemBase("dust_tin");
        dustIron = new ItemBase("dust_iron");

        // TOOLS

        stoneKnife = new ItemKnife(NoTreePunching.toolMaterialFlint,"stone_knife");
        crudeHatchet = new ItemCrudeAxe(NoTreePunching.toolMaterialFlint,"crude_axe");
        crudePick = new ItemCrudePick(NoTreePunching.toolMaterialFlint,"crude_pick");
        crudeShovel = new ItemCrudeShovel(NoTreePunching.toolMaterialFlint, "crude_shovel");
        crudeHoe = new ItemCrudeHoe(NoTreePunching.toolMaterialFlint, "crude_hoe");

        ironKnife = new ItemKnife(Item.ToolMaterial.IRON,"iron_knife");
        ironMattock = new ItemMattock(Item.ToolMaterial.IRON,"iron_mattock");
        ironSaw = new ItemSaw(Item.ToolMaterial.IRON,"iron_saw");

        goldKnife = new ItemKnife(Item.ToolMaterial.GOLD, "gold_knife");
        goldMattock = new ItemMattock(Item.ToolMaterial.GOLD,"gold_mattock");
        goldSaw = new ItemSaw(Item.ToolMaterial.GOLD,"gold_saw");

        diamondKnife = new ItemKnife(Item.ToolMaterial.DIAMOND,"diamond_knife");
        diamondMattock = new ItemMattock(Item.ToolMaterial.DIAMOND,"diamond_mattock");
        diamondSaw = new ItemSaw(Item.ToolMaterial.DIAMOND,"diamond_saw");

        copperKnife = new ItemKnife(NoTreePunching.toolMaterialCopper,"copper_knife");
        copperSaw = new ItemSaw(NoTreePunching.toolMaterialCopper,"copper_saw");
        copperMattock = new ItemMattock(NoTreePunching.toolMaterialCopper,"copper_mattock");

        bronzeKnife = new ItemKnife(NoTreePunching.toolMaterialBronze,"bronze_knife");
        bronzeSaw = new ItemSaw(NoTreePunching.toolMaterialBronze, "bronze_saw");
        bronzeMattock = new ItemMattock(NoTreePunching.toolMaterialBronze, "bronze_mattock");

        addSteelTools = OreDictionary.doesOreNameExist("ingotSteel");
        if(addSteelTools){
            steelKnife = new ItemKnife(NoTreePunching.toolMaterialSteel, "steel_knife");
            steelSaw = new ItemSaw(NoTreePunching.toolMaterialSteel, "steel_saw");
            steelMattock = new ItemMattock(NoTreePunching.toolMaterialSteel, "steel_mattock");
        }

    }

    public static void addItemToRegistry(Item item, String name, boolean addToCreativeTab){
        item.setUnlocalizedName(name);
        item.setRegistryName(name);

        if(addToCreativeTab) { item.setCreativeTab(NoTreePunching.NTP_Tab); }

        RegistryHandler.ITEM_REGISTRY.add(item);
    }

    public static List<ItemStack> listAllKnives(){
        List<ItemStack> array = new ArrayList<>();
        array.add(new ItemStack(stoneKnife));
        array.add(new ItemStack(ironKnife));
        array.add(new ItemStack(goldKnife));
        array.add(new ItemStack(diamondKnife));
        array.add(new ItemStack(copperKnife));
        array.add(new ItemStack(bronzeKnife));
        if(addSteelTools) { array.add(new ItemStack(steelKnife)); }
        return array;
    }
    public static List<ItemStack> listAllMattocks(){
        List<ItemStack> array = new ArrayList<>();
        array.add(new ItemStack(ironMattock));
        array.add(new ItemStack(goldMattock));
        array.add(new ItemStack(diamondMattock));
        array.add(new ItemStack(copperMattock));
        array.add(new ItemStack(bronzeMattock));
        if(addSteelTools) { array.add(new ItemStack(steelMattock)); }
        return array;
    }

    public static List<ItemStack> listAllSaws(){
        List<ItemStack> array = new ArrayList<>();
        array.add(new ItemStack(ironSaw));
        array.add(new ItemStack(goldSaw));
        array.add(new ItemStack(diamondSaw));
        array.add(new ItemStack(copperSaw));
        array.add(new ItemStack(bronzeSaw));
        if(addSteelTools) { array.add(new ItemStack(steelSaw)); }
        return array;
    }

    public static void initOreDict(){
        OreDictionary.registerOre("toolSaw", new ItemStack(diamondSaw,1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("toolSaw", new ItemStack(ironSaw,1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("toolSaw", new ItemStack(goldSaw,1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("toolSaw",new ItemStack(copperSaw,1,OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("toolSaw",new ItemStack(bronzeSaw,1,OreDictionary.WILDCARD_VALUE));
        if(addSteelTools) { OreDictionary.registerOre("toolSaw",new ItemStack(steelSaw,1,OreDictionary.WILDCARD_VALUE)); }

        OreDictionary.registerOre("string", new ItemStack(Items.STRING));
        OreDictionary.registerOre("string", new ItemStack(ModItems.grassString));

        OreDictionary.registerOre("cobblestone", new ItemStack(ModBlocks.andesiteCobble));
        OreDictionary.registerOre("cobblestone", new ItemStack(ModBlocks.graniteCobble));
        OreDictionary.registerOre("cobblestone", new ItemStack(ModBlocks.dioriteCobble));
        if(replaceQuarkStones) {
            OreDictionary.registerOre("cobblestone", new ItemStack(ModBlocks.marbleCobble));
            OreDictionary.registerOre("cobblestone", new ItemStack(ModBlocks.limestoneCobble));
        }
        if(replaceRusticStone) {
            OreDictionary.registerOre("cobblestone", new ItemStack(ModBlocks.slateCobble));
        }

        OreDictionary.registerOre("ingotCopper", new ItemStack(ingotCopper));
        OreDictionary.registerOre("ingotTin", new ItemStack(ingotTin));
        OreDictionary.registerOre("ingotBronze", new ItemStack(ingotBronze));
        OreDictionary.registerOre("dustIron", new ItemStack(dustIron));
        OreDictionary.registerOre("dustCopper", new ItemStack(dustCopper));
        OreDictionary.registerOre("dustTin", new ItemStack(dustTin));
        OreDictionary.registerOre("dustBronze", new ItemStack(dustBronze));

        OreDictionary.registerOre("oreCopper", new ItemStack(ModBlocks.oreCopper));
        OreDictionary.registerOre("oreTin", new ItemStack(ModBlocks.oreTin));
    }
}

