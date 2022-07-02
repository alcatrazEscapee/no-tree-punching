package com.alcatrazescapee.notreepunching;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import com.mojang.logging.LogUtils;
import net.minecraft.ResourceLocationException;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.slf4j.Logger;

import com.alcatrazescapee.notreepunching.common.blocks.ModBlocks;
import com.alcatrazescapee.notreepunching.common.blocks.PotteryBlock;
import com.alcatrazescapee.notreepunching.platform.AbstractConfig;
import com.alcatrazescapee.notreepunching.platform.XPlatform;

public abstract class Config extends AbstractConfig
{
    public static final Config INSTANCE = XPlatform.INSTANCE.createConfig();

    private static final Logger LOGGER = LogUtils.getLogger();

    // Common
    public final BooleanValue enableLooseRocksWorldGen;

    // Server
    public final BooleanValue doBlocksMineWithoutCorrectTool;
    public final BooleanValue doInstantBreakBlocksMineWithoutCorrectTool;

    public final BooleanValue doBlocksDropWithoutCorrectTool;
    public final BooleanValue doInstantBreakBlocksDropWithoutCorrectTool;

    public final BooleanValue doInstantBreakBlocksDamageKnives;
    public final DoubleValue flintKnappingConsumeChance;
    public final DoubleValue flintKnappingSuccessChance;
    public final DoubleValue fireStarterFireStartChance;
    public final BooleanValue fireStarterCanMakeCampfire;
    public final BooleanValue fireStarterCanMakeSoulCampfire;
    public final BooleanValue largeVesselKeepsContentsWhenBroken;

    private final ListValue<String> potteryBlockSequences;

    protected Config()
    {
        // Common
        enableLooseRocksWorldGen = build(Type.COMMON, "enableLooseRocksWorldGen", true, "Enables loose rock world gen added automatically to biomes.");

        doBlocksMineWithoutCorrectTool = build(Type.SERVER, "doBlocksMineWithoutCorrectTool", false, "If blocks are mineable without the correct tool.");
        doBlocksDropWithoutCorrectTool = build(Type.SERVER, "doBlocksDropWithoutCorrectTool", false, "If blocks drop their items without the correct tool.");

        doInstantBreakBlocksDropWithoutCorrectTool = build(Type.SERVER, "doInstantBreakBlocksDropWithoutCorrectTool", false, "If blocks that break instantly are mineable without the correct tool.");
        doInstantBreakBlocksMineWithoutCorrectTool = build(Type.SERVER, "doInstantBreakBlocksMineWithoutCorrectTool", true, "If blocks that break instantly drop their items without the correct tool.");

        doInstantBreakBlocksDamageKnives = build(Type.SERVER, "doInstantBreakBlocksDamageKnives", true, "If blocks such as tall grass which break instantly consume durability when broken with a knife (only affects No Tree Punching knives)");

        flintKnappingConsumeChance = build(Type.SERVER, "flintKnappingConsumeChance", 0.4, 0, 1, "The chance to consume a piece of flint when knapping");
        flintKnappingSuccessChance = build(Type.SERVER, "flintKnappingSuccessChance", 0.7, 0, 1, "The chance to produce flint shards if a piece of flint has been consumed while knapping");

        fireStarterFireStartChance = build(Type.SERVER, "fireStarterFireStartChance", 0.3, 0, 1, "The chance for a fire starter to start fires");
        fireStarterCanMakeCampfire = build(Type.SERVER, "fireStarterCanMakeCampfire", true, "If the fire starter can be used to make a campfire (with one '#notreepunching:fire_starter_logs' and three '#notreepunching:fire_starter_kindling'");
        fireStarterCanMakeSoulCampfire = build(Type.SERVER, "fireStarterCanMakeSoulCampfire", true, "If the fire starter can be used to make a soul campfire (with one '#notreepunching:fire_starter_logs', three '#notreepunching:fire_starter_kindling', and one '#notreepunching:fire_starter_soul_fire_catalyst'");

        largeVesselKeepsContentsWhenBroken = build(Type.SERVER, "largeVesselKeepsContentsWhenBroken", true, "If the large ceramic vessel block keeps it's contents when broken (as opposed to dropping them on the ground");

        potteryBlockSequences = build(Type.SERVER, "potteryBlockSequences", defaultPotteryBlockSequence(),
            "The sequence of blocks that can be created with the clay tool. When the clay tool is used, if the block is present in this list, it may be converted to the next block in the list. If the next block is minecraft:air, the block will be destroyed (the clay tool will never try and convert air into something)");
    }

    public List<Block> getPotteryBlockSequences()
    {
        return potteryBlockSequences.get()
            .stream()
            .map(id -> {
                try
                {
                    return Registry.BLOCK.get(new ResourceLocation(id));
                }
                catch (ResourceLocationException e)
                {
                    LOGGER.warn("Illegal entry: {} in No Tree Punching config at potteryBlockSequences: {}", id, e.getMessage());
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    private List<String> defaultPotteryBlockSequence()
    {
        final List<String> values = new ArrayList<>();
        values.add("minecraft:clay");
        for (PotteryBlock.Variant pottery : PotteryBlock.Variant.values())
        {
            values.add(ModBlocks.POTTERY.get(pottery).id().toString());
        }
        values.add("minecraft:air");
        return values;
    }
}