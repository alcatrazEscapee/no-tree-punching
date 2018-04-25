package notreepunching.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.oredict.OreDictionary;
import notreepunching.NoTreePunching;

import java.util.ArrayList;
import java.util.List;

import static notreepunching.NoTreePunching.addBronzeTools;
import static notreepunching.NoTreePunching.addCopperTools;
import static notreepunching.NoTreePunching.addSteelTools;

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

    public static ItemFirestarter firestarter;

    public static void init(){
        rockStone = new ItemRock("rock");
        grassFiber = new ItemBase("grass_fiber");
        grassString = new ItemBase("grass_string");
        flintShard = new ItemBase("flint_shard");
        firestarter = new ItemFirestarter("firestarter");

        // TOOLS

        stoneKnife = new ItemKnife(NoTreePunching.toolMaterialFlint,"stone_knife");
        crudeHatchet = new ItemCrudeAxe(NoTreePunching.toolMaterialFlint,"crude_axe");
        crudePick = new ItemCrudePick(NoTreePunching.toolMaterialFlint,"crude_pick");
        crudeShovel = new ItemCrudeShovel(NoTreePunching.toolMaterialFlint, "crude_shovel");

        ironKnife = new ItemKnife(Item.ToolMaterial.IRON,"iron_knife");
        ironMattock = new ItemMattock(Item.ToolMaterial.IRON,"iron_mattock");
        ironSaw = new ItemSaw(Item.ToolMaterial.IRON,"iron_saw");

        goldKnife = new ItemKnife(Item.ToolMaterial.GOLD, "gold_knife");
        goldMattock = new ItemMattock(Item.ToolMaterial.GOLD,"gold_mattock");
        goldSaw = new ItemSaw(Item.ToolMaterial.GOLD,"gold_saw");

        diamondKnife = new ItemKnife(Item.ToolMaterial.DIAMOND,"diamond_knife");
        diamondMattock = new ItemMattock(Item.ToolMaterial.DIAMOND,"diamond_mattock");
        diamondSaw = new ItemSaw(Item.ToolMaterial.DIAMOND,"diamond_saw");

        addCopperTools = OreDictionary.doesOreNameExist("ingotCopper");
        if(addCopperTools){
            copperKnife = new ItemKnife(NoTreePunching.toolMaterialCopper,"copper_knife");
            copperSaw = new ItemSaw(NoTreePunching.toolMaterialCopper,"copper_saw");
            copperMattock = new ItemMattock(NoTreePunching.toolMaterialCopper,"copper_mattock");
        }

        addBronzeTools = OreDictionary.doesOreNameExist("ingotBronze");
        if(addBronzeTools){
            bronzeKnife = new ItemKnife(NoTreePunching.toolMaterialBronze,"bronze_knife");
            bronzeSaw = new ItemSaw(NoTreePunching.toolMaterialBronze, "bronze_saw");
            bronzeMattock = new ItemMattock(NoTreePunching.toolMaterialBronze, "bronze_mattock");
        }
        addSteelTools = OreDictionary.doesOreNameExist("ingotSteel");
        if(addSteelTools){
            steelKnife = new ItemKnife(NoTreePunching.toolMaterialSteel, "steel_knife");
            steelSaw = new ItemSaw(NoTreePunching.toolMaterialSteel, "steel_saw");
            steelMattock = new ItemMattock(NoTreePunching.toolMaterialSteel, "steel_mattock");
        }

    }

    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                rockStone,
                grassFiber,
                grassString,
                flintShard,
                stoneKnife,
                ironKnife,
                goldKnife,
                diamondKnife,
                ironSaw,
                goldSaw,
                diamondSaw,
                ironMattock,
                goldMattock,
                diamondMattock,
                crudeHatchet,
                crudePick,
                crudeShovel,
                firestarter
        );
        if(addCopperTools){
            event.getRegistry().registerAll(
                    copperKnife,
                    copperMattock,
                    copperSaw
            );
        }
        if(addBronzeTools) {
            event.getRegistry().registerAll(
                    bronzeKnife,
                    bronzeSaw,
                    bronzeMattock
            );
        }
        if(addSteelTools) {
            event.getRegistry().registerAll(
                    steelKnife,
                    steelMattock,
                    steelSaw
            );
        }
    }

    public static void registerItemModels(){

        for(int i=0;i<7;i++) {
            if(!NoTreePunching.replaceQuarkStones && (i == 4 || i == 5)) { continue; }
            if(!NoTreePunching.replaceRusticStone && (i == 6)) { continue; }
            NoTreePunching.proxy.registerItemModelWithVariant(rockStone, i, rockStone.name + "_" +  rockStone.getStoneName(new ItemStack(rockStone,1,i)),"inventory");
        }
        NoTreePunching.proxy.registerItemModel(grassFiber);
        NoTreePunching.proxy.registerItemModel(grassString);
        NoTreePunching.proxy.registerItemModel(flintShard);

        NoTreePunching.proxy.registerItemModel(stoneKnife);
        NoTreePunching.proxy.registerItemModel(ironKnife);
        NoTreePunching.proxy.registerItemModel(goldKnife);
        NoTreePunching.proxy.registerItemModel(diamondKnife);

        NoTreePunching.proxy.registerItemModel(ironMattock);
        NoTreePunching.proxy.registerItemModel(goldMattock);
        NoTreePunching.proxy.registerItemModel(diamondMattock);

        NoTreePunching.proxy.registerItemModel(ironSaw);
        NoTreePunching.proxy.registerItemModel(goldSaw);
        NoTreePunching.proxy.registerItemModel(diamondSaw);

        NoTreePunching.proxy.registerItemModel(crudePick);
        NoTreePunching.proxy.registerItemModel(crudeHatchet);
        NoTreePunching.proxy.registerItemModel(crudeShovel);

        NoTreePunching.proxy.registerItemModel(firestarter);

        if(addCopperTools){
            NoTreePunching.proxy.registerItemModel(copperKnife);
            NoTreePunching.proxy.registerItemModel(copperMattock);
            NoTreePunching.proxy.registerItemModel(copperSaw);
        }

        if(addBronzeTools) {
            NoTreePunching.proxy.registerItemModel(bronzeKnife);
            NoTreePunching.proxy.registerItemModel(bronzeSaw);
            NoTreePunching.proxy.registerItemModel(bronzeMattock);
        }
        if(addSteelTools){
            NoTreePunching.proxy.registerItemModel(steelKnife);
            NoTreePunching.proxy.registerItemModel(steelMattock);
            NoTreePunching.proxy.registerItemModel(steelSaw);
        }
    }

    public static List<ItemStack> listAllKnives(){
        List<ItemStack> array = new ArrayList<>();
        array.add(new ItemStack(stoneKnife));
        array.add(new ItemStack(ironKnife));
        array.add(new ItemStack(goldKnife));
        array.add(new ItemStack(diamondKnife));
        if(addCopperTools) { array.add(new ItemStack(copperKnife)); }
        if(addBronzeTools) { array.add(new ItemStack(bronzeKnife)); }
        if(addSteelTools) { array.add(new ItemStack(steelKnife)); }
        return array;
    }
    public static List<ItemStack> listAllMattocks(){
        List<ItemStack> array = new ArrayList<>();
        array.add(new ItemStack(ironMattock));
        array.add(new ItemStack(goldMattock));
        array.add(new ItemStack(diamondMattock));
        if(addCopperTools) { array.add(new ItemStack(copperMattock)); }
        if(addBronzeTools) { array.add(new ItemStack(bronzeMattock)); }
        if(addSteelTools) { array.add(new ItemStack(steelMattock)); }
        return array;
    }

    public static List<ItemStack> listAllSaws(){
        List<ItemStack> array = new ArrayList<>();
        array.add(new ItemStack(ironSaw));
        array.add(new ItemStack(goldSaw));
        array.add(new ItemStack(diamondSaw));
        if(addCopperTools) { array.add(new ItemStack(copperSaw)); }
        if(addBronzeTools) { array.add(new ItemStack(bronzeSaw)); }
        if(addSteelTools) { array.add(new ItemStack(steelSaw)); }
        return array;
    }

    public static void initOreDict(){
        OreDictionary.registerOre("toolSaw", new ItemStack(diamondSaw,1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("toolSaw", new ItemStack(ironSaw,1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("toolSaw", new ItemStack(goldSaw,1, OreDictionary.WILDCARD_VALUE));
        if(addCopperTools) { OreDictionary.registerOre("toolSaw",new ItemStack(copperSaw,1,OreDictionary.WILDCARD_VALUE)); }
        if(addBronzeTools) { OreDictionary.registerOre("toolSaw",new ItemStack(bronzeSaw,1,OreDictionary.WILDCARD_VALUE)); }
        if(addSteelTools) { OreDictionary.registerOre("toolSaw",new ItemStack(steelSaw,1,OreDictionary.WILDCARD_VALUE)); }
    }
}

