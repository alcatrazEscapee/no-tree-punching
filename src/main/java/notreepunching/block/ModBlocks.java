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

    public static void registerBlocks(RegistryEvent.Register<Block> event){
        event.getRegistry().registerAll(
                looseRock
        );
    }

    public static void registerItemBlocks(RegistryEvent.Register<Item> event){
        event.getRegistry().registerAll(
                new ItemBlock(looseRock).setRegistryName(looseRock.name)
        );
    }

    public static void registerItemBlockModels(){
        NoTreePunching.proxy.registerItemModel(Item.getItemFromBlock(looseRock),0,looseRock.name);
    }
}
