package notreepunching.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.oredict.OreDictionary;
import notreepunching.NoTreePunching;

import java.util.ArrayList;
import java.util.List;

import static notreepunching.NoTreePunching.addCopperTools;

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

    public static ItemSaw ironSaw;
    public static ItemSaw diamondSaw;
    public static ItemSaw goldSaw;
    public static ItemSaw copperSaw;

    public static ItemMattock ironMattock;
    public static ItemMattock goldMattock;
    public static ItemMattock diamondMattock;
    public static ItemMattock copperMattock;

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
    }

    public static void registerItemModels(){

        for(int i=0;i<7;i++) {
            if(!NoTreePunching.replaceQuarkStones && (i == 4 || i == 5)) { continue; }
            if(!NoTreePunching.replaceRusticStone && (i == 6)) { continue; }
            NoTreePunching.proxy.registerItemModelWithVariant(rockStone, i, rockStone.name + "_" +  rockStone.getStoneName(new ItemStack(rockStone,1,i)),"inventory");
        }
        NoTreePunching.proxy.registerItemModel(grassFiber,0,grassFiber.name);
        NoTreePunching.proxy.registerItemModel(grassString,0,grassString.name);
        NoTreePunching.proxy.registerItemModel(flintShard,0,flintShard.name);

        NoTreePunching.proxy.registerItemModel(stoneKnife,0,stoneKnife.name);
        NoTreePunching.proxy.registerItemModel(ironKnife,0,ironKnife.name);
        NoTreePunching.proxy.registerItemModel(goldKnife,0,goldKnife.name);
        NoTreePunching.proxy.registerItemModel(diamondKnife,0,diamondKnife.name);

        NoTreePunching.proxy.registerItemModel(ironMattock,0,ironMattock.name);
        NoTreePunching.proxy.registerItemModel(goldMattock,0,goldMattock.name);
        NoTreePunching.proxy.registerItemModel(diamondMattock,0,diamondMattock.name);

        NoTreePunching.proxy.registerItemModel(ironSaw,0,ironSaw.name);
        NoTreePunching.proxy.registerItemModel(goldSaw,0,goldSaw.name);
        NoTreePunching.proxy.registerItemModel(diamondSaw,0,diamondSaw.name);

        NoTreePunching.proxy.registerItemModel(crudePick,0,crudePick.name);
        NoTreePunching.proxy.registerItemModel(crudeHatchet,0,crudeHatchet.name);
        NoTreePunching.proxy.registerItemModel(crudeShovel,0,crudeShovel.name);

        NoTreePunching.proxy.registerItemModel(firestarter,0,firestarter.name);

        if(addCopperTools){
            NoTreePunching.proxy.registerItemModel(copperKnife,0,copperKnife.name);
            NoTreePunching.proxy.registerItemModel(copperMattock,0,copperMattock.name);
            NoTreePunching.proxy.registerItemModel(copperSaw,0,copperSaw.name);
        }
    }

    public static List<ItemStack> listAllKnives(){
        List<ItemStack> array = new ArrayList<>();
        array.add(new ItemStack(stoneKnife));
        array.add(new ItemStack(ironKnife));
        array.add(new ItemStack(goldKnife));
        array.add(new ItemStack(diamondKnife));
        return array;
    }
    public static List<ItemStack> listAllMattocks(){
        List<ItemStack> array = new ArrayList<>();
        array.add(new ItemStack(ironMattock));
        array.add(new ItemStack(goldMattock));
        array.add(new ItemStack(diamondMattock));
        return array;
    }

    public static List<ItemStack> listAllSaws(){
        List<ItemStack> array = new ArrayList<>();
        array.add(new ItemStack(ironSaw));
        array.add(new ItemStack(goldSaw));
        array.add(new ItemStack(diamondSaw));
        return array;
    }

    public static void initOreDict(){
        OreDictionary.registerOre("toolSaw", new ItemStack(diamondSaw,1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("toolSaw", new ItemStack(ironSaw,1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("toolSaw", new ItemStack(goldSaw,1, OreDictionary.WILDCARD_VALUE));
        System.out.println(OreDictionary.getOres("toolSaw"));
    }
}

