/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching;

import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public final class Config
{
    public static final CommonConfig COMMON = register(ModConfig.Type.COMMON, CommonConfig::new);
    public static final ServerConfig SERVER = register(ModConfig.Type.SERVER, ServerConfig::new);

    public static void init() {}

    private static <T> T register(ModConfig.Type type, Function<ForgeConfigSpec.Builder, T> factory)
    {
        Pair<T, ForgeConfigSpec> configPair = new ForgeConfigSpec.Builder().configure(factory);
        ModLoadingContext.get().registerConfig(type, configPair.getRight());
        return configPair.getLeft();
    }

    public static final class CommonConfig
    {
        public final ForgeConfigSpec.BooleanValue enableLooseRocksWorldGen;

        private CommonConfig(ForgeConfigSpec.Builder builder)
        {
            enableLooseRocksWorldGen = builder.comment("Enables loose rock world gen added automatically to biomes.").define("enableLooseRocksWorldGen", true);
        }
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
}