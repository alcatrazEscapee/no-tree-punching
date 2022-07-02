/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.core.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

import com.alcatrazescapee.notreepunching.client.ClientEventHandler;
import com.alcatrazescapee.notreepunching.client.ModSounds;
import com.alcatrazescapee.notreepunching.common.blocks.ModBlocks;
import com.alcatrazescapee.notreepunching.common.container.ModContainers;
import com.alcatrazescapee.notreepunching.common.items.ClayToolItem;
import com.alcatrazescapee.notreepunching.common.items.ModItems;
import com.alcatrazescapee.notreepunching.common.recipes.ModRecipes;
import com.alcatrazescapee.notreepunching.common.blockentity.ModBlockEntities;
import com.alcatrazescapee.notreepunching.util.HarvestBlockHandler;
import com.alcatrazescapee.notreepunching.world.ModFeatures;

@Mod(value = NoTreePunching.MOD_ID)
public final class NoTreePunching
{
    public static final String MOD_ID = "notreepunching";

    private static final Logger LOGGER = LogManager.getLogger();

    public static void earlySetup()
    {
        ModBlocks.BLOCKS.earlySetup();
        ModItems.ITEMS.earlySetup();
        ModBlockEntities.BLOCK_ENTITIES.earlySetup();
        ModContainers.CONTAINERS.earlySetup();
        ModRecipes.RECIPE_SERIALIZERS.earlySetup();

        ModFeatures.FEATURES.earlySetup();
        ModFeatures.CONFIGURED_FEATURES.earlySetup();
        ModFeatures.PLACED_FEATURES.earlySetup();
    }

    public static void lateSetup()
    {
        ModBlocks.BLOCKS.lateSetup();
        ModItems.ITEMS.lateSetup();
        ModBlockEntities.BLOCK_ENTITIES.lateSetup();
        ModContainers.CONTAINERS.lateSetup();
        ModRecipes.RECIPE_SERIALIZERS.lateSetup();

        ModFeatures.FEATURES.lateSetup();
        ModFeatures.CONFIGURED_FEATURES.lateSetup();
        ModFeatures.PLACED_FEATURES.lateSetup();
    }

    public NoTreePunching()
    {
        LOGGER.info("I tried to punch tree. It didn't work, my hands are now covered in blood and splinters. Need to try something else ...");

        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setup);

        NoTreePunching.earlySetup();

        Config.init();
        ForgeEventHandler.init();

        if (FMLEnvironment.dist == Dist.CLIENT)
        {
            ClientEventHandler.init();
        }
    }

    public void setup(FMLCommonSetupEvent event)
    {
        LOGGER.info("Setup");

        NoTreePunching.lateSetup();
        HarvestBlockHandler.setup();

        event.enqueueWork(() -> {
            DispenserBlock.registerBehavior(ModItems.CLAY_TOOL.get(), (context, stack) -> {
                BlockPos offsetPos = context.getPos().relative(context.getBlockState().getValue(DispenserBlock.FACING));
                return ClayToolItem.interactWithBlock(context.getLevel(), offsetPos, context.getLevel().getBlockState(offsetPos), null, null, stack);
            });
        });
    }
}