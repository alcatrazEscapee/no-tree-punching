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

    // Config options:
    public static boolean CFG_WOOD_TOOLS_DISABLE = true;
    public static boolean CFG_STONE_TOOLS_DISABLE = true;
    public static boolean CFG_ALTERNATE_FURNACE_RECIPE = true;

    public static List<String> CFG_ALWAYS_BREAKABLE = new ArrayList<String>();

    // This will create the config if it doesn't exist yet and read the values if it does exist.
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

        CFG_WOOD_TOOLS_DISABLE = cfg.getBoolean("disable_wood_tools",GENERAL,true,"Disables vanilla wooden tool recipes");
        CFG_STONE_TOOLS_DISABLE = cfg.getBoolean("disable_stone_tools",GENERAL,true,"Disables vanilla stone tool recipes");
        CFG_STONE_TOOLS_DISABLE = cfg.getBoolean("disable_furnace",GENERAL,true,"Forces the vanilla furnace recipe to require one coal in the center.");

        String[] list = cfg.getStringList("always_breakable",GENERAL, new String[] {"notreepunching:loose_rock","minecraft:leaves"},"List of blocks that will always drop their item. Use the format modid:registry_name");
        CFG_ALWAYS_BREAKABLE = new ArrayList<String>( Arrays.asList(list) );

    }
}