/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.alcatrazescapee.notreepunching.client.ModSounds;
import com.alcatrazescapee.notreepunching.common.ModTags;
import com.alcatrazescapee.notreepunching.common.items.ModItems;
import com.alcatrazescapee.notreepunching.util.HarvestBlockHandler;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ForgeEventHandler
{
    private static final Random RANDOM = new Random();

    @SubscribeEvent
    public static void onHarvestCheck(PlayerEvent.HarvestCheck event)
    {
        boolean canHarvest = event.canHarvest();
        if (ModTags.Blocks.ALWAYS_DROPS.contains(event.getTargetBlock().getBlock()))
        {
            canHarvest = true;
        }
        else
        {
            if (Config.SERVER.noBlockDropsWithoutCorrectTool.get())
            {
                canHarvest |= HarvestBlockHandler.canHarvest(event.getTargetBlock(), event.getPlayer());
            }
            else
            {
                canHarvest |= HarvestBlockHandler.doesBlockRequireNoToolByDefault(event.getTargetBlock().getBlock());
            }
        }
        event.setCanHarvest(canHarvest);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void breakSpeed(PlayerEvent.BreakSpeed event)
    {
        if (Config.SERVER.noMiningWithoutCorrectTool.get())
        {
            if (!HarvestBlockHandler.canHarvest(event.getState(), event.getPlayer()) && !ModTags.Blocks.ALWAYS_BREAKABLE.contains(event.getState().getBlock()))
            {
                // Everything except instant breaking things will take basically forever (if enabled)
                float newSpeed = Config.SERVER.doInstantBreakBlocksRequireTool.get() ? 0 : 1e-10f;
                event.setNewSpeed(newSpeed);
            }
        }
    }

    @SubscribeEvent
    public static void playerInteractEvent(PlayerInteractEvent.RightClickBlock event)
    {
        ItemStack stack = event.getItemStack();
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        BlockState state = world.getBlockState(pos);
        if (ModTags.Items.FLINT_KNAPPABLE.contains(stack.getItem()) && state.getMaterial() == Material.ROCK)
        {
            if (!world.isRemote)
            {
                if (RANDOM.nextFloat() < Config.SERVER.flintKnappingConsumeChance.get())
                {
                    if (RANDOM.nextFloat() < Config.SERVER.flintKnappingSuccessChance.get())
                    {
                        Direction face = event.getFace() == null ? Direction.UP : event.getFace();
                        InventoryHelper.spawnItemStack(world, pos.getX() + 0.5 + face.getXOffset() * 0.5, pos.getY() + 0.5 + face.getYOffset() * 0.5, pos.getZ() + 0.5 + face.getZOffset() * 0.5, new ItemStack(ModItems.FLINT_SHARD.get(), 2));
                    }
                    stack.shrink(1);
                    event.getPlayer().setHeldItem(event.getHand(), stack);
                }
                world.playSound(null, pos, ModSounds.KNAPPING.get(), SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
            event.setCancellationResult(ActionResultType.SUCCESS);
            event.setCanceled(true);
        }
    }
}