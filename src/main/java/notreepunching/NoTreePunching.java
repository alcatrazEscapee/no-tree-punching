package notreepunching;

import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import notreepunching.client.CreativeTabBase;
import notreepunching.client.NTPGuiHandler;
import notreepunching.config.Config;
import notreepunching.event.HarvestEventHandler;
import notreepunching.event.PlayerEventHandler;
import notreepunching.item.ModItems;
import notreepunching.proxy.IProxy;
import notreepunching.recipe.ModRecipes;
import notreepunching.registry.RegistryHandler;
import notreepunching.world.WorldGen;
import org.apache.logging.log4j.Logger;

@Mod(modid = NoTreePunching.MODID, version = NoTreePunching.VERSION, dependencies = "after:quark;after:rustic;after:biomeoplenty")

public class NoTreePunching {

    public static final String MODID = "notreepunching";
    public static final String VERSION = "GRADLE:VERSION";
    public static final String actualName = "NoTreePunching";

    public static final Item.ToolMaterial toolMaterialFlint = EnumHelper.addToolMaterial("NTP_FLINT", Config.Tools.FLINT_MINING_LEVEL,35,1.5F,0.5F,0);
    public static final Item.ToolMaterial toolMaterialCopper = EnumHelper.addToolMaterial("NTP_COPPER", Config.Tools.COPPER_MINING_LEVEL,180,4F,1.5F,6);
    public static final Item.ToolMaterial toolMaterialBronze = EnumHelper.addToolMaterial("NTP_BRONZE", Config.Tools.BRONZE_MINING_LEVEL,350,8F,2.5F,8);
    public static final Item.ToolMaterial toolMaterialSteel = EnumHelper.addToolMaterial("NTP_STEEL", Config.Tools.STEEL_MINING_LEVEL,1400,11F,3.0F,10);

    public static final CreativeTabBase NTP_Tab = new CreativeTabBase(NoTreePunching.MODID);

    public static boolean replaceQuarkStones;
    public static boolean replaceRusticStone;

    public static boolean addCopperTools;
    public static boolean addBronzeTools;
    public static boolean addSteelTools;

    @SidedProxy(clientSide = MODID+".proxy.ClientProxy", serverSide = MODID+".proxy.ServerProxy")
    public static IProxy proxy;

    @Mod.Instance
    public static NoTreePunching instance;

    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        logger.info("Pre-init started");

        replaceQuarkStones = Loader.isModLoaded("quark") && Config.VanillaTweaks.QUARK_STONE_REPLACE;
        replaceRusticStone = Loader.isModLoaded("rustic") && Config.VanillaTweaks.RUSTIC_STONE_REPLACE;

        // Register World Generation
        MinecraftForge.EVENT_BUS.register(new WorldGen());

        // Register GUI Handler
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new NTPGuiHandler());

        // Register Event Handlers
        MinecraftForge.EVENT_BUS.register(new HarvestEventHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());

        // Registry Handler
        MinecraftForge.EVENT_BUS.register(new RegistryHandler());

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

    @SubscribeEvent
    public void configChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(MODID)) {
            ConfigManager.sync(MODID, net.minecraftforge.common.config.Config.Type.INSTANCE);
        }
    }
}


