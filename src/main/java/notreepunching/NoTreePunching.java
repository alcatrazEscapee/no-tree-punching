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
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import notreepunching.client.CreativeTabBase;
import notreepunching.client.NTPGuiHandler;
import notreepunching.config.ModConfig;
import notreepunching.event.HarvestEventHandler;
import notreepunching.event.PlayerEventHandler;
import notreepunching.item.ModItems;
import notreepunching.network.ModNetwork;
import notreepunching.network.PacketRequestBellows;
import notreepunching.network.PacketUpdateBellows;
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

    public static final Item.ToolMaterial toolMaterialFlint = EnumHelper.addToolMaterial("NTP_FLINT", ModConfig.Balance.FLINT_MINING_LEVEL,45,2.5F,0.5F,0);
    public static final Item.ToolMaterial toolMaterialCopper = EnumHelper.addToolMaterial("NTP_COPPER", ModConfig.Balance.COPPER_MINING_LEVEL,180,4F,1.5F,6);
    public static final Item.ToolMaterial toolMaterialBronze = EnumHelper.addToolMaterial("NTP_BRONZE", ModConfig.Balance.BRONZE_MINING_LEVEL,350,8F,2.5F,8);
    public static final Item.ToolMaterial toolMaterialSteel = EnumHelper.addToolMaterial("NTP_STEEL", ModConfig.Balance.STEEL_MINING_LEVEL,1400,11F,3.0F,10);

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

    private static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        logger.info("Pre-init started");

        replaceQuarkStones = Loader.isModLoaded("quark") && ModConfig.VanillaTweaks.QUARK_STONE_REPLACE;
        replaceRusticStone = Loader.isModLoaded("rustic") && ModConfig.VanillaTweaks.RUSTIC_STONE_REPLACE;

        // Register World Generation
        MinecraftForge.EVENT_BUS.register(new WorldGen());

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


