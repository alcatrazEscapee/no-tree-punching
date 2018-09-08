/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package notreepunching;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static notreepunching.NoTreePunching.MOD_ID;

@Config(modid = MOD_ID, category = "")
@Mod.EventBusSubscriber(modid = MOD_ID)
@SuppressWarnings("WeakerAccess")
public class ModConfig
{
    public static final GeneralConfig GENERAL = new GeneralConfig();

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
}
