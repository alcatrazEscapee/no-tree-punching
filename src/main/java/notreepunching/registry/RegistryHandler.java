package notreepunching.registry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import notreepunching.NoTreePunching;
import notreepunching.block.ModBlocks;
import notreepunching.block.tile.IHasTileEntity;
import notreepunching.client.ModSounds;
import notreepunching.item.ModItems;
import notreepunching.recipe.ModRecipes;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class RegistryHandler {

    public static final List<Item> ITEM_REGISTRY = new ArrayList<Item>();
    public static final List<Block> BLOCK_REGISTRY = new ArrayList<Block>();
    public static final List<SoundEvent> SOUND_REGISTRY = new ArrayList<SoundEvent>();

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        ModItems.init();

        for(Item item : ITEM_REGISTRY){
            event.getRegistry().register(item);
        }

        ModItems.initOreDict();
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        ModBlocks.init();

        for(Block block : BLOCK_REGISTRY){
            event.getRegistry().register(block);
            if(block instanceof IHasTileEntity){
                GameRegistry.registerTileEntity(((IHasTileEntity<?>)block).getTileEntityClass(), NoTreePunching.MODID+"_"+block.getUnlocalizedName());
            }
        }
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        ModRecipes.removeVanillaRecipes(event.getRegistry());
    }

    @SubscribeEvent
    public static void registerSoundEvents(RegistryEvent.Register<SoundEvent> event) {
        ModSounds.init();

        for(SoundEvent sound : SOUND_REGISTRY){
            event.getRegistry().register(sound);
        }
    }
}
