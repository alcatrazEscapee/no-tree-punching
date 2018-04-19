package notreepunching.config;

import notreepunching.proxy.CommonProxy;
import net.minecraftforge.common.config.Configuration;
import notreepunching.NoTreePunching;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraftforge.common.config.Config.Comment;


@net.minecraftforge.common.config.Config(modid=NoTreePunching.MODID)
public class Config {

    public static VanillaTweaks tweaks;
    public static class VanillaTweaks {

        @Comment({"Disable wooden tool recipes"})
        public static boolean WOOD_TOOLS_DISABLE = true;
        @Comment({"Disable stone tool recipes"})
        public static boolean STONE_TOOLS_DISABLE = true;

        @Comment({"Furnace requires coal or charcoal"})
        public static boolean ALTERNATE_FURNACE_RECIPE = false;

        @Comment({"Should Prospectus create tools from Thermal Foundation metals?"})
        public static String[] BREAKABLE = new String[] {"notreepunching:loose_rock","minecraft:leaves"};
    }

}