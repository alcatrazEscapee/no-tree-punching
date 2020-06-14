/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.ForgeRegistries;

import com.alcatrazescapee.core.util.Cache;
import com.alcatrazescapee.core.util.CoreHelpers;

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

    public static void onConfigReloading()
    {
        SERVER.largeCeramicVesselBlacklist.invalidate();
        SERVER.smallCeramicVesselBlacklist.invalidate();
    }

    public static final class ServerConfig
    {
        public final ForgeConfigSpec.BooleanValue noMiningWithoutCorrectTool;
        public final ForgeConfigSpec.BooleanValue noBlockDropsWithoutCorrectTool;
        public final ForgeConfigSpec.DoubleValue flintKnappingConsumeChance;
        public final ForgeConfigSpec.DoubleValue flintKnappingSuccessChance;
        public final ForgeConfigSpec.DoubleValue fireStarterFireStartChance;

        public final Cache<Predicate<ItemStack>> largeCeramicVesselBlacklist;
        public final Cache<Predicate<ItemStack>> smallCeramicVesselBlacklist;

        private final ForgeConfigSpec.ConfigValue<List<? extends String>> internalLargeCeramicVesselBlacklist;
        private final ForgeConfigSpec.ConfigValue<List<? extends String>> internalSmallCeramicVesselBlacklist;

        private ServerConfig(ForgeConfigSpec.Builder builder)
        {
            noMiningWithoutCorrectTool = builder.comment("Makes blocks take forever to mine if using the wrong tool").define("noMiningWithoutCorrectTool", true);
            noBlockDropsWithoutCorrectTool = builder.comment("Makes blocks not drop anything when broken with the wrong tool").define("noBlockDropsWithoutCorrectTool", true);

            flintKnappingConsumeChance = builder.comment("The chance to consume a piece of flint when knapping").defineInRange("flintKnappingConsumeChance", 0.4, 0, 1);
            flintKnappingSuccessChance = builder.comment("The chance to produce flint shards if a piece of flint has been consumed while knapping").defineInRange("flintKnappingSuccessChance", 0.7, 0, 1);

            fireStarterFireStartChance = builder.comment("The chance for a fire starter to start fires").defineInRange("fireStarterFireStartChance", 0.3, 0, 1);

            internalLargeCeramicVesselBlacklist = builder.comment("A blacklist of items that are allowed in a large ceramic vessel").defineList("largeCeramicVesselBlacklist", this::getDefaultVesselBlacklist, e -> e instanceof String);
            internalSmallCeramicVesselBlacklist = builder.comment("A blacklist of items that are allowed in a small ceramic vessel").defineList("smallCeramicVesselBlacklist", this::getDefaultVesselBlacklist, e -> e instanceof String);


            largeCeramicVesselBlacklist = Cache.of(() -> convertBlacklistToPredicate(internalLargeCeramicVesselBlacklist.get()));
            smallCeramicVesselBlacklist = Cache.of(() -> convertBlacklistToPredicate(internalSmallCeramicVesselBlacklist.get()));
        }

        private Predicate<ItemStack> convertBlacklistToPredicate(List<? extends String> blacklist)
        {
            // Build the list of predicates, then the cached only has to do item equality and tag set contains
            List<Predicate<ItemStack>> predicates = blacklist.stream()
                .map(((Function<String, Predicate<ItemStack>>)(s -> {
                ResourceLocation id = new ResourceLocation(s);
                if (ForgeRegistries.ITEMS.containsKey(id))
                {
                    Item item = ForgeRegistries.ITEMS.getValue(id);
                    return stack -> stack.getItem() == item;
                }
                else
                {
                    Tag<Item> tag = ItemTags.getCollection().get(id);
                    if (tag != null)
                    {
                        return stack -> tag.contains(stack.getItem());
                    }
                }
                LOGGER.warn("Invalid item or tag in item blacklist: {}", s);
                return null;
            }))).filter(Objects::nonNull).collect(Collectors.toList());
            return stackIn -> predicates.stream().anyMatch(predicate -> predicate.test(stackIn));
        }

        private List<String> getDefaultVesselBlacklist()
        {
            List<String> blacklist = CoreHelpers.listOf("notreepunching:ceramic_small_vessel");
            for (DyeColor color : DyeColor.values())
            {
                blacklist.add("minecraft:" + color.getName() + "_shulker_box");
            }
            return blacklist;
        }
    }

    public static final class CommonConfig
    {

        private CommonConfig(ForgeConfigSpec.Builder builder)
        {
        }
    }
}
