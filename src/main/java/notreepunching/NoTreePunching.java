package notreepunching;

import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import notreepunching.client.CreativeTabBase;
import notreepunching.client.NTPGuiHandler;
import notreepunching.config.ModConfig;
import notreepunching.event.HarvestEventHandler;
import notreepunching.event.PlayerEventHandler;
import notreepunching.item.ModItems;
import notreepunching.network.ModNetwork;
import notreepunching.proxy.IProxy;
import notreepunching.recipe.ModRecipes;
import notreepunching.registry.RegistryHandler;
import notreepunching.world.WorldGenDeco;
import notreepunching.world.WorldGenOres;
import org.apache.logging.log4j.Logger;

@Mod(modid = NoTreePunching.MODID, version = NoTreePunching.VERSION, dependencies = "after:quark;after:rustic;after:biomeoplenty")

public class NoTreePunching {

    public static final String MODID = "notreepunching";
    public static final String VERSION = "GRADLE:VERSION";
    public static final String actualName = "NoTreePunching";

    public static final CreativeTabBase NTP_Tab = new CreativeTabBase(NoTreePunching.MODID);

    public static boolean replaceQuarkStones;
    public static boolean replaceRusticStone;

    @SidedProxy(clientSide = MODID+".proxy.ClientProxy", serverSide = MODID+".proxy.ServerProxy")
    public static IProxy proxy;

    @Mod.Instance
    public static NoTreePunching instance;

    private static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        logger.info("Pre-init started");

        replaceQuarkStones = Loader.isModLoaded("quark") && ModConfig.VanillaTweaks.QUARK_STONE_REPLACE;
        replaceRusticStone = Loader.isModLoaded("rustic") && ModConfig.VanillaTweaks.RUSTIC_STONE_REPLACE;

        // Register World Generation
        MinecraftForge.EVENT_BUS.register(new WorldGenDeco());
        GameRegistry.registerWorldGenerator(new WorldGenOres(), 3);

        // Register GUI Handler
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new NTPGuiHandler());

        // Register Event Handlers
        MinecraftForge.EVENT_BUS.register(new HarvestEventHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());

        // Registry Handler
        MinecraftForge.EVENT_BUS.register(new RegistryHandler());

        // Register Network
        ModNetwork.preInit();

        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        logger.info("Init started");

        // Register Ore Dict
        ModRecipes.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        logger.info("Post-init started");

        ModRecipes.postInit();
        NTP_Tab.setTabItem(ModItems.stoneKnife);

        logger.info("Finished Loading");
    }
}


