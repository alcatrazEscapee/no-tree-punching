/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package notreepunching;

import org.apache.logging.log4j.Logger;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import alcatrazcore.util.RegistryHelper;
import notreepunching.client.ModSounds;
import notreepunching.common.blocks.ModBlocks;
import notreepunching.common.capability.CapabilityPlayerHarvestTool;
import notreepunching.common.items.ModItems;


@Mod(modid = NoTreePunching.MOD_ID, version = NoTreePunching.VERSION, dependencies = NoTreePunching.DEPENDENCIES, useMetadata = true)
public class NoTreePunching
{

    public static final String MOD_ID = "notreepunching";
    public static final String MOD_NAME = "No Tree Punching";

    // Versioning / Dependencies
    public static final String VERSION = "GRADLE:VERSION";
    public static final String FORGE_REQUIRED = "required-after:forge@[GRADLE:FORGE_VERSION,15.0.0.0);";
    public static final String ALC_CORE_REQUIRED = "required-after:alcatrazcore@[1.0.0,2.0.0)";
    public static final String DEPENDENCIES = FORGE_REQUIRED + ALC_CORE_REQUIRED;

    @Mod.Instance
    private static NoTreePunching INSTANCE;
    private static Logger LOG;
    private static SimpleNetworkWrapper NETWORK;

    public static NoTreePunching getInstance()
    {
        return INSTANCE;
    }

    public static Logger getLog()
    {
        return LOG;
    }

    public static SimpleNetworkWrapper getNetwork()
    {
        return NETWORK;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        LOG = event.getModLog();
        LOG.info("Pre-init started");

        //NetworkRegistry.INSTANCE.registerGuiHandler(this, new ModGuiHandler());
        MinecraftForge.EVENT_BUS.register(ModEventHandler.INSTANCE);

        NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel("notreepunching");

        CapabilityPlayerHarvestTool.preInit();

        // Bellows
        //NETWORK.registerMessage(PacketUpdateBellows.Handler.class, PacketUpdateBellows.class, ++id, Side.CLIENT);
        //NETWORK.registerMessage(PacketRequestBellows.Handler.class, PacketRequestBellows.class, ++id, Side.SERVER);
        // Grindstone
        //NETWORK.registerMessage(PacketUpdateGrindstone.Handler.class, PacketUpdateGrindstone.class, ++id, Side.CLIENT);
        //NETWORK.registerMessage(PacketRequestGrindstone.Handler.class, PacketRequestGrindstone.class, ++id, Side.SERVER);

        ModBlocks.preInit();
        ModItems.preInit();
        ModSounds.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        LOG.info("Init started");

        // Register World Generation
        //MinecraftForge.EVENT_BUS.register(new WorldGenDeco());
        //GameRegistry.registerWorldGenerator(new WorldGenOres(), 3);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        LOG.info("Post-init started");

        // Recipes Post init
        //ModEventHandler.postInit();

        //ModCreativeTabs.ITEMS_TAB.setTabItem(ItemRock.get(Stone.STONE));
        //ModCreativeTabs.TOOLS_TAB.setTabItem(ModItems.crudePick);

        LOG.info("Finished Loading");
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event)
    {
        RegistryHelper.get(MOD_ID).initItems(event);
    }

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event)
    {
        RegistryHelper.get(MOD_ID).initBlocks(event);
    }

    @SubscribeEvent
    public void registerSounds(RegistryEvent.Register<SoundEvent> event)
    {
        RegistryHelper.get(MOD_ID).initSounds(event);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void registerModels(ModelRegistryEvent event)
    {
        RegistryHelper.get(MOD_ID).initModels(event);
    }

}


