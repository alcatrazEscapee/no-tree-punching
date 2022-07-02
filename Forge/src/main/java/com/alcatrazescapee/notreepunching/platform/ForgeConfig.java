package com.alcatrazescapee.notreepunching.platform;

import java.util.List;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import com.alcatrazescapee.notreepunching.Config;

public final class ForgeConfig
{
    public static Config create()
    {
        final ForgeConfigSpec.Builder common = new ForgeConfigSpec.Builder();
        final ForgeConfigSpec.Builder client = new ForgeConfigSpec.Builder();
        final Config config = new Config()
        {

            @Override
            protected BooleanValue build(Type configType, String name, boolean defaultValue, String comment)
            {
                return builder(configType, comment).define(name, defaultValue)::get;
            }

            @Override
            protected DoubleValue build(Type configType, String name, double defaultValue, double minValue, double maxValue, String comment)
            {
                return builder(configType, comment).defineInRange(name, defaultValue, minValue, maxValue)::get;
            }

            @Override
            @SuppressWarnings("unchecked")
            protected ListValue<String> build(Type configType, String name, List<String> defaultValue, String comment)
            {
                final ForgeConfigSpec.ConfigValue<List<? extends String>> value = builder(configType, comment).defineList(name, defaultValue, e -> e instanceof String);
                return () -> (List<String>) value.get();
            }

            private ForgeConfigSpec.Builder builder(Config.Type configType, String comment)
            {
                return (configType == Type.COMMON ? common : client).comment(" " + comment);
            }
        };

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, common.build());
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, client.build());
        return config;
    }
}
