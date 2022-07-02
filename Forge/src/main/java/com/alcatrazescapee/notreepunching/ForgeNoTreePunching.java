package com.alcatrazescapee.notreepunching;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

import com.alcatrazescapee.notreepunching.client.ForgeNoTreePunchingClient;
import com.alcatrazescapee.notreepunching.world.ModFeatures;

@Mod(value = NoTreePunching.MOD_ID)
public final class ForgeNoTreePunching
{
    public ForgeNoTreePunching()
    {
        NoTreePunching.earlySetup();
        FMLJavaModLoadingContext.get().getModEventBus().addListener((FMLCommonSetupEvent event) -> event.enqueueWork(NoTreePunching::lateSetup));

        MinecraftForge.EVENT_BUS.addListener(this::onBiomeLoad);
        MinecraftForge.EVENT_BUS.addListener((PlayerInteractEvent.RightClickBlock event) -> {
            final InteractionResult result = EventHandler.onRightClickBlock(event.getWorld(), event.getPos(), event.getPlayer(), event.getHand(), event.getItemStack(), event.getFace());
            if (result != null)
            {
                event.setCanceled(true);
                event.setCancellationResult(result);
            }
        });
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOW, (PlayerEvent.BreakSpeed event) -> EventHandler.onBreakSpeed(event.getPlayer(), event.getState(), event::setNewSpeed));
        MinecraftForge.EVENT_BUS.addListener((PlayerEvent.HarvestCheck event) -> {
            if (!event.canHarvest())
            {
                EventHandler.onHarvestCheck(event.getPlayer(), event.getTargetBlock(), event::setCanHarvest);
            }
        });

        if (FMLEnvironment.dist == Dist.CLIENT)
        {
            ForgeNoTreePunchingClient.clientSetup();
        }
    }

    private void onBiomeLoad(BiomeLoadingEvent event)
    {
        if (Config.INSTANCE.enableLooseRocksWorldGen.get() && !EventHandler.isCategoryWithoutRocks(event.getCategory()))
        {
            event.getGeneration().addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, ModFeatures.PLACED_LOOSE_ROCKS.holder());
        }
    }
}
