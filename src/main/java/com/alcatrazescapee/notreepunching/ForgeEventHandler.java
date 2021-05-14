/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.alcatrazescapee.notreepunching.client.ModSounds;
import com.alcatrazescapee.notreepunching.common.ModTags;
import com.alcatrazescapee.notreepunching.common.items.ModItems;
import com.alcatrazescapee.notreepunching.util.HarvestBlockHandler;
import com.alcatrazescapee.notreepunching.world.ModFeatures;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ForgeEventHandler
{
    private static final Random RANDOM = new Random();
    private static final Set<Biome.Category> CATEGORIES_WITHOUT_ROCKS = new HashSet<>(Arrays.asList(Biome.Category.NONE, Biome.Category.THEEND, Biome.Category.NETHER, Biome.Category.OCEAN));

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
                canHarvest |= HarvestBlockHandler.canHarvest(event.getTargetBlock(), event.getPlayer(), true);
            }
            else
            {
                canHarvest |= HarvestBlockHandler.doesBlockRequireNoToolByDefault(event.getTargetBlock().getBlock());
            }
        }
        event.setCanHarvest(canHarvest);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event)
    {
        if (Config.SERVER.noMiningWithoutCorrectTool.get())
        {
            if (!HarvestBlockHandler.canHarvest(event.getState(), event.getPlayer(), false) && !ModTags.Blocks.ALWAYS_BREAKABLE.contains(event.getState().getBlock()))
            {
                // Everything except instant breaking things will take basically forever (if enabled)
                float newSpeed = Config.SERVER.doInstantBreakBlocksRequireTool.get() ? 0 : 1e-10f;
                event.setNewSpeed(newSpeed);
            }
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event)
    {
        ItemStack stack = event.getItemStack();
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        BlockState state = world.getBlockState(pos);
        if (ModTags.Items.FLINT_KNAPPABLE.contains(stack.getItem()) && state.getMaterial() == Material.STONE)
        {
            if (!world.isClientSide)
            {
                if (RANDOM.nextFloat() < Config.SERVER.flintKnappingConsumeChance.get())
                {
                    if (RANDOM.nextFloat() < Config.SERVER.flintKnappingSuccessChance.get())
                    {
                        Direction face = event.getFace() == null ? Direction.UP : event.getFace();
                        InventoryHelper.dropItemStack(world, pos.getX() + 0.5 + face.getStepX() * 0.5, pos.getY() + 0.5 + face.getStepY() * 0.5, pos.getZ() + 0.5 + face.getStepZ() * 0.5, new ItemStack(ModItems.FLINT_SHARD.get(), 2));
                    }
                    stack.shrink(1);
                    event.getPlayer().setItemInHand(event.getHand(), stack);
                }
                world.playSound(null, pos, ModSounds.KNAPPING.get(), SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
            event.setCancellationResult(ActionResultType.SUCCESS);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onBiomeLoad(BiomeLoadingEvent event)
    {
        if (Config.COMMON.enableLooseRocksWorldGen.get() && !CATEGORIES_WITHOUT_ROCKS.contains(event.getCategory()))
        {
            event.getGeneration().addFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, ModFeatures.LOOSE_ROCKS_CONFIGURED.get());
        }
    }
}