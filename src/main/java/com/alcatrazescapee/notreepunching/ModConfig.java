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
        }
    }

    public static class GeneralConfig
    {
        @Config.Comment("Chance for a flint knapping")
        @Config.LangKey("config." + MOD_ID + ".flintKnappingChance")
        @Config.RangeDouble(min = 0, max = 1)
        public double flintKnappingChance = 0.6;

        @Config.Comment("Chance for a sucessful flint knapping")
        @Config.LangKey("config." + MOD_ID + ".flintKnappingSuccessChance")
        @Config.RangeDouble(min = 0, max = 1)
        public double flintKnappingSuccessChance = 0.7;

        private GeneralConfig() {}

    }

    public static class ToolsConfig
    {
        @Config.Comment("Mining level of flint tools. 0 = Wood, 1 = Stone, 2 = Iron, 3 = Diamond")
        @Config.LangKey("config." + MOD_ID + ".miningLevelFlint")
        @Config.RangeInt(min = 0, max = 4)
        public int miningLevelFlint = 0;

        @Config.Comment("Mining level of tin tools. 0 = Wood, 1 = Stone, 2 = Iron, 3 = Diamond")
        @Config.LangKey("config." + MOD_ID + ".miningLevelTin")
        @Config.RangeInt(min = 0, max = 4)
        public int miningLevelTin = 0;

        @Config.Comment("Mining level of copper tools. 0 = Wood, 1 = Stone, 2 = Iron, 3 = Diamond")
        @Config.LangKey("config." + MOD_ID + ".miningLevelCopper")
        @Config.RangeInt(min = 0, max = 4)
        public int miningLevelCopper = 0;

        @Config.Comment("Mining level of bronze tools. 0 = Wood, 1 = Stone, 2 = Iron, 3 = Diamond")
        @Config.LangKey("config." + MOD_ID + ".miningLevelBronze")
        @Config.RangeInt(min = 0, max = 4)
        public int miningLevelBronze = 0;

        @Config.Comment("Mining level of steel tools. 0 = Wood, 1 = Stone, 2 = Iron, 3 = Diamond")
        @Config.LangKey("config." + MOD_ID + ".miningLevelSteel")
        @Config.RangeInt(min = 0, max = 4)
        public int miningLevelSteel = 0;

        private ToolsConfig() {}
    }
}
