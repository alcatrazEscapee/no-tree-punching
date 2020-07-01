/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public final class Config
{
    public static final ServerConfig SERVER;
    public static final CommonConfig COMMON;

    private static final ForgeConfigSpec SERVER_SPEC;
    private static final ForgeConfigSpec COMMON_SPEC;

    private static final Logger LOGGER = LogManager.getLogger();

    static
    {
        Pair<ServerConfig, ForgeConfigSpec> serverPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
        Pair<CommonConfig, ForgeConfigSpec> commonPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);

        SERVER_SPEC = serverPair.getRight();
        COMMON_SPEC = commonPair.getRight();

        SERVER = serverPair.getLeft();
        COMMON = commonPair.getLeft();
    }

    public static void init()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC);
    }

    public static final class ServerConfig
    {
        public final ForgeConfigSpec.BooleanValue noMiningWithoutCorrectTool;
        public final ForgeConfigSpec.BooleanValue noBlockDropsWithoutCorrectTool;
        public final ForgeConfigSpec.BooleanValue doInstantBreakBlocksRequireTool;
        public final ForgeConfigSpec.BooleanValue doInstantBreakBlocksDamageKnives;
        public final ForgeConfigSpec.DoubleValue flintKnappingConsumeChance;
        public final ForgeConfigSpec.DoubleValue flintKnappingSuccessChance;
        public final ForgeConfigSpec.DoubleValue fireStarterFireStartChance;

        private ServerConfig(ForgeConfigSpec.Builder builder)
        {
            noMiningWithoutCorrectTool = builder.comment("Makes blocks take forever to mine if using the wrong tool").define("noMiningWithoutCorrectTool", true);
            noBlockDropsWithoutCorrectTool = builder.comment("Makes blocks not drop anything when broken with the wrong tool").define("noBlockDropsWithoutCorrectTool", true);
            doInstantBreakBlocksRequireTool = builder.comment("Makes blocks that would otherwise be broken instantly unbreakable if using the wrong tool.").define("doInstantBreakBlocksRequireTool", false);
            doInstantBreakBlocksDamageKnives = builder.comment("If blocks such as tall grass which break instantly consume durability when broken with a knife (only affects No Tree Punching knives)").define("doInstantBreakBlocksDamageKnives", true);

            flintKnappingConsumeChance = builder.comment("The chance to consume a piece of flint when knapping").defineInRange("flintKnappingConsumeChance", 0.4, 0, 1);
            flintKnappingSuccessChance = builder.comment("The chance to produce flint shards if a piece of flint has been consumed while knapping").defineInRange("flintKnappingSuccessChance", 0.7, 0, 1);

            fireStarterFireStartChance = builder.comment("The chance for a fire starter to start fires").defineInRange("fireStarterFireStartChance", 0.3, 0, 1);
        }
    }

    public static final class CommonConfig
    {
        public final ForgeConfigSpec.BooleanValue enableLooseRockSpawning;
        public final ForgeConfigSpec.IntValue looseRockFrequency;

        private CommonConfig(ForgeConfigSpec.Builder builder)
        {
            enableLooseRockSpawning = builder.comment("Enable loose rock spawning. Note: this requires a MC restart to take effect.").define("enableLooseRockSpawning", true);
            looseRockFrequency = builder.comment("How common loose rocks are.  Note: this requires a MC restart to take effect.").defineInRange("looseRockFrequency", 12, 0, Integer.MAX_VALUE);
        }
    }
}
