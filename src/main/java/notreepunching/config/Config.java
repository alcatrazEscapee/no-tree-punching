package notreepunching.config;

import notreepunching.NoTreePunching;

import net.minecraftforge.common.config.Config.Comment;


@net.minecraftforge.common.config.Config(modid=NoTreePunching.MODID)
public class Config {

    public static VanillaTweaks tweaks;
    public static class VanillaTweaks {

        @Comment({"Disable wooden tool recipes"})
        public static boolean WOOD_TOOLS_DISABLE = true;
        @Comment({"Disable stone tool recipes"})
        public static boolean STONE_TOOLS_DISABLE = true;

        @Comment({"Disable Log -> Planks and Planks -> Sticks recipe. Forces use of the crude axe + saw"})
        public static boolean WOOD_RECIPE_DISABLE = true;

        @Comment({"List of blocks that always will drop items and will mine at a regular speed."})
        public static String[] BREAKABLE = new String[] {"notreepunching:loose_rock","minecraft:leaves","minecraft:gravel"};

        @Comment({"Stone Blocks (Stone, Andesite, Granite, Diorite) break into small rocks when mined, must be crafted back into cobblestone"})
        public static boolean STONE_DROPS_ROCKS = true;

        @Comment({"Quark Stone Blocks (Marble, Limestone) break into small rocks when mined, must be crafted back into cobblestone"})
        public static boolean QUARK_STONE_REPLACE = true;

        @Comment({"Rustic Stone (Slate) breaks into small rocks when mined, must be crafted back into cobblestone"})
        public static boolean RUSTIC_STONE_REPLACE = true;

    }

    public static Tools tools;
    public static class Tools {
        @Comment({"Mining level for flint tools"})
        public static int FLINT_MINING_LEVEL = 1;

        @Comment({"Mining level for copper tools"})
        public static int COPPER_MINING_LEVEL = 1;

        @Comment({"Mining level for bronze tools"})
        public static int BRONZE_MINING_LEVEL = 2;

        @Comment({"Mining level for steel tools"})
        public static int STEEL_MINING_LEVEL = 3;
    }

    public static Firepit firepit;
    public static class Firepit {
        @Comment({"Multiplier for how long fuel lasts in a firepit vs a furnace"})
        public static int FUEL_MULT = 10;

        @Comment({"How long (in ticks) food takes to cook in the firepit for recipes added by furnace. (Default Value)"})
        public static int COOK_MULT = 400;

        @Comment({"Maximum burn time (in ticks) for fuel that is allowed in the firepit (Coal = 1600, Log = 300)"})
        public static int FUEL_MAX = 800;
    }

    public static Balance balance;
    public static class Balance {
        @Comment({"Chance for a sucessful flint knapping"})
        public static double FLINT_CHANCE = 0.6D;

        @Comment({"Chance for the firestarter to set a fire. Set to 0 to disable"})
        public static double FIRE_CHANCE = 0.5D;

        @Comment({"Chance for tall grass to drop plant fibers"})
        public static double GRASS_FIBER_CHANCE = 0.7D;

    }

    public static Forge forge;
    public static class Forge {
        @Comment({"Disable ore to ingot recipes in the furnace that can be made in a forge"})
        public static boolean disableMetalSmelting = true;
    }

}