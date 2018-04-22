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
import notreepunching.config.Config;
import notreepunching.item.ModItems;
import notreepunching.recipe.ModRecipes;
import notreepunching.world.WorldGen;
import org.apache.logging.log4j.Logger;
import notreepunching.proxy.CommonProxy;

@Mod(modid = NoTreePunching.MODID, version = NoTreePunching.VERSION, dependencies = "after:quark;after:rustic")

public class NoTreePunching {

    public static final String MODID = "notreepunching";
    public static final String VERSION = "GRADLE:VERSION";
    public static final String actualName = "NoTreePunching";

    public static final Item.ToolMaterial toolMaterialFlint = EnumHelper.addToolMaterial("NTP_FLINT", Config.Tools.FLINT_MINING_LEVEL,35,1.5F,0.5F,0);

    public static final CreativeTabBase NTP_Tab = new CreativeTabBase(NoTreePunching.MODID);

    public static boolean replaceQuarkStones;
    public static boolean replaceRusticStone;

    @SidedProxy(clientSide = "notreepunching.proxy.ClientProxy", serverSide = "notreepunching.proxy.ServerProxy")
    public static CommonProxy proxy;

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

        proxy.preInit(event);

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        logger.info("Init started");

        // Register Ore Dict
        ModItems.initOreDict();

        //ModRecipes.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        logger.info("Post-init started");

        proxy.postInit(event);
        NTP_Tab.setTabItem(ModItems.stoneKnife);

        logger.info("Finished Loading");
    }
}


