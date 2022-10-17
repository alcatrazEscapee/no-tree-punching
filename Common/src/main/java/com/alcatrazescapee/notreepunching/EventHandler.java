package com.alcatrazescapee.notreepunching;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.Nullable;

import com.alcatrazescapee.notreepunching.client.ModSounds;
import com.alcatrazescapee.notreepunching.common.ModTags;
import com.alcatrazescapee.notreepunching.common.items.ModItems;
import com.alcatrazescapee.notreepunching.util.HarvestBlockHandler;
import com.alcatrazescapee.notreepunching.util.Helpers;

public final class EventHandler
{
    public static boolean hasLooseRocks(Biome.BiomeCategory category)
    {
        return category != Biome.BiomeCategory.NONE && category != Biome.BiomeCategory.THEEND && category != Biome.BiomeCategory.NETHER && category != Biome.BiomeCategory.OCEAN;
    }

    public static boolean modifyHarvestCheck(Player player, BlockState state, @Nullable BlockPos pos, boolean canHarvest)
    {
        return canHarvest || HarvestBlockHandler.isUsingCorrectToolForDrops(state, pos, player);
    }

    public static float modifyBreakSpeed(Player player, BlockState state, @Nullable BlockPos pos, float speed)
    {
        return HarvestBlockHandler.isUsingCorrectToolToMine(state, pos, player) ? speed : 0;
    }

    /**
     * @return If non-null, an interaction was done and the regular code flow should be prevented.
     */
    @Nullable
    public static InteractionResult onRightClickBlock(Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack, @Nullable Direction targetedFace)
    {
        final BlockState state = level.getBlockState(pos);
        if (Helpers.isItem(stack.getItem(), ModTags.Items.FLINT_KNAPPABLE) && state.getMaterial() == Material.STONE)
        {
            if (!level.isClientSide)
            {
                if (level.random.nextFloat() < Config.INSTANCE.flintKnappingConsumeChance.getAsFloat())
                {
                    if (level.random.nextFloat() < Config.INSTANCE.flintKnappingSuccessChance.getAsFloat())
                    {
                        Direction face = targetedFace == null ? Direction.UP : targetedFace;
                        Containers.dropItemStack(level, pos.getX() + 0.5 + face.getStepX() * 0.5, pos.getY() + 0.5 + face.getStepY() * 0.5, pos.getZ() + 0.5 + face.getStepZ() * 0.5, new ItemStack(ModItems.FLINT_SHARD.get(), 2));
                    }
                    stack.shrink(1);
                    player.setItemInHand(hand, stack);
                }
                level.playSound(null, pos, ModSounds.KNAPPING.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return InteractionResult.SUCCESS;
        }
        return null;
    }

    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("notreepunchingReloadConfig").requires(c -> c.hasPermission(2)).executes(source -> {
            Config.INSTANCE.load();
            return Command.SINGLE_SUCCESS;
        }));
    }
}