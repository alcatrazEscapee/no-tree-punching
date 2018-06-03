package notreepunching.config;

import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import notreepunching.NoTreePunching;

import javax.annotation.Nonnull;

import static notreepunching.NoTreePunching.MODID;

@Mod.EventBusSubscriber
@Config(modid=MODID)
public class ModConfig {

    @Comment({"Enable Metalworking module (Copper, tin, bronze, steel, forge, blast furnace, bellows, tuyere)"})
    @Config.RequiresMcRestart
    public static boolean MODULE_METALWORKING = true;

    @Comment({"Enable Pottery Module (Clay tools, clay vessels, ceramics)"})
    @Config.RequiresMcRestart
    public static boolean MODULE_POTTERY = true;

    public static VanillaTweaks tweaks;
    public static class VanillaTweaks {

        @Comment({"Disable wooden tool recipes"})
        public static boolean WOOD_TOOLS_DISABLE = true;
        @Comment({"Disable stone tool recipes"})
        public static boolean STONE_TOOLS_DISABLE = true;

        @Comment({"Disable Log -> Planks and Planks -> Sticks recipe. Forces use of the crude axe + saw"})
        public static boolean WOOD_RECIPE_DISABLE = true;

        @Comment({"Disable gold tool recipes"})
        public static boolean GOLD_TOOLS_DISABLE = false;

        @Comment({"List of blocks that always will drop items and will mine at a regular speed.",
                "Use the format modid:registry_name"})
        public static String[] BREAKABLE = new String[] {"notreepunching:loose_rock","minecraft:leaves","minecraft:gravel"};

        @Comment({"Stone Blocks (Stone, Andesite, Granite, Diorite) break into small rocks when mined, must be crafted back into cobblestone"})
        public static boolean STONE_DROPS_ROCKS = true;

        @Comment({"Quark Stone Blocks (Marble, Limestone) break into small rocks when mined, must be crafted back into cobblestone"})
        public static boolean QUARK_STONE_REPLACE = true;

        @Comment({"Rustic Stone (Slate) breaks into small rocks when mined, must be crafted back into cobblestone"})
        public static boolean RUSTIC_STONE_REPLACE = true;

        @Comment({"Disable Furnace ore smelting recipes, forcing the use of the forge. If false, it will add furnace recipes for NTP metals"})
        public static boolean DISABLE_SMELTING_ORE = true;

        @Comment({"Alternate recipes for various blocks and items using NTP materials (generally slightly harder)"})
        public static boolean COOLER_RECIPES = true;

    }

    public static Balance balance;
    public static class Balance {
        @Comment({"Chance for a sucessful flint knapping"})
        @Config.RangeDouble(min = 0.0, max = 1.0)
        public static double FLINT_CHANCE = 0.6D;

        @Comment({"Chance for the firestarter to set a fire. Set to 0 to disable firestarter starting fires"})
        @Config.RangeDouble(min = 0.0, max = 1.0)
        public static double FIRE_CHANCE = 0.5D;

        @Comment({"Chance for tall grass to drop plant fibers"})
        @Config.RangeDouble(min = 0.0, max = 1.0)
        public static double GRASS_FIBER_CHANCE = 0.7D;

        @Comment({"Time (in ticks) for the charcoal pit to run (1000 ticks = 1 in game hour)"})
        public static int CHARCOAL_PIT_TIMER = 8000;

        @Comment({"Multiplier for how long fuel lasts in a firepit vs a furnace"})
        public static int FUEL_MULT = 10;

        @Comment({"How long (in ticks) food takes to cook in the firepit."})
        @Config.Name("COOK_TIME")
        public static int COOK_MULT = 400;

        @Comment({"Maximum burn time (in ticks) for fuel that is allowed in the firepit (Coal = 1600, Log = 300)"})
        public static int FUEL_MAX = 800;

        @Comment({"Mining level for flint tools", "0 = Wood, 1 = Stone, 2 = Iron, 3 = Diamond"})
        public static int FLINT_MINING_LEVEL = 0;

        @Comment({"Mining level for copper tools", "0 = Wood, 1 = Stone, 2 = Iron, 3 = Diamond"})
        public static int COPPER_MINING_LEVEL = 1;

        @Comment({"Mining level for bronze tools", "0 = Wood, 1 = Stone, 2 = Iron, 3 = Diamond"})
        public static int BRONZE_MINING_LEVEL = 2;

        @Comment({"Mining level for steel tools", "0 = Wood, 1 = Stone, 2 = Iron, 3 = Diamond"})
        public static int STEEL_MINING_LEVEL = 3;

        @Comment({"Amount of dust generated per ore in the grindstone"})
        public static int DUST_PER_ORE = 1;

        @Comment({"Time (in ticks) that the Blast Furnace takes to cook"})
        public static int BLAST_FURNACE_TIME = 600;

        @Comment({"Time (in ticks) that one piece of charcoal lasts in the Blast Furnace"})
        public static int BLAST_FURNACE_FUEL_TIME = 800;

    }

    public static World world;
    public static class World{

        @Comment({"Generate Copper ore in the world"})
        public static boolean COPPER_ORE = true;

        @Comment({"Generate Tin ore in the world"})
        public static boolean TIN_ORE = true;

        @Comment({"Generate Loose rocks on the surface"})
        public static boolean LOOSE_ROCKS = true;

        @Comment({"Frequency of loose rocks appearing in the world"})
        @Config.RangeInt(min = 1, max = 100)
        public static int LOOSE_ROCKS_FREQUENCY = 6;
    }

    public static Metalworking metalworking;
    public static class Metalworking{

    }

    public static Metalworking pottery;
    public static class Pottery{

    }

    @SubscribeEvent
    public static void configChanged(@Nonnull ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(MODID)) {
            ConfigManager.sync(MODID, net.minecraftforge.common.config.Config.Type.INSTANCE);
        }
    }

}