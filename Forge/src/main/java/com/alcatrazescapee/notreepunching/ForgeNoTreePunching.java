package com.alcatrazescapee.notreepunching;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

import com.alcatrazescapee.notreepunching.client.ForgeNoTreePunchingClient;
import com.alcatrazescapee.notreepunching.util.HarvestBlockHandler;
import com.alcatrazescapee.notreepunching.util.inventory.ForgeInventoryCapabilities;

@Mod(value = NoTreePunching.MOD_ID)
public final class ForgeNoTreePunching
{
    public ForgeNoTreePunching()
    {
        NoTreePunching.earlySetup();
        FMLJavaModLoadingContext.get().getModEventBus().addListener((FMLCommonSetupEvent event) -> event.enqueueWork(NoTreePunching::lateSetup));

        MinecraftForge.EVENT_BUS.addListener((PlayerInteractEvent.RightClickBlock event) -> {
            final InteractionResult result = EventHandler.onRightClickBlock(event.getLevel(), event.getPos(), event.getEntity(), event.getHand(), event.getItemStack(), event.getFace());
            if (result != null)
            {
                event.setCanceled(true);
                event.setCancellationResult(result);
            }
        });

        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOW, (PlayerEvent.BreakSpeed event) -> event.setNewSpeed(EventHandler.modifyBreakSpeed(event.getEntity(), event.getState(), event.getPos(), event.getNewSpeed())));
        MinecraftForge.EVENT_BUS.addListener((PlayerEvent.HarvestCheck event) -> event.setCanHarvest(EventHandler.modifyHarvestCheck(event.getEntity(), event.getTargetBlock(), null, event.canHarvest())));
        MinecraftForge.EVENT_BUS.addListener((TagsUpdatedEvent event) -> HarvestBlockHandler.inferUniqueToolTags());
        MinecraftForge.EVENT_BUS.addListener((RegisterCommandsEvent event) -> EventHandler.registerCommands(event.getDispatcher()));

        MinecraftForge.EVENT_BUS.addGenericListener(ItemStack.class, ForgeInventoryCapabilities::attachItemStackCapabilities);
        MinecraftForge.EVENT_BUS.addGenericListener(BlockEntity.class, ForgeInventoryCapabilities::attachBlockEntityCapabilities);

        if (FMLEnvironment.dist == Dist.CLIENT)
        {
            ForgeNoTreePunchingClient.clientSetup();
        }
    }
}
