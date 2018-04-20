package notreepunching.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemMultiTexture;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import notreepunching.NoTreePunching;
import notreepunching.config.Config;

public class ModBlocks {

    public static BlockRock looseRock;

    public static BlockCobble andesiteCobble;
    public static BlockCobble graniteCobble;
    public static BlockCobble dioriteCobble;

    public static BlockCobble marbleCobble;
    public static BlockCobble limestoneCobble;
    public static BlockCobble slateCobble;

    public static BlockFirepit firepit;

    public static void init(){
        looseRock = new BlockRock("loose_rock");

        if(Config.VanillaTweaks.STONE_DROPS_ROCKS) {
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

        firepit = new BlockFirepit(Material.WOOD,"firepit");
    }

    public static void registerBlocks(RegistryEvent.Register<Block> event){
        event.getRegistry().registerAll(
                looseRock,
                firepit
        );
        if(Config.VanillaTweaks.STONE_DROPS_ROCKS){
            event.getRegistry().registerAll(
                    andesiteCobble,
                    graniteCobble,
                    dioriteCobble
            );
        }
        if(NoTreePunching.replaceQuarkStones){
            event.getRegistry().registerAll(
                    marbleCobble,
                    limestoneCobble
            );
        }
        if(NoTreePunching.replaceRusticStone){
            event.getRegistry().register(slateCobble);
        }

        GameRegistry.registerTileEntity(firepit.getTileEntityClass(), "tile_entity_firepit");
    }

    public static void registerItemBlocks(RegistryEvent.Register<Item> event){
        event.getRegistry().registerAll(
                new ItemMultiTexture(looseRock,looseRock,looseRock::getStoneName).setRegistryName(looseRock.name),
                new ItemBlock(firepit).setRegistryName(firepit.name)
        );
        if(Config.VanillaTweaks.STONE_DROPS_ROCKS){
            event.getRegistry().registerAll(
                    new ItemBlock(andesiteCobble).setRegistryName(andesiteCobble.name),
                    new ItemBlock(dioriteCobble).setRegistryName(dioriteCobble.name),
                    new ItemBlock(graniteCobble).setRegistryName(graniteCobble.name)
            );
        }
        if(NoTreePunching.replaceQuarkStones){
            event.getRegistry().registerAll(
                    new ItemBlock(marbleCobble).setRegistryName(marbleCobble.name),
                    new ItemBlock(limestoneCobble).setRegistryName(limestoneCobble.name)
            );
        }
        if(NoTreePunching.replaceRusticStone){
            event.getRegistry().register(new ItemBlock(slateCobble).setRegistryName(slateCobble.name));
        }
    }

    public static void registerItemBlockModels(){
        for(int i=0; i<7; i++) {
            NoTreePunching.proxy.registerItemModelWithVariant(Item.getItemFromBlock(looseRock), i, looseRock.name,"type="+looseRock.getStoneName(i));
        }

        if(Config.VanillaTweaks.STONE_DROPS_ROCKS) {
            NoTreePunching.proxy.registerItemModel(Item.getItemFromBlock(andesiteCobble), 0, andesiteCobble.name);
            NoTreePunching.proxy.registerItemModel(Item.getItemFromBlock(dioriteCobble), 0, dioriteCobble.name);
            NoTreePunching.proxy.registerItemModel(Item.getItemFromBlock(graniteCobble), 0, graniteCobble.name);
        }
        if(NoTreePunching.replaceQuarkStones){
            NoTreePunching.proxy.registerItemModel(Item.getItemFromBlock(marbleCobble), 0, marbleCobble.name);
            NoTreePunching.proxy.registerItemModel(Item.getItemFromBlock(limestoneCobble), 0, limestoneCobble.name);
        }
        if(NoTreePunching.replaceRusticStone){
            NoTreePunching.proxy.registerItemModel(Item.getItemFromBlock(slateCobble), 0, slateCobble.name);
        }

        NoTreePunching.proxy.registerItemModel(Item.getItemFromBlock(firepit),0,firepit.name);
    }
}
