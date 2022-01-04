/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;

import com.alcatrazescapee.notreepunching.client.ModSounds;
import com.alcatrazescapee.notreepunching.common.ModTags;
import com.alcatrazescapee.notreepunching.common.items.ModItems;
import com.alcatrazescapee.notreepunching.util.HarvestBlockHandler;
import com.alcatrazescapee.notreepunching.world.ModFeatures;

public final class ForgeEventHandler
{
    private static final Set<Biome.BiomeCategory> CATEGORIES_WITHOUT_ROCKS = new HashSet<>(Arrays.asList(Biome.BiomeCategory.NONE, Biome.BiomeCategory.THEEND, Biome.BiomeCategory.NETHER, Biome.BiomeCategory.OCEAN));

    public static void init()
    {
        final IEventBus bus = MinecraftForge.EVENT_BUS;

        bus.addListener(ForgeEventHandler::onHarvestCheck);
        bus.addListener(EventPriority.LOW, ForgeEventHandler::onBreakSpeed);
        bus.addListener(ForgeEventHandler::onRightClickBlock);
        bus.addListener(ForgeEventHandler::onBiomeLoad);
    }

    public static void onHarvestCheck(PlayerEvent.HarvestCheck event)
    {
        event.setCanHarvest(event.canHarvest() || HarvestBlockHandler.isUsingCorrectToolForDrops(event.getTargetBlock(), event.getPlayer()));
    }

    public static void onBreakSpeed(PlayerEvent.BreakSpeed event)
    {
        if (!HarvestBlockHandler.isUsingCorrectToolToMine(event.getState(), event.getPlayer()))
        {
            event.setNewSpeed(0);
        }
    }

    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event)
    {
        final ItemStack stack = event.getItemStack();
        final Level level = event.getWorld();
        final BlockPos pos = event.getPos();
        final BlockState state = level.getBlockState(pos);
        if (ModTags.Items.FLINT_KNAPPABLE.contains(stack.getItem()) && state.getMaterial() == Material.STONE)
        {
            if (!level.isClientSide)
            {
                if (level.random.nextFloat() < Config.SERVER.flintKnappingConsumeChance.get())
                {
                    if (level.random.nextFloat() < Config.SERVER.flintKnappingSuccessChance.get())
                    {
                        Direction face = event.getFace() == null ? Direction.UP : event.getFace();
                        Containers.dropItemStack(level, pos.getX() + 0.5 + face.getStepX() * 0.5, pos.getY() + 0.5 + face.getStepY() * 0.5, pos.getZ() + 0.5 + face.getStepZ() * 0.5, new ItemStack(ModItems.FLINT_SHARD.get(), 2));
                    }
                    stack.shrink(1);
                    event.getPlayer().setItemInHand(event.getHand(), stack);
                }
                level.playSound(null, pos, ModSounds.KNAPPING.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }
    }

    public static void onBiomeLoad(BiomeLoadingEvent event)
    {
        if (Config.COMMON.enableLooseRocksWorldGen.get() && !CATEGORIES_WITHOUT_ROCKS.contains(event.getCategory()))
        {
            event.getGeneration().addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, ModFeatures.LOOSE_ROCKS_PLACED.get());
        }
    }
}