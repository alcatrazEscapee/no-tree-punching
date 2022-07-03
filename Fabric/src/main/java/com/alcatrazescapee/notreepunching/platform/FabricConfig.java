package com.alcatrazescapee.notreepunching.platform;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import com.mojang.logging.LogUtils;
import io.github.fablabsmc.fablabs.api.fiber.v1.builder.ConfigLeafBuilder;
import io.github.fablabsmc.fablabs.api.fiber.v1.builder.ConfigTreeBuilder;
import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigType;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ListConfigType;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import com.alcatrazescapee.notreepunching.Config;
import com.alcatrazescapee.notreepunching.NoTreePunching;

import static io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes.BOOLEAN;
import static io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes.DOUBLE;
import static io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes.STRING;
import static io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes.makeList;

public final class FabricConfig
{
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final ListConfigType<List<String>, String> STRING_LIST = makeList(STRING);

    public static Config create()
    {
        try
        {
            Files.createDirectory(Paths.get("config"));
        }
        catch (FileAlreadyExistsException ignored) {}
        catch (IOException e)
        {
            LOGGER.warn("Failed to make config directory", e);
        }

        final JanksonValueSerializer serializer = new JanksonValueSerializer(false);
        final boolean dedicatedClient = XPlatform.INSTANCE.isDedicatedClient();
        final ConfigTreeBuilder common = ConfigTree.builder();
        final ConfigTreeBuilder server = ConfigTree.builder();

        final Config config = new Config()
        {

            @Override
            protected BooleanValue build(Type configType, String name, boolean defaultValue, String comment)
            {
                return builder(configType, name, b -> b.beginValue(name, BOOLEAN, defaultValue).withComment(comment), BOOLEAN)::get;
            }

            @Override
            protected DoubleValue build(Type configType, String name, double defaultValue, double minValue, double maxValue, String comment)
            {
                return builder(configType, name, b -> b.beginValue(name, DOUBLE.withMinimum(minValue).withMaximum(maxValue), defaultValue), DOUBLE)::get;
            }

            @Override
            protected ListValue<String> build(Type configType, String name, List<String> defaultValue, String comment)
            {
                return builder(configType, name, b -> b.beginValue(name, STRING_LIST, defaultValue).withComment(comment), STRING_LIST)::get;
            }

            private <T> Supplier<T> builder(Type configType, String name, Function<ConfigTreeBuilder, ConfigLeafBuilder<?, ?>> factory, ConfigType<T, ?, ?> converter)
            {
                final ConfigTreeBuilder builder = configType == Type.COMMON ? common : server;
                final PropertyMirror<T> mirror = PropertyMirror.create(converter);
                factory.apply(builder).finishValue(mirror::mirror);
                return mirror::getValue;
            }
        };

        setupConfig(common, Paths.get("config", NoTreePunching.MOD_ID + "-common.json5"), serializer);
        if (dedicatedClient)
        {
            setupConfig(server, Paths.get("config", NoTreePunching.MOD_ID + "-server.json5"), serializer);
        }

        return config;
    }

    private static void setupConfig(ConfigTree config, Path p, JanksonValueSerializer serializer)
    {
        writeDefaultConfig(config, p, serializer);
        try (InputStream s = new BufferedInputStream(Files.newInputStream(p, StandardOpenOption.READ, StandardOpenOption.CREATE)))
        {
            FiberSerialization.deserialize(config, s, serializer);
        }
        catch (IOException | ValueDeserializationException e)
        {
            LOGGER.error("Error loading config from {}: {}", p, e);
        }
    }

    private static void writeDefaultConfig(ConfigTree config, Path path, JanksonValueSerializer serializer)
    {
        try (OutputStream s = new BufferedOutputStream(Files.newOutputStream(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW)))
        {
            FiberSerialization.serialize(config, s, serializer);
        }
        catch (FileAlreadyExistsException ignored) {}
        catch (IOException e)
        {
            LOGGER.error("Error writing default config", e);
        }
    }
}
