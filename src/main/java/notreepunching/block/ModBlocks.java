package notreepunching.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import notreepunching.NoTreePunching;
import notreepunching.client.ModTabs;
import notreepunching.config.ModConfig;
import notreepunching.registry.RegistryHandler;

import javax.annotation.Nullable;

public class ModBlocks {

    public static BlockRock looseRock;

    public static BlockCobble andesiteCobble;
    public static BlockCobble graniteCobble;
    public static BlockCobble dioriteCobble;

    public static BlockCobble marbleCobble;
    public static BlockCobble limestoneCobble;
    public static BlockCobble slateCobble;

    public static BlockFirepit firepit;
    public static BlockCharcoalPile charcoalPile;
    public static BlockForge forge;
    public static BlockWoodPile woodPile;
    public static BlockBellows bellows;
    public static BlockGrindstone grindstone;

    public static BlockMetal oreCopper;
    public static BlockMetal oreTin;
    public static BlockMetal blockTin;
    public static BlockMetal blockCopper;
    public static BlockMetal blockBronze;
    public static BlockMetal blockSteel;

    public static void init(){
        looseRock = new BlockRock("loose_rock");

        if(ModConfig.VanillaTweaks.STONE_DROPS_ROCKS) {
            andesiteCobble = new BlockCobble("andesite");
            graniteCobble = new BlockCobble("granite");
            dioriteCobble = new BlockCobble("diorite");
        }
        if(NoTreePunching.replaceQuarkStones){
            marbleCobble = new BlockCobble("marble");
            limestoneCobble = new BlockCobble("limestone");
        }
        if(NoTreePunching.replaceRusticStone){
            slateCobble = new BlockCobble("slate");
        }

        firepit = new BlockFirepit("firepit");
        charcoalPile = new BlockCharcoalPile("charcoal_pile");
        forge = new BlockForge("forge");
        woodPile = new BlockWoodPile("wood_pile");
        bellows = new BlockBellows("bellows");
        grindstone = new BlockGrindstone("grindstone");

        oreCopper = new BlockMetal("ore_copper");
        oreTin = new BlockMetal("ore_tin");
        blockBronze = new BlockMetal("block_bronze");
        blockSteel = new BlockMetal("block_steel");
        blockCopper = new BlockMetal("block_copper");
        blockTin = new BlockMetal("block_tin");

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
