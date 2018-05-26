package notreepunching.registry;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import notreepunching.block.IHasItemBlockModel;
import notreepunching.block.tile.IHasTESR;
import notreepunching.block.tile.IHasTileEntity;
import notreepunching.block.tile.TileEntityBellows;
import notreepunching.client.tesr.TESRBellows;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientRegistryHandler {

    public static final Map<ItemStack, ModelResourceLocation> MODEL_REGISTRY = new HashMap<ItemStack, ModelResourceLocation>();

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {

        for(Block block : RegistryHandler.BLOCK_REGISTRY){
            // ItemBlock Render models
            // This has to be here, otherwise the ItemStack picks up 'tile.air' for some reason
            if(block instanceof IHasItemBlockModel){
                ((IHasItemBlockModel) block).addModelToRegistry();
            }
            // TESR Models
            if(block instanceof IHasTESR){
                IHasTESR blockTESR = (IHasTESR) block;
                ClientRegistry.bindTileEntitySpecialRenderer(blockTESR.getTileEntityClass(), blockTESR.getTESR());
            }
        }

        // Add model to everything in MODEL_REGISTRY
        for(Map.Entry<ItemStack, ModelResourceLocation> entry : MODEL_REGISTRY.entrySet()){
            ModelLoader.setCustomModelResourceLocation(entry.getKey().getItem(), entry.getKey().getItemDamage(), entry.getValue());
        }

    }
}
