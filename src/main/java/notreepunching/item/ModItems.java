package notreepunching.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.oredict.OreDictionary;
import notreepunching.NoTreePunching;
import notreepunching.recipe.ModRecipes;
import org.apache.logging.log4j.core.config.Order;
import scala.Array;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;
import java.util.List;

public class ModItems {

    // Declare Instances of all items

    public static ItemRock rockStone = new ItemRock("rock");
    public static ItemBase grassFiber = new ItemBase("grass_fiber");
    public static ItemBase grassString = new ItemBase("grass_string");
    public static ItemBase flintShard = new ItemBase("flint_shard");

    public static ItemKnife stoneKnife = new ItemKnife(NoTreePunching.toolMaterialFlint,"stone_knife");
    public static ItemKnife ironKnife = new ItemKnife(Item.ToolMaterial.IRON,"iron_knife");
    public static ItemKnife goldKnife = new ItemKnife(Item.ToolMaterial.GOLD,"gold_knife");
    public static ItemKnife diamondKnife = new ItemKnife(Item.ToolMaterial.DIAMOND,"diamond_knife");

    public static ItemSaw ironSaw = new ItemSaw(Item.ToolMaterial.IRON,"iron_saw");
    public static ItemSaw diamondSaw = new ItemSaw(Item.ToolMaterial.DIAMOND,"diamond_saw");
    public static ItemSaw goldSaw = new ItemSaw(Item.ToolMaterial.GOLD,"gold_saw");

    public static ItemMattock ironMattock = new ItemMattock(Item.ToolMaterial.IRON,"iron_mattock");
    public static ItemMattock goldMattock = new ItemMattock(Item.ToolMaterial.GOLD,"gold_mattock");
    public static ItemMattock diamondMattock = new ItemMattock(Item.ToolMaterial.DIAMOND,"diamond_mattock");

    public static ItemCrudeAxe crudeHatchet = new ItemCrudeAxe(NoTreePunching.toolMaterialFlint,"crude_axe");
    public static ItemCrudePick crudePick = new ItemCrudePick(NoTreePunching.toolMaterialFlint,"crude_pick");
    public static ItemCrudeShovel crudeShovel = new ItemCrudeShovel(NoTreePunching.toolMaterialFlint,"crude_shovel");

    public static ItemFirestarter firestarter = new ItemFirestarter("firestarter");

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

