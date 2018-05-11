package notreepunching.registry;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import notreepunching.block.ModBlocks;
import notreepunching.item.ModItems;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientRegistryHandler {

    public static final Map<ItemStack, ModelResourceLocation> MODEL_REGISTRY = new HashMap<ItemStack, ModelResourceLocation>();

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ModItems.registerItemModels();
        ModBlocks.registerItemBlockModels();
    }

}
