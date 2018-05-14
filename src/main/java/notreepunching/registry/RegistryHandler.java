package notreepunching.registry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import notreepunching.block.ModBlocks;
import notreepunching.block.firepit.TileEntityFirepit;
import notreepunching.block.forge.TileEntityForge;
import notreepunching.item.ModItems;
import notreepunching.recipe.ModRecipes;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class RegistryHandler {

    public static final List<Item> ITEM_REGISTRY = new ArrayList<Item>();
    public static final List<Block> BLOCK_REGISTRY = new ArrayList<Block>();

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        ModItems.init();

        for(Item item : ITEM_REGISTRY){
            event.getRegistry().register(item);
        }

        //ModItems.registerItems(event);

        ModItems.initOreDict();
        //ModBlocks.registerItemBlocks(event);
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        ModBlocks.init();

        for(Block block : BLOCK_REGISTRY){
            if(block!=null) {
                event.getRegistry().register(block);
            }
        }

        GameRegistry.registerTileEntity(TileEntityFirepit.class, "notreepunching_firepit");
        GameRegistry.registerTileEntity(TileEntityForge.class, "notreepunching_forge");

        //ModBlocks.registerBlocks(event);
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        ModRecipes.removeVanillaRecipes(event.getRegistry());
    }
}
