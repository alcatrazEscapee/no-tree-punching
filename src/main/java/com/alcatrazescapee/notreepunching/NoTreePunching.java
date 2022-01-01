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
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.alcatrazescapee.notreepunching.client.ClientEventHandler;
import com.alcatrazescapee.notreepunching.client.ModSounds;
import com.alcatrazescapee.notreepunching.common.blocks.ModBlocks;
import com.alcatrazescapee.notreepunching.common.container.ModContainers;
import com.alcatrazescapee.notreepunching.common.items.ClayToolItem;
import com.alcatrazescapee.notreepunching.common.items.ModItems;
import com.alcatrazescapee.notreepunching.common.recipes.ModRecipes;
import com.alcatrazescapee.notreepunching.common.tileentity.ModTileEntities;
import com.alcatrazescapee.notreepunching.util.HarvestBlockHandler;
import com.alcatrazescapee.notreepunching.world.ModFeatures;

@Mod(value = NoTreePunching.MOD_ID)
public final class NoTreePunching
{
    public static final String MOD_ID = "notreepunching";

    private static final Logger LOGGER = LogManager.getLogger();

    public NoTreePunching()
    {
        LOGGER.info("I tried to punch tree. It didn't work, my hands are now covered in blood and splinters. Need to try something else ...");

        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setup);

        ModBlocks.BLOCKS.register(bus);
        ModItems.ITEMS.register(bus);
        ModTileEntities.TILE_ENTITIES.register(bus);
        ModContainers.CONTAINERS.register(bus);
        ModFeatures.FEATURES.register(bus);
        ModSounds.SOUNDS.register(bus);
        ModRecipes.RECIPE_SERIALIZERS.register(bus);

        Config.init();
        ForgeEventHandler.init();
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientEventHandler::init);
    }

    public void setup(FMLCommonSetupEvent event)
    {
        LOGGER.info("Setup");

        HarvestBlockHandler.setup();
        ModFeatures.setup();

        event.enqueueWork(() -> {
            DispenserBlock.registerBehavior(ModItems.CLAY_TOOL.get(), (context, stack) -> {
                BlockPos offsetPos = context.getPos().relative(context.getBlockState().getValue(DispenserBlock.FACING));
                return ClayToolItem.interactWithBlock(context.getLevel(), offsetPos, context.getLevel().getBlockState(offsetPos), null, null, stack);
            });
        });
    }
}