package notreepunching.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import notreepunching.NoTreePunching;

public class ModBlocks {

    public static BlockRock looseRock = new BlockRock("loose_rock",Material.GROUND);
    public static BlockFirePit firePit = new BlockFirePit("fire_pit",Material.WOOD,true);

    public static void registerBlocks(RegistryEvent.Register<Block> event){
        event.getRegistry().registerAll(
                looseRock,
                firePit
        );
    }

    public static void registerItemBlocks(RegistryEvent.Register<Item> event){
        event.getRegistry().registerAll(
                new ItemBlock(looseRock).setRegistryName(looseRock.name),
                new ItemBlock(firePit).setRegistryName(firePit.name)

        );
    }

    public static void registerItemBlockModels(){
        //SimpleBeginnings.proxy.registerItemModel(Item);
    }

    public static void registerTileEntities(RegistryEvent.Register<Block> event){
        GameRegistry.registerTileEntity(firePit.getTileEntityClass(), firePit.name);
    }

    public static void registerVanillaTweaks(){
        // Wood Changes
        Blocks.LOG.setHardness(7F);
        Blocks.LOG.setHarvestLevel("axe",0);

        // Dirt / Grass / Gravel / Sand
        Blocks.DIRT.setHardness(0.8F);
        Blocks.GRASS.setHardness(0.9F);
        Blocks.GRAVEL.setHardness(1.1F);
        Blocks.SAND.setHardness(0.8F);

        // Add pickaxe harvest to piston
        Blocks.PISTON.setHarvestLevel("pickaxe",1);
    }
}
