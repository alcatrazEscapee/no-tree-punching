package notreepunching.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import notreepunching.NoTreePunching;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;
import java.util.List;

public class ModItems {

    // Declare Instances of all items

    public static ItemRock rockStone = new ItemRock("rock");
    public static ItemBase grassFiber = new ItemBase("grass_fiber");
    public static ItemBase grassString = new ItemBase("grass_string");
    public static ItemBase flintShard = new ItemBase("flint_shard");

    public static ItemKnife stoneKnife = new ItemKnife(NoTreePunching.toolMaterialCrudeStone,"stone_knife");
    public static ItemKnife ironKnife = new ItemKnife(Item.ToolMaterial.IRON,"iron_knife");
    public static ItemKnife goldKnife = new ItemKnife(Item.ToolMaterial.GOLD,"gold_knife");
    public static ItemKnife diamondKnife = new ItemKnife(Item.ToolMaterial.DIAMOND,"diamond_knife");

    public static ItemMattock ironMattock = new ItemMattock(Item.ToolMaterial.IRON,"iron_mattock");
    public static ItemMattock goldMattock = new ItemMattock(Item.ToolMaterial.GOLD,"gold_mattock");
    public static ItemMattock diamondMattock = new ItemMattock(Item.ToolMaterial.DIAMOND,"diamond_mattock");

    public static ItemCrudeAxe crudeHatchet = new ItemCrudeAxe(NoTreePunching.toolMaterialFlint,"crude_axe");
    public static ItemCrudePick crudePick = new ItemCrudePick(NoTreePunching.toolMaterialFlint,"crude_pick");

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
                ironMattock,
                goldMattock,
                diamondMattock,
                diamondKnife,
                crudeHatchet,
                crudePick,
                firestarter
        );
    }

    public static void registerItemModels(){

        for(int i=0;i<7;i++) {
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

        NoTreePunching.proxy.registerItemModel(crudePick,0,crudePick.name);
        NoTreePunching.proxy.registerItemModel(crudeHatchet,0,crudeHatchet.name);

        NoTreePunching.proxy.registerItemModel(firestarter,0,firestarter.name);
    }

    public static List<ItemStack> listAllKnives(){
        return new ArrayList<ItemStack>(Arrays.asList(new ItemStack[]{new ItemStack(stoneKnife),new ItemStack(ironKnife),new ItemStack(goldKnife),new ItemStack(diamondKnife)}));
    }
    public static List<ItemStack> listAllMattocks(){
        return new ArrayList<ItemStack>(Arrays.asList(new ItemStack[]{new ItemStack(ironMattock),new ItemStack(goldMattock),new ItemStack(diamondMattock)}));
    }
}

