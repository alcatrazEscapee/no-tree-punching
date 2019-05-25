/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching;

import net.minecraftforge.common.config.Config;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

@Config(modid = MOD_ID, category = "")
@SuppressWarnings("WeakerAccess")
public final class ModConfig
{
    public static final GeneralConfig GENERAL = new GeneralConfig();
    public static final BalanceConfig BALANCE = new BalanceConfig();
    public static final ToolsConfig TOOLS = new ToolsConfig();
    public static final CompatConfig COMPAT = new CompatConfig();

    public static class GeneralConfig
    {
        @Config.Comment({"Blocks that are always breakable. Use the format modid:registryname or modid:registryname:metadata", "Note that this will not make blocks drop if they wouldn't normally drop when broken with fists."})
        public String[] alwaysBreakable = new String[] {"minecraft:leaves", "minecraft:gravel", "minecraft:sand", "minecraft:dirt", "minecraft:grass"};

        @Config.Comment("Fluids that are able to be picked up by the ceramic bucket")
        public String[] ceramicBucketValidFluids = new String[] {"water"};

        @Config.RequiresMcRestart
        @Config.Comment({"If true, NTP will add its own log->plank and plank->stick recipes and remove old versions",
                "If false, NTP will still add its own recipes, but it will not remove any other vanilla or modded recipes"})
        public boolean replaceLogRecipes = true;

        @Config.RequiresMcRestart
        @Config.Comment({"If true, NTP will remove recipes for vanilla wooden + stone tools. It will also attempt to hide these items from the creative inventory.",
                "If false, NTP will not add or remove any vanilla recipes"})
        public boolean replaceVanillaRecipes = true;

        @Config.Comment("If false, this will disable all changes to breaking speed + block drops.")
        public boolean enableBreakingChanges = true;

        @Config.Comment("If false, this will disable all changes to Stone / Cobblestone drops.")
        public boolean enableStoneDropChanges = true;

        @Config.Comment("If false, this will disable stone variants (andesite, diorite, and granite) from appearing, except if the respective stone is found during world gen. (It will only disable random occurrences, not ones based on the underground material.)")
        public boolean enableRandomStoneWorldGenVariants = false;

        @Config.Comment("If false, this will disable NTP world gen (surface rocks).")
        public boolean looseRocksGeneration = true;

        @Config.Comment("Frequency of loose rocks in the world")
        @Config.RangeInt(min = 1, max = 1000)
        public int looseRocksFrequency = 10;

        private GeneralConfig() {}
    }

    public static class BalanceConfig
    {
        @Config.Comment("Chance for a flint knapping to occur. Set to zero to disable flint knapping.")
        @Config.RangeDouble(min = 0, max = 1)
        public double flintKnappingChance = 0.6;

        @Config.Comment("Chance for a successful flint knapping")
        @Config.RangeDouble(min = 0, max = 1)
        public double flintKnappingSuccessChance = 0.7;

        @Config.Comment("Chance for a log chopping to occur")
        @Config.RangeDouble(min = 0, max = 1)
        public double logChoppingChance = 0.6;

        @Config.Comment("Fire pit cook time (in ticks). Furnace is 200 ticks")
        @Config.RangeInt(min = 20)
        public int firePitCookTime = 400;

        @Config.Comment("Fire pit fuel efficiency multiplier as compared to the furnace")
        @Config.RangeInt(min = 1)
        public int firePitFuelMultiplier = 5;

        @Config.Comment("The maximum burn amount required for a fire pit fuel. (in ticks, coal = 1600)")
        @Config.RangeInt(min = 20)
        public int firePitFuelMaxAmount = 800;

        @Config.Comment("The chance that a fire starter will start a fire")
        @Config.RangeDouble(min = 0, max = 1)
        public double fireStarterFireStartChance = 0.5;

        @Config.Comment("The chance for leaves to drop sticks when broken")
        @Config.RangeDouble(min = 0, max = 1)
        public double leavesStickDropChance = 0.2;

        @Config.Comment("The chance for tall grass to drop plant fiber when broken with a knife")
        @Config.RangeDouble(min = 0, max = 1)
        public double tallGrassDropPlantFiberChance = 0.4;

        @Config.Comment("Can the player pick up rocks by right clicking? (vs. having to mine them)")
        public boolean canPickUpRocks = true;

        private BalanceConfig() {}
    }

    public static class ToolsConfig
    {
        @Config.Comment("Mining level of flint tools. 0 = Wood, 1 = Stone, 2 = Iron, 3 = Diamond")
        @Config.RangeInt(min = 0, max = 4)
        @Config.RequiresMcRestart
        public int miningLevelFlint = 0;

        @Config.Comment("Mining level of tin tools. 0 = Wood, 1 = Stone, 2 = Iron, 3 = Diamond")
        @Config.RangeInt(min = 0, max = 4)
        @Config.RequiresMcRestart
        public int miningLevelTin = 0;

        @Config.Comment("Mining level of copper tools. 0 = Wood, 1 = Stone, 2 = Iron, 3 = Diamond")
        @Config.RangeInt(min = 0, max = 4)
        @Config.RequiresMcRestart
        public int miningLevelCopper = 1;

        @Config.Comment("Mining level of bronze tools. 0 = Wood, 1 = Stone, 2 = Iron, 3 = Diamond")
        @Config.RangeInt(min = 0, max = 4)
        @Config.RequiresMcRestart
        public int miningLevelBronze = 2;

        @Config.Comment("Mining level of steel tools. 0 = Wood, 1 = Stone, 2 = Iron, 3 = Diamond")
        @Config.RangeInt(min = 0, max = 4)
        @Config.RequiresMcRestart
        public int miningLevelSteel = 3;

        @Config.Comment("Enable copper versions of NTP tools (knife, mattock, saw)")
        @Config.RequiresMcRestart
        public boolean enableCopperTools = true;

        @Config.Comment("Enable tin versions of NTP tools (knife, mattock, saw)")
        @Config.RequiresMcRestart
        public boolean enableTinTools = true;

        @Config.Comment("Enable bronze versions of NTP tools (knife, mattock, saw)")
        @Config.RequiresMcRestart
        public boolean enableBronzeTools = true;

        @Config.Comment("Enable steel versions of NTP tools (knife, mattock, saw)")
        @Config.RequiresMcRestart
        public boolean enableSteelTools = true;

        private ToolsConfig() {}
    }

    public static class CompatConfig
    {
        @Config.Comment("Enable compatibility features from Rustic (slate cobblestone + loose rock)")
        @Config.RequiresMcRestart
        public boolean enableRusticCompat = true;

        @Config.Comment("Enable compatibility features from Quark (marble + limestone cobblestone and loose rock)")
        @Config.RequiresMcRestart
        public boolean enableQuarkCompat = true;

        @Config.Comment("Enable compatability features from Chisel (marble + limestone + basalt loose rocks)")
        public boolean enableChiselCompat = true;

        //@Config.Comment("Enable compatibility features from Underground Biomes Constructs (UBC) (loose rocks)")
        //@Config.RequiresMcRestart
        //public boolean enableUBCCompat = true;

        private CompatConfig() {}
    }
}
