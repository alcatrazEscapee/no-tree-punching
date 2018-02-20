package notreepunching;

import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import notreepunching.client.CreativeTabBase;
import notreepunching.item.ModItems;
import notreepunching.recipe.ModRecipes;
import notreepunching.world.WorldGen;
import org.apache.logging.log4j.Logger;
import notreepunching.proxy.CommonProxy;

@Mod(modid = NoTreePunching.MODID, version = NoTreePunching.VERSION)

public class NoTreePunching {


    public static final String MODID = "notreepunching";
    public static final String VERSION = "1.0";

    public static final Item.ToolMaterial toolMaterialFlint = EnumHelper.addToolMaterial("NTP_FLINT",1,30,2F,0.5F,0);
    public static final Item.ToolMaterial toolMaterialCrudeStone = EnumHelper.addToolMaterial("NTP_CRUDE_STONE",0,15,1F,1.5F,0);

    public static final CreativeTabBase NTP_Tab = new CreativeTabBase(NoTreePunching.MODID);

    @SidedProxy(clientSide = "notreepunching.proxy.ClientProxy", serverSide = "notreepunching.proxy.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static NoTreePunching instance;

    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        logger.info("Pre-init started");

        // Register World Generation
        //GameRegistry.registerWorldGenerator(new WorldGen(),0);
        MinecraftForge.EVENT_BUS.register(new WorldGen());

        // Register GUI Handler

        proxy.preInit(event);

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        logger.info("Init started");

        ModRecipes.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        logger.info("Post-init started");

        //proxy.postInit(event);
        NTP_Tab.setTabItem(ModItems.stoneKnife);

        logger.info("Finished Loading");
    }
}


