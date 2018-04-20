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

        @Comment({"Furnace requires coal or charcoal"})
        public static boolean ALTERNATE_FURNACE_RECIPE = false;

        @Comment({"List of blocks that always will drop. (They will still take additional time to mine based on material if not mined with the correct tool)"})
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
        @Comment({"Mining level for flint pickaxe and axe"})
        public static int FLINT_MINING_LEVEL = 0;
    }

    public static Firepit firepit;
    public static class Firepit {
        @Comment({"Multiplier for how long fuel lasts in a firepit vs a furnace"})
        public static int FUEL_MULT = 10;

        @Comment({"Multiplier for how long food items take to cook in a firepit vs a furnace"})
        public static int COOK_MULT = 2;

        @Comment({"Maximum burn time (in ticks) that is allowed in the firepit (Coal = 1600, Log = 300)"})
        public static int FUEL_MAX = 800;
    }

    public static Balance balance;
    public static class Balance {
        @Comment({"Chance for a sucessful flint knapping"})
        public static double FLINT_CHANCE = 0.4D;

        @Comment({"Chance for the firestarter to set a fire (Set to 0 to disable)"})
        public static double FIRE_CHANCE = 0.5D;
    }

}