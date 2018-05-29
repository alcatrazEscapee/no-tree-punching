package notreepunching.item;

import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
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
    public static ItemBaseAxe copperAxe;
    public static ItemBaseAxe bronzeAxe;
    public static ItemBaseAxe steelAxe;

    public static ItemBaseSword copperSword;
    public static ItemBaseSword bronzeSword;
    public static ItemBaseSword steelSword;

    public static ItemBasePick crudePick;
    public static ItemBasePick copperPick;
    public static ItemBasePick bronzePick;
    public static ItemBasePick steelPick;

    public static ItemBaseShovel crudeShovel;
    public static ItemBaseShovel copperShovel;
    public static ItemBaseShovel bronzeShovel;
    public static ItemBaseShovel steelShovel;

    public static ItemBaseHoe crudeHoe;
    public static ItemBaseHoe copperHoe;
    public static ItemBaseHoe bronzeHoe;
    public static ItemBaseHoe steelHoe;

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
    public static ItemBase nuggetCopper;
    public static ItemBase nuggetTin;
    public static ItemBase nuggetBronze;
    public static ItemBase nuggetSteel;
    public static ItemBase gearWood;

    public static ItemBaseArmor helmetCopper;
    public static ItemBaseArmor helmetBronze;
    public static ItemBaseArmor helmetSteel;
    public static ItemBaseArmor chestplateCopper;
    public static ItemBaseArmor chestplateBronze;
    public static ItemBaseArmor chestplateSteel;
    public static ItemBaseArmor leggingsCopper;
    public static ItemBaseArmor leggingsBronze;
    public static ItemBaseArmor leggingsSteel;
    public static ItemBaseArmor bootsCopper;
    public static ItemBaseArmor bootsBronze;
    public static ItemBaseArmor bootsSteel;

    private static Item.ToolMaterial toolMaterialFlint = EnumHelper.addToolMaterial("NTP_FLINT", ModConfig.Balance.FLINT_MINING_LEVEL,45,2.5F,0.5F,0);
    private static Item.ToolMaterial toolMaterialCopper = EnumHelper.addToolMaterial("NTP_COPPER", ModConfig.Balance.COPPER_MINING_LEVEL,220,4F,1.5F,6);
    private static Item.ToolMaterial toolMaterialBronze = EnumHelper.addToolMaterial("NTP_BRONZE", ModConfig.Balance.BRONZE_MINING_LEVEL,400,8F,2.5F,8);
    private static Item.ToolMaterial toolMaterialSteel = EnumHelper.addToolMaterial("NTP_STEEL", ModConfig.Balance.STEEL_MINING_LEVEL,1600,11F,3.0F,10);

    private static ItemArmor.ArmorMaterial armorMaterialCopper = EnumHelper.addArmorMaterial(
            "NTP_COPPER", NoTreePunching.MODID+"copper", 80, new int[]{1,3,4,1}, 6, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);
    private static ItemArmor.ArmorMaterial armorMaterialBronze = EnumHelper.addArmorMaterial(
            "NTP_BRONZE", NoTreePunching.MODID+"bronze", 140, new int[]{2,4,6,2}, 8, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2.0F);
    private static ItemArmor.ArmorMaterial armorMaterialSteel = EnumHelper.addArmorMaterial(
            "NTP_STEEL", NoTreePunching.MODID+"steel", 400, new int[]{3,6,8,3}, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 4.0F);

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
        nuggetBronze = new ItemBase("nugget_bronze");
        nuggetSteel = new ItemBase("nugget_steel");
        nuggetTin = new ItemBase("nugget_tin");
        nuggetCopper = new ItemBase("nugget_copper");

        // TOOLS
        stoneKnife = new ItemKnife(toolMaterialFlint,"stone_knife");
        crudeHatchet = new ItemCrudeAxe(toolMaterialFlint,"crude_axe");
        crudePick = new ItemBasePick(toolMaterialFlint,"crude_pick");
        crudeShovel = new ItemBaseShovel(toolMaterialFlint, "crude_shovel");
        crudeHoe = new ItemBaseHoe(toolMaterialFlint, "crude_hoe");

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
        copperPick = new ItemBasePick(toolMaterialCopper, "copper_pick");
        copperAxe = new ItemBaseAxe(toolMaterialCopper, "copper_axe");
        copperHoe = new ItemBaseHoe(toolMaterialCopper, "copper_hoe");
        copperShovel = new ItemBaseShovel(toolMaterialCopper, "copper_shovel");
        copperSword = new ItemBaseSword(toolMaterialCopper, "copper_sword");

        bronzeKnife = new ItemKnife(toolMaterialBronze,"bronze_knife");
        bronzeSaw = new ItemSaw(toolMaterialBronze, "bronze_saw");
        bronzeMattock = new ItemMattock(toolMaterialBronze, "bronze_mattock");
        bronzePick = new ItemBasePick(toolMaterialBronze, "bronze_pick");
        bronzeAxe = new ItemBaseAxe(toolMaterialBronze, "bronze_axe");
        bronzeHoe = new ItemBaseHoe(toolMaterialBronze, "bronze_hoe");
        bronzeShovel = new ItemBaseShovel(toolMaterialBronze, "bronze_shovel");
        bronzeSword = new ItemBaseSword(toolMaterialBronze, "bronze_sword");

        steelKnife = new ItemKnife(toolMaterialSteel, "steel_knife");
        steelSaw = new ItemSaw(toolMaterialSteel, "steel_saw");
        steelMattock = new ItemMattock(toolMaterialSteel, "steel_mattock");
        steelPick = new ItemBasePick(toolMaterialSteel, "steel_pick");
        steelAxe = new ItemBaseAxe(toolMaterialSteel, "steel_axe");
        steelHoe = new ItemBaseHoe(toolMaterialSteel, "steel_hoe");
        steelShovel = new ItemBaseShovel(toolMaterialSteel, "steel_shovel");
        steelSword = new ItemBaseSword(toolMaterialSteel, "steel_sword");

        // ARMOR
        helmetCopper = new ItemBaseArmor(armorMaterialCopper, EntityEquipmentSlot.HEAD, "copper_helmet");
        chestplateCopper = new ItemBaseArmor(armorMaterialCopper, EntityEquipmentSlot.CHEST, "copper_chestplate");
        leggingsCopper = new ItemBaseArmor(armorMaterialCopper, EntityEquipmentSlot.LEGS, "copper_leggings");
        bootsCopper = new ItemBaseArmor(armorMaterialCopper, EntityEquipmentSlot.FEET, "copper_boots");

        helmetBronze = new ItemBaseArmor(armorMaterialBronze, EntityEquipmentSlot.HEAD, "bronze_helmet");
        chestplateBronze = new ItemBaseArmor(armorMaterialBronze, EntityEquipmentSlot.CHEST, "bronze_chestplate");
        leggingsBronze = new ItemBaseArmor(armorMaterialBronze, EntityEquipmentSlot.LEGS, "bronze_leggings");
        bootsBronze = new ItemBaseArmor(armorMaterialBronze, EntityEquipmentSlot.FEET, "bronze_boots");

        helmetSteel = new ItemBaseArmor(armorMaterialSteel, EntityEquipmentSlot.HEAD, "steel_helmet");
        chestplateSteel = new ItemBaseArmor(armorMaterialSteel, EntityEquipmentSlot.CHEST, "steel_chestplate");
        leggingsSteel = new ItemBaseArmor(armorMaterialSteel, EntityEquipmentSlot.LEGS, "steel_leggings");
        bootsSteel = new ItemBaseArmor(armorMaterialSteel, EntityEquipmentSlot.FEET, "steel_boots");


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
        OreDictionary.registerOre("nuggetBronze", new ItemStack(nuggetBronze));
        OreDictionary.registerOre("nuggetCopper", new ItemStack(nuggetCopper));
        OreDictionary.registerOre("nuggetSteel", new ItemStack(nuggetSteel));
        OreDictionary.registerOre("nuggetTin", new ItemStack(nuggetTin));

        OreDictionary.registerOre("oreCopper", new ItemStack(ModBlocks.oreCopper));
        OreDictionary.registerOre("oreTin", new ItemStack(ModBlocks.oreTin));
        OreDictionary.registerOre("blockTin", new ItemStack(ModBlocks.blockTin));
        OreDictionary.registerOre("blockCopper", new ItemStack(ModBlocks.blockCopper));
        OreDictionary.registerOre("blockBronze", new ItemStack(ModBlocks.blockBronze));
        OreDictionary.registerOre("blockSteel", new ItemStack(ModBlocks.blockSteel));

        OreDictionary.registerOre("gearWood", new ItemStack(gearWood));
    }
}

