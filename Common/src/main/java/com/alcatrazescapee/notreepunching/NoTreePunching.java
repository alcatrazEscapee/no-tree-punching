package com.alcatrazescapee.notreepunching;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.DispenserBlock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alcatrazescapee.notreepunching.common.blockentity.ModBlockEntities;
import com.alcatrazescapee.notreepunching.common.blocks.ModBlocks;
import com.alcatrazescapee.notreepunching.common.container.ModContainers;
import com.alcatrazescapee.notreepunching.common.items.ClayToolItem;
import com.alcatrazescapee.notreepunching.common.items.ModItems;
import com.alcatrazescapee.notreepunching.common.recipes.ModRecipes;
import com.alcatrazescapee.notreepunching.util.HarvestBlockHandler;
import com.alcatrazescapee.notreepunching.world.ModFeatures;

public final class NoTreePunching
{
    public static final String MOD_ID = "notreepunching";

    private static final Logger LOGGER = LogManager.getLogger();

    public static void earlySetup()
    {
        LOGGER.info("I tried to punch tree. It didn't work, my hands are now covered in blood and splinters. Need to try something else ...");

        ModBlocks.BLOCKS.earlySetup();
        ModItems.ITEMS.earlySetup();
        ModBlockEntities.BLOCK_ENTITIES.earlySetup();
        ModContainers.CONTAINERS.earlySetup();
        ModRecipes.RECIPE_SERIALIZERS.earlySetup();
        ModRecipes.RECIPE_TYPES.earlySetup();

        ModFeatures.FEATURES.earlySetup();
        ModFeatures.CONFIGURED_FEATURES.earlySetup();
        ModFeatures.PLACED_FEATURES.earlySetup();

        Config.INSTANCE.earlySetup();
    }

    public static void lateSetup()
    {
        ModBlocks.BLOCKS.lateSetup();
        ModItems.ITEMS.lateSetup();
        ModBlockEntities.BLOCK_ENTITIES.lateSetup();
        ModContainers.CONTAINERS.lateSetup();
        ModRecipes.RECIPE_SERIALIZERS.lateSetup();
        ModRecipes.RECIPE_TYPES.lateSetup();

        ModFeatures.FEATURES.lateSetup();
        ModFeatures.CONFIGURED_FEATURES.lateSetup();
        ModFeatures.PLACED_FEATURES.lateSetup();

        HarvestBlockHandler.setup();

        DispenserBlock.registerBehavior(ModItems.CLAY_TOOL.get(), (context, stack) -> {
            BlockPos offsetPos = context.getPos().relative(context.getBlockState().getValue(DispenserBlock.FACING));
            return ClayToolItem.interactWithBlock(context.getLevel(), offsetPos, context.getLevel().getBlockState(offsetPos), null, null, stack);
        });
    }
}