/*
 *
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 *
 */

package notreepunching.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import notreepunching.NoTreePunching;
import notreepunching.client.ModTabs;
import notreepunching.config.ModConfig;
import notreepunching.registry.RegistryHandler;

import javax.annotation.Nullable;

public class ModBlocks {

    public static BlockRock looseRock;

    public static BlockBase andesiteCobble;
    public static BlockBase graniteCobble;
    public static BlockBase dioriteCobble;
    public static BlockBase marbleCobble;
    public static BlockBase limestoneCobble;
    public static BlockBase slateCobble;

    public static BlockFirepit firepit;
    public static BlockCharcoalPile charcoalPile;
    public static BlockForge forge;
    public static BlockWoodPile woodPile;
    public static BlockBellows bellows;
    public static BlockGrindstone grindstone;
    public static BlockBlastFurnace blastFurnace;

    public static BlockBase oreCopper;
    public static BlockBase oreTin;
    public static BlockBase blockTin;
    public static BlockBase blockCopper;
    public static BlockBase blockBronze;
    public static BlockBase blockSteel;

    public static BlockTuyere blockTuyere;

    public static BlockWorkedClay workedClay;
    public static BlockLargeVessel largeVessel;

    public static void init(){

        // NTP Core
        looseRock = new BlockRock("loose_rock");
        firepit = new BlockFirepit("firepit");
        woodPile = new BlockWoodPile("wood_pile");
        charcoalPile = new BlockCharcoalPile("charcoal_pile");
        grindstone = new BlockGrindstone("grindstone");

        if(ModConfig.MODULE_STONEWORKS) {
            andesiteCobble = new BlockBase("andesite_cobblestone", Material.ROCK).setHardness(2.0F);
            graniteCobble = new BlockBase("granite_cobblestone", Material.ROCK).setHardness(2.0F);
            dioriteCobble = new BlockBase("diorite_cobblestone", Material.ROCK).setHardness(2.0F);
            if (NoTreePunching.hasQuark) {
                marbleCobble = new BlockBase("marble_cobblestone", Material.ROCK).setHardness(2.0F);
                limestoneCobble = new BlockBase("limestone_cobblestone", Material.ROCK).setHardness(2.0F);
            }
            if (NoTreePunching.hasRustic) {
                slateCobble = new BlockBase("slate_cobblestone", Material.ROCK).setHardness(2.0F);
            }
            // Brick Blocks
            // Smooth Variants
        }

        if(ModConfig.MODULE_METALWORKING) {
            forge = new BlockForge("forge");
            bellows = new BlockBellows("bellows");
            blastFurnace = new BlockBlastFurnace("blast_furnace");

            oreCopper = new BlockBase("ore_copper", Material.ROCK).setHardness(2.5F).setHarvestType("pickaxe", 0);
            oreTin = new BlockBase("ore_tin", Material.ROCK).setHardness(2.5F).setHarvestType("pickaxe", 0);
            blockBronze = new BlockBase("block_bronze", Material.IRON).setHardness(4.0F).setHarvestType("pickaxe", 2);
            blockSteel = new BlockBase("block_steel", Material.IRON).setHardness(4.0F).setHarvestType("pickaxe", 3);
            blockCopper = new BlockBase("block_copper", Material.IRON).setHardness(4.0F).setHarvestType("pickaxe", 1);
            blockTin = new BlockBase("block_tin", Material.IRON).setHardness(4.0F).setHarvestType("pickaxe", 0);

            blockTuyere = new BlockTuyere("block_tuyere");
        }

        if(ModConfig.MODULE_POTTERY){
            workedClay = new BlockWorkedClay("worked_clay");
            largeVessel = new BlockLargeVessel("large_vessel");
        }
    }

    public static void addBlockToRegistry(Block block, ItemBlock itemBlock, String name){
        addBlockToRegistry(block, itemBlock, name, null);
    }

    public static void addBlockToRegistry(Block block, ItemBlock itemBlock, String name, @Nullable ModTabs tab){
        block.setRegistryName(name);
        block.setUnlocalizedName(name);
        itemBlock.setRegistryName(name);

        if(tab != null){
            block.setCreativeTab(tab);
        }

        RegistryHandler.BLOCK_REGISTRY.add(block);
        RegistryHandler.ITEM_REGISTRY.add(itemBlock);
    }
}
