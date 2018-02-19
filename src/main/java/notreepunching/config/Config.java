package notreepunching.config;

import notreepunching.proxy.CommonProxy;
import net.minecraftforge.common.config.Configuration;
import notreepunching.NoTreePunching;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Config {

    private static final String GENERAL = "General Options";

    // This values below you can access elsewhere in your mod:
    public static boolean CFG_VANILLA_HARVEST_TWEAKS = true;

    public static boolean CFG_WOOD_TOOLS_DISABLE = true;
    public static boolean CFG_STONE_TOOLS_DISABLE = true;

    public static List<String> CFG_ALWAYS_BREAKABLE = new ArrayList<String>();

    // Call this from CommonProxy.preInit(). It will create our config if it doesn't
    // exist yet and read the values if it does exist.
    public static void readConfig() {
        Configuration cfg = CommonProxy.config;
        try {
            cfg.load();
            initConfig(cfg);
        } catch (Exception problem) {
            NoTreePunching.logger.log(Level.ERROR, "Problem loading config file!", problem);
        } finally {
            if (cfg.hasChanged()) {
                cfg.save();
            }
        }
    }

    private static void initConfig(Configuration cfg) {

        CFG_VANILLA_HARVEST_TWEAKS = cfg.getBoolean("vanilla_hardness_tweaks",GENERAL,true,"Set to false to disable the changes to vanilla block breaking hardness");

        CFG_WOOD_TOOLS_DISABLE = cfg.getBoolean("disable_wood_tools",GENERAL,true,"Disables vanilla wooden tool recipes");
        CFG_STONE_TOOLS_DISABLE = cfg.getBoolean("disable_stone_tools",GENERAL,true,"Disables stone tool recipes");

        String[] list = cfg.getStringList("always_breakable",GENERAL, new String[] {"notreepunching:loose_rock","minecraft:leaves"},"List of blocks that will always drop their item. Use the format modid:registry_name");
        CFG_ALWAYS_BREAKABLE = new ArrayList<String>( Arrays.asList(list) );
        //fillAlwaysBreakable();
        //System.out.println("THIS IS SPARTA: "+CFG_ALWAYS_BREAKABLE);

    }
}