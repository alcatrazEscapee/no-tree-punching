package notreepunching.item;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.OreDictionary;
import notreepunching.NoTreePunching;
import notreepunching.block.ModBlocks;
import notreepunching.client.ModTabs;
import notreepunching.config.ModConfig;
import notreepunching.registry.RegistryHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

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
    public static ItemAxeBase copperAxe;
    public static ItemAxeBase bronzeAxe;
    public static ItemAxeBase steelAxe;

    public static ItemSwordBase copperSword;
    public static ItemSwordBase bronzeSword;
    public static ItemSwordBase steelSword;

    public static ItemPickBase crudePick;
    public static ItemPickBase copperPick;
    public static ItemPickBase bronzePick;
    public static ItemPickBase steelPick;

    public static ItemShovelBase crudeShovel;
    public static ItemShovelBase copperShovel;
    public static ItemShovelBase bronzeShovel;
    public static ItemShovelBase steelShovel;

    public static ItemHoeBase crudeHoe;
    public static ItemHoeBase copperHoe;
    public static ItemHoeBase bronzeHoe;
    public static ItemHoeBase steelHoe;

    public static ItemFirestarter firestarter;
    public static ItemGrindWheel grindWheel;

    public static ItemBase ingotTin;
    public static ItemBase ingotCopper;
    public static ItemBase ingotBronze;
    public static ItemBase ingotSteel;
    public static ItemBase dustTin;
    public static ItemBase dustCopper;
    public static ItemBase dustBronze;
    public static ItemBase dustIron;
    public static ItemBase dustSteel;
    public static ItemBase gearWood;

    private static Item.ToolMaterial toolMaterialFlint = EnumHelper.addToolMaterial("NTP_FLINT", ModConfig.Balance.FLINT_MINING_LEVEL,45,2.5F,0.5F,0);
    private static Item.ToolMaterial toolMaterialCopper = EnumHelper.addToolMaterial("NTP_COPPER", ModConfig.Balance.COPPER_MINING_LEVEL,180,4F,1.5F,6);
    private static Item.ToolMaterial toolMaterialBronze = EnumHelper.addToolMaterial("NTP_BRONZE", ModConfig.Balance.BRONZE_MINING_LEVEL,350,8F,2.5F,8);
    private static Item.ToolMaterial toolMaterialSteel = EnumHelper.addToolMaterial("NTP_STEEL", ModConfig.Balance.STEEL_MINING_LEVEL,1400,11F,3.0F,10);

    public static void init(){
        rockStone = new ItemRock("rock");
        grassFiber = new ItemBase("grass_fiber");
        grassString = new ItemBase("grass_string");
        flintShard = new ItemBase("flint_shard");

        firestarter = new ItemFirestarter("firestarter");
        grindWheel = new ItemGrindWheel("grind_wheel");

        ingotBronze = new ItemBase("ingot_bronze");
        ingotTin = new ItemBase("ingot_tin");
        ingotCopper = new ItemBase("ingot_copper");
        ingotSteel = new ItemBase("ingot_steel");
        dustBronze = new ItemBase("dust_bronze");
        dustCopper = new ItemBase("dust_copper");
        dustTin = new ItemBase("dust_tin");
        dustIron = new ItemBase("dust_iron");
        dustSteel = new ItemBase("dust_steel");
        gearWood = new ItemBase("gear_wood");

        // TOOLS
        stoneKnife = new ItemKnife(toolMaterialFlint,"stone_knife");
        crudeHatchet = new ItemCrudeAxe(toolMaterialFlint,"crude_axe");
        crudePick = new ItemPickBase(toolMaterialFlint,"crude_pick");
        crudeShovel = new ItemShovelBase(toolMaterialFlint, "crude_shovel");
        crudeHoe = new ItemHoeBase(toolMaterialFlint, "crude_hoe");

        ironKnife = new ItemKnife(Item.ToolMaterial.IRON,"iron_knife");
        ironMattock = new ItemMattock(Item.ToolMaterial.IRON,"iron_mattock");
        ironSaw = new ItemSaw(Item.ToolMaterial.IRON,"iron_saw");

        goldKnife = new ItemKnife(Item.ToolMaterial.GOLD, "gold_knife");
        goldMattock = new ItemMattock(Item.ToolMaterial.GOLD,"gold_mattock");
        goldSaw = new ItemSaw(Item.ToolMaterial.GOLD,"gold_saw");

        diamondKnife = new ItemKnife(Item.ToolMaterial.DIAMOND,"diamond_knife");
        diamondMattock = new ItemMattock(Item.ToolMaterial.DIAMOND,"diamond_mattock");
        diamondSaw = new ItemSaw(Item.ToolMaterial.DIAMOND,"diamond_saw");

        copperKnife = new ItemKnife(toolMaterialCopper,"copper_knife");
        copperSaw = new ItemSaw(toolMaterialCopper,"copper_saw");
        copperMattock = new ItemMattock(toolMaterialCopper,"copper_mattock");
        copperPick = new ItemPickBase(toolMaterialCopper, "copper_pick");
        copperAxe = new ItemAxeBase(toolMaterialCopper, "copper_axe");
        copperHoe = new ItemHoeBase(toolMaterialCopper, "copper_hoe");
        copperShovel = new ItemShovelBase(toolMaterialCopper, "copper_shovel");
        copperSword = new ItemSwordBase(toolMaterialCopper, "copper_sword");

        bronzeKnife = new ItemKnife(toolMaterialBronze,"bronze_knife");
        bronzeSaw = new ItemSaw(toolMaterialBronze, "bronze_saw");
        bronzeMattock = new ItemMattock(toolMaterialBronze, "bronze_mattock");
        bronzePick = new ItemPickBase(toolMaterialBronze, "bronze_pick");
        bronzeAxe = new ItemAxeBase(toolMaterialBronze, "bronze_axe");
        bronzeHoe = new ItemHoeBase(toolMaterialBronze, "bronze_hoe");
        bronzeShovel = new ItemShovelBase(toolMaterialBronze, "bronze_shovel");
        bronzeSword = new ItemSwordBase(toolMaterialBronze, "bronze_sword");

        steelKnife = new ItemKnife(toolMaterialSteel, "steel_knife");
        steelSaw = new ItemSaw(toolMaterialSteel, "steel_saw");
        steelMattock = new ItemMattock(toolMaterialSteel, "steel_mattock");
        steelPick = new ItemPickBase(toolMaterialSteel, "steel_pick");
        steelAxe = new ItemAxeBase(toolMaterialSteel, "steel_axe");
        steelHoe = new ItemHoeBase(toolMaterialSteel, "steel_hoe");
        steelShovel = new ItemShovelBase(toolMaterialSteel, "steel_shovel");
        steelSword = new ItemSwordBase(toolMaterialSteel, "steel_sword");

    }

    public static void addItemToRegistry(Item item, String name){
        addItemToRegistry(item, name, null);
    }
    public static void addItemToRegistry(Item item, String name, @Nullable ModTabs tab){
        item.setUnlocalizedName(name);
        item.setRegistryName(name);

        if(tab != null){
            item.setCreativeTab(tab);
        }

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
        array.add(new ItemStack(steelKnife));
        return array;
    }
    public static List<ItemStack> listAllMattocks(){
        List<ItemStack> array = new ArrayList<>();
        array.add(new ItemStack(ironMattock));
        array.add(new ItemStack(goldMattock));
        array.add(new ItemStack(diamondMattock));
        array.add(new ItemStack(copperMattock));
        array.add(new ItemStack(bronzeMattock));
        array.add(new ItemStack(steelMattock));
        return array;
    }

    public static List<ItemStack> listAllSaws(){
        List<ItemStack> array = new ArrayList<>();
        array.add(new ItemStack(ironSaw));
        array.add(new ItemStack(goldSaw));
        array.add(new ItemStack(diamondSaw));
        array.add(new ItemStack(copperSaw));
        array.add(new ItemStack(bronzeSaw));
        array.add(new ItemStack(steelSaw));
        return array;
    }

    public static void initOreDict(){
        OreDictionary.registerOre("toolSaw", new ItemStack(diamondSaw,1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("toolSaw", new ItemStack(ironSaw,1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("toolSaw", new ItemStack(goldSaw,1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("toolSaw",new ItemStack(copperSaw,1,OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("toolSaw",new ItemStack(bronzeSaw,1,OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("toolSaw",new ItemStack(steelSaw,1,OreDictionary.WILDCARD_VALUE));

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
        OreDictionary.registerOre("ingotSteel", new ItemStack(ingotSteel));
        OreDictionary.registerOre("dustIron", new ItemStack(dustIron));
        OreDictionary.registerOre("dustCopper", new ItemStack(dustCopper));
        OreDictionary.registerOre("dustTin", new ItemStack(dustTin));
        OreDictionary.registerOre("dustBronze", new ItemStack(dustBronze));
        OreDictionary.registerOre("dustSteel", new ItemStack(dustSteel));

        OreDictionary.registerOre("oreCopper", new ItemStack(ModBlocks.oreCopper));
        OreDictionary.registerOre("oreTin", new ItemStack(ModBlocks.oreTin));

        OreDictionary.registerOre("gearWood", new ItemStack(gearWood));
    }
}

