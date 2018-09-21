/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.alcatrazescapee.notreepunching.util.HarvestBlockHandler;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

@Config(modid = MOD_ID, category = "")
@Mod.EventBusSubscriber(modid = MOD_ID)
@SuppressWarnings("WeakerAccess")
public class ModConfig
{
    public static final GeneralConfig GENERAL = new GeneralConfig();
    public static final ToolsConfig TOOLS = new ToolsConfig();

    @SubscribeEvent
    public static void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equals(MOD_ID))
        {
            ConfigManager.sync(MOD_ID, Config.Type.INSTANCE);
            HarvestBlockHandler.reloadWhitelist();
        }
    }

    public static class GeneralConfig
    {
        @Config.Comment("Chance for a flint knapping to occur")
        @Config.RangeDouble(min = 0, max = 1)
        public double flintKnappingChance = 0.6;

        @Config.Comment("Chance for a sucessful flint knapping")
        @Config.RangeDouble(min = 0, max = 1)
        public double flintKnappingSuccessChance = 0.7;

        @Config.Comment("Chance for a log chopping to occur")
        @Config.RangeDouble(min = 0, max = 1)
        public double logChoppingChance = 0.6;

        @Config.Comment("Blocks that are always breakable. Use the format modid:registryname or modid:registryname:metadata")
        public String[] alwaysBreakable = new String[] {"minecraft:leaves", "minecraft:gravel", "minecraft:sand", "minecraft:dirt", "minecraft:grass"};

        @Config.Comment({"If true, NTP will add its own log->plank and plank->stick recipes and remove old versions",
                "If false, NTP will still add its own recipes, but it will not remove any other vanilla or modded recipes"})
        public boolean replaceLogRecipes = true;

        @Config.Comment({"If true, NTP will replace certain vanilla recipes with ones that use some NTP items",
                "If false, NTP will not add or remove any vanilla recipes"})
        public boolean replaceVanillaRecipes = true;

        @Config.Comment("If false, this will disable all changes to breaking speed + block drops.")
        public boolean enableBreakingChanges = true;

        @Config.Comment("If false, this will disable NTP worldgen (surface rocks).")
        public boolean looseRocksGeneration = true;

        @Config.Comment("Frequence of loose rocks in the world")
        public int looseRocksFrequency = 10;

        private GeneralConfig() {}
    }

    public static class ToolsConfig
    {
        @Config.Comment("Mining level of flint tools. 0 = Wood, 1 = Stone, 2 = Iron, 3 = Diamond")
        @Config.RangeInt(min = 0, max = 4)
        public int miningLevelFlint = 0;

        @Config.Comment("Mining level of tin tools. 0 = Wood, 1 = Stone, 2 = Iron, 3 = Diamond")
        @Config.RangeInt(min = 0, max = 4)
        public int miningLevelTin = 0;

        @Config.Comment("Mining level of copper tools. 0 = Wood, 1 = Stone, 2 = Iron, 3 = Diamond")
        @Config.RangeInt(min = 0, max = 4)
        public int miningLevelCopper = 0;

        @Config.Comment("Mining level of bronze tools. 0 = Wood, 1 = Stone, 2 = Iron, 3 = Diamond")
        @Config.RangeInt(min = 0, max = 4)
        public int miningLevelBronze = 0;

        @Config.Comment("Mining level of steel tools. 0 = Wood, 1 = Stone, 2 = Iron, 3 = Diamond")
        @Config.RangeInt(min = 0, max = 4)
        public int miningLevelSteel = 0;

        @Config.Comment("Enable copper versions of NTP tools (knife, mattock, saw)")
        public boolean enableCopperTools = false;

        @Config.Comment("Enable tin versions of NTP tools (knife, mattock, saw)")
        public boolean enableTinTools = false;

        @Config.Comment("Enable bronze versions of NTP tools (knife, mattock, saw)")
        public boolean enableBronzeTools = false;

        @Config.Comment("Enable steel versions of NTP tools (knife, mattock, saw)")
        public boolean enableSteelTools = false;

        private ToolsConfig() {}
    }
}
