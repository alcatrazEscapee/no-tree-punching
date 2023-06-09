package com.alcatrazescapee.notreepunching;

import com.alcatrazescapee.notreepunching.common.items.ModItemGroups;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alcatrazescapee.notreepunching.client.ModSounds;
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
        ModItemGroups.TABS.earlySetup();
        ModSounds.SOUNDS.earlySetup();
        ModBlockEntities.BLOCK_ENTITIES.earlySetup();
        ModContainers.CONTAINERS.earlySetup();
        ModRecipes.RECIPE_SERIALIZERS.earlySetup();
        ModRecipes.RECIPE_TYPES.earlySetup();

        ModFeatures.FEATURES.earlySetup();
    }

    public static void lateSetup()
    {
        ModBlocks.BLOCKS.lateSetup();
        ModItems.ITEMS.lateSetup();
        ModItemGroups.TABS.lateSetup();
        ModSounds.SOUNDS.lateSetup();
        ModBlockEntities.BLOCK_ENTITIES.lateSetup();
        ModContainers.CONTAINERS.lateSetup();
        ModRecipes.RECIPE_SERIALIZERS.lateSetup();
        ModRecipes.RECIPE_TYPES.lateSetup();

        ModFeatures.FEATURES.lateSetup();

        HarvestBlockHandler.setup();

        DispenserBlock.registerBehavior(ModItems.CLAY_TOOL.get(), (context, stack) -> {
            BlockPos offsetPos = context.getPos().relative(context.getBlockState().getValue(DispenserBlock.FACING));
            return ClayToolItem.interactWithBlock(context.getLevel(), offsetPos, context.getLevel().getBlockState(offsetPos), null, null, stack);
        });

        CauldronInteraction.WATER.put(ModItems.CERAMIC_BUCKET.get(), (state, level, pos, player, hand, stack) -> {
            if (state.getValue(LayeredCauldronBlock.LEVEL) == 3)
            {
                if (!level.isClientSide)
                {
                    player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(ModItems.CERAMIC_WATER_BUCKET.get())));
                    player.awardStat(Stats.USE_CAULDRON);
                    player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                    level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());
                    level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            return InteractionResult.PASS;
        });

        CauldronInteraction.EMPTY.put(ModItems.CERAMIC_WATER_BUCKET.get(), (state, level, pos, player, hand, stack) -> {
            if (!level.isClientSide)
            {
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(ModItems.CERAMIC_BUCKET.get())));
                player.awardStat(Stats.FILL_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                level.setBlockAndUpdate(pos, Blocks.WATER_CAULDRON.defaultBlockState());
                level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        });

        Config.INSTANCE.load();
    }
}