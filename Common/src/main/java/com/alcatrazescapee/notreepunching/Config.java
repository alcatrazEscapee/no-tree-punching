package com.alcatrazescapee.notreepunching;

import java.nio.file.Path;
import java.util.List;
import com.mojang.logging.LogUtils;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.slf4j.Logger;

import com.alcatrazescapee.epsilon.EpsilonUtil;
import com.alcatrazescapee.epsilon.ParseError;
import com.alcatrazescapee.epsilon.Spec;
import com.alcatrazescapee.epsilon.SpecBuilder;
import com.alcatrazescapee.epsilon.Type;
import com.alcatrazescapee.epsilon.value.BoolValue;
import com.alcatrazescapee.epsilon.value.FloatValue;
import com.alcatrazescapee.epsilon.value.TypeValue;
import com.alcatrazescapee.notreepunching.common.blocks.ModBlocks;
import com.alcatrazescapee.notreepunching.common.blocks.PotteryBlock;
import com.alcatrazescapee.notreepunching.platform.XPlatform;

public enum Config
{
    INSTANCE;

    private static final Logger LOGGER = LogUtils.getLogger();

    public final BoolValue enableLooseRocksWorldGen;
    public final BoolValue doBlocksMineWithoutCorrectTool;
    public final BoolValue doInstantBreakBlocksMineWithoutCorrectTool;
    public final BoolValue doBlocksDropWithoutCorrectTool;
    public final BoolValue doInstantBreakBlocksDropWithoutCorrectTool;
    public final BoolValue doInstantBreakBlocksDamageKnives;

    public final FloatValue flintKnappingConsumeChance;
    public final FloatValue flintKnappingSuccessChance;
    public final FloatValue fireStarterFireStartChance;
    public final BoolValue fireStarterCanMakeCampfire;
    public final BoolValue fireStarterCanMakeSoulCampfire;
    public final BoolValue largeVesselKeepsContentsWhenBroken;
    public final TypeValue<List<Block>> potteryBlockSequences;

    private final Spec spec;

    Config()
    {
        final SpecBuilder builder = Spec.builder();

        enableLooseRocksWorldGen = builder
            .push("worldgen")
            .comment("Enables loose rock world gen added automatically to biomes.")
            .define("enableLooseRocksWorldGen", true);

        doBlocksMineWithoutCorrectTool = builder
            .swap("blockHarvesting")
            .comment("If blocks are mineable without the correct tool.")
            .define("doBlocksMineWithoutCorrectTool", false);
        doBlocksDropWithoutCorrectTool = builder
            .comment("If blocks drop their items without the correct tool.")
            .define("doBlocksDropWithoutCorrectTool", false);

        doInstantBreakBlocksDropWithoutCorrectTool = builder
            .comment("If blocks that break instantly are mineable without the correct tool.")
            .define("doInstantBreakBlocksDropWithoutCorrectTool", false);
        doInstantBreakBlocksMineWithoutCorrectTool = builder
            .comment("If blocks that break instantly drop their items without the correct tool.")
            .define("doInstantBreakBlocksMineWithoutCorrectTool", true);

        doInstantBreakBlocksDamageKnives = builder
            .comment("If blocks such as tall grass which break instantly consume durability when broken with a knife (only affects No Tree Punching knives)")
            .define("doInstantBreakBlocksDamageKnives", true);

        flintKnappingConsumeChance = builder
            .swap("balance")
            .comment("The chance to consume a piece of flint when knapping")
            .define("flintKnappingConsumeChance", 0.4f, 0f, 1f);
        flintKnappingSuccessChance = builder
            .comment("The chance to produce flint shards if a piece of flint has been consumed while knapping")
            .define("flintKnappingSuccessChance", 0.7f, 0f, 1f);

        fireStarterFireStartChance = builder
            .comment("The chance for a fire starter to start fires")
            .define("fireStarterFireStartChance", 0.3f, 0f, 1f);
        fireStarterCanMakeCampfire = builder
            .comment("If the fire starter can be used to make a campfire (with one '#notreepunching:fire_starter_logs' and three '#notreepunching:fire_starter_kindling'")
            .define("fireStarterCanMakeCampfire", true);
        fireStarterCanMakeSoulCampfire = builder
            .comment("If the fire starter can be used to make a soul campfire (with one '#notreepunching:fire_starter_logs', three '#notreepunching:fire_starter_kindling', and one '#notreepunching:fire_starter_soul_fire_catalyst'")
            .define("fireStarterCanMakeSoulCampfire", true);

        largeVesselKeepsContentsWhenBroken = builder
            .comment("If the large ceramic vessel block keeps it's contents when broken (as opposed to dropping them on the ground")
            .define("largeVesselKeepsContentsWhenBroken", true);

        potteryBlockSequences = builder
            .comment(
                "The sequence of blocks that can be created with the clay tool.",
                "When the clay tool is used, if the block is present in this list, it may be converted to the next block in the list.",
                "If the next block is minecraft:air, the block will be destroyed (the clay tool will never try and convert air into something)"
            )
            .define("potteryBlockSequences", List.of(
                Blocks.CLAY,
                ModBlocks.POTTERY.get(PotteryBlock.Variant.WORKED).get(),
                ModBlocks.POTTERY.get(PotteryBlock.Variant.LARGE_VESSEL).get(),
                ModBlocks.POTTERY.get(PotteryBlock.Variant.SMALL_VESSEL).get(),
                ModBlocks.POTTERY.get(PotteryBlock.Variant.BUCKET).get(),
                ModBlocks.POTTERY.get(PotteryBlock.Variant.FLOWER_POT).get(),
                Blocks.AIR
            ), Type.STRING_LIST.map(
                list -> list.stream().map(name -> ParseError.requireNotNull(() -> BuiltInRegistries.BLOCK.getOptional(new ResourceLocation(name)).orElse(null), "Invalid block: '%s'".formatted(name))).toList(),
                list -> list.stream().map(block -> BuiltInRegistries.BLOCK.getKey(block).toString()).toList(),
                TypeValue::new
            ));

        spec = builder
            .pop()
            .build();
    }

    public void load()
    {
        LOGGER.info("Loading NoTreePunching Config");
        final Path path = XPlatform.INSTANCE.configPath();
        EpsilonUtil.parse(INSTANCE.spec, Path.of(path.toString(), NoTreePunching.MOD_ID + ".toml"), LOGGER::warn);
        LOGGER.info("Loaded NoTreePunching Config");
    }
}