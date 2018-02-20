package notreepunching.proxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import notreepunching.NoTreePunching;
import notreepunching.block.ModBlocks;
import notreepunching.item.ModItems;


@Mod.EventBusSubscriber
public class ClientProxy extends CommonProxy {

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ModItems.registerItemModels();
        ModBlocks.registerItemBlockModels();
    }

    @Override
    public void registerItemModel(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(NoTreePunching.MODID + ":" + id, "inventory"));
    }

    @Override
    public void registerItemModelWithVariant(Item item, int meta, String id, String variant){
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(NoTreePunching.MODID + ":" + id,variant));
    }

    @Override
    public String localize(String unlocalized, Object... args) {
        return I18n.format(unlocalized, args);
    }

}
