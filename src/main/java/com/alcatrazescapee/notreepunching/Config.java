/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching;

import org.apache.commons.lang3.tuple.Pair;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

public final class Config
{
    public static final ServerConfig SERVER;
    public static final CommonConfig COMMON;

    private static final ForgeConfigSpec SERVER_SPEC;
    private static final ForgeConfigSpec COMMON_SPEC;

    static
    {
        Pair<ServerConfig, ForgeConfigSpec> serverPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
        Pair<CommonConfig, ForgeConfigSpec> commonPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);

        SERVER_SPEC = serverPair.getRight();
        COMMON_SPEC = commonPair.getRight();

        SERVER = serverPair.getLeft();
        COMMON = commonPair.getLeft();
    }

    public static void register()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC);
    }

    public static final class ServerConfig
    {
        public final ForgeConfigSpec.BooleanValue noMiningWithoutCorrectTool;
        public final ForgeConfigSpec.BooleanValue noBlockDropsWithoutCorrectTool;
        public final ForgeConfigSpec.DoubleValue flintKnappingConsumeChance;
        public final ForgeConfigSpec.DoubleValue flintKnappingSuccessChance;
        public final ForgeConfigSpec.DoubleValue fireStarterFireStartChance;

        private ServerConfig(ForgeConfigSpec.Builder builder)
        {
            noMiningWithoutCorrectTool = builder.comment("Makes blocks take forever to mine if using the wrong tool").define("noMiningWithoutCorrectTool", true);
            noBlockDropsWithoutCorrectTool = builder.comment("Makes blocks not drop anything when broken with the wrong tool").define("noBlockDropsWithoutCorrectTool", true);

            flintKnappingConsumeChance = builder.comment("The chance to consume a piece of flint when knapping").defineInRange("flintKnappingConsumeChance", 0.4, 0, 1);
            flintKnappingSuccessChance = builder.comment("The chance to produce flint shards if a piece of flint has been consumed while knapping").defineInRange("flintKnappingSuccessChance", 0.7, 0, 1);

            fireStarterFireStartChance = builder.comment("The chance for a fire starter to start fires").defineInRange("fireStarterFireStartChance", 0.3, 0, 1);
        }
    }

    public static final class CommonConfig
    {

        private CommonConfig(ForgeConfigSpec.Builder builder)
        {
        }
    }
}
