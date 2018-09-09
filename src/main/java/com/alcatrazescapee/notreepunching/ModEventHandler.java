/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.alcatrazescapee.alcatrazcore.util.CoreHelpers;
import com.alcatrazescapee.alcatrazcore.util.RegistryHelper;
import com.alcatrazescapee.notreepunching.client.ModSounds;
import com.alcatrazescapee.notreepunching.common.capability.CapabilityPlayerItem;
import com.alcatrazescapee.notreepunching.common.capability.IPlayerItem;
import com.alcatrazescapee.notreepunching.common.items.ModItems;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class ModEventHandler
{
    @SubscribeEvent
    public static void breakEvent(BlockEvent.BreakEvent event)
    {
        EntityPlayer player = event.getPlayer();
        if (player != null)
        {
            IPlayerItem cap = player.getCapability(CapabilityPlayerItem.HARVEST_TOOL_CAPABILITY, null);
            if (cap != null)
            {
                cap.setStack(player.getHeldItemMainhand());
            }
        }
    }

    // Controls the slow mining speed of blocks that aren't the right tool
    @SubscribeEvent
    public static void slowMining(PlayerEvent.BreakSpeed event)
    {

        EntityPlayer player = event.getEntityPlayer();
        if (player == null || player instanceof FakePlayer || player.capabilities.isCreativeMode)
        {
            return;
        }

        // Get Variables for the block and item held
        Block block = event.getState().getBlock();
        ItemStack heldItemStack = player.getHeldItemMainhand();

        // Get variables for the required and current harvest levels + tools
        int neededHarvestLevel = block.getHarvestLevel(event.getState());
        String neededToolClass = block.getHarvestTool(event.getState());

        // heldItemStack != ItemStack.EMPTY  && neededHarvestLevel >= 0
        if (neededToolClass != null)
        {
            for (String toolClass : heldItemStack.getItem().getToolClasses(heldItemStack))
            {
                if (neededToolClass.equals(toolClass))
                {
                    if (heldItemStack.getItem().getHarvestLevel(heldItemStack, toolClass, null, null) >= neededHarvestLevel)
                    {
                        return;
                    }
                }
                else if (neededToolClass.equals("shovel") && toolClass.equals("pickaxe") && heldItemStack.getItem().getHarvestLevel(heldItemStack, toolClass, null, null) >= 1)
                {
                    return;
                }
                // Tinkers Construct Mattock
                else if ((neededToolClass.equals("shovel") && toolClass.equals("mattock")) || (neededToolClass.equals("axe") && toolClass.equals("mattock")))
                {
                    return;
                }
            }
            // todo: whitelist for blocks that always break
            //if (isWhitelisted(block.getRegistryName()))
            //    return;

            switch (neededToolClass)
            {
                case "axe":
                    event.setNewSpeed(event.getOriginalSpeed() / 5);
                    break;
                case "shovel":
                    event.setNewSpeed(event.getOriginalSpeed() / 3);
                    break;
                case "pickaxe":
                    event.setNewSpeed(event.getOriginalSpeed() / 8);
                default:
                    event.setNewSpeed(event.getOriginalSpeed() / 3);
            }
        }

    }

    // Controls the drops of any block that is broken to require specific tools.
    @SubscribeEvent
    public static void harvestBlock(BlockEvent.HarvestDropsEvent event)
    {

        EntityPlayer player = event.getHarvester();

        if (!event.getWorld().isRemote)
        {
            // Explosions, Quarries, etc. all are ok to break blocks
            if (player == null || player instanceof FakePlayer)
            {
                return;
            }
            // Always allow creative mode
            if (player.capabilities.isCreativeMode)
            {
                return;
            }

            // Get Variables for the block and item held. Use CapabilityPlayerHarvestTool in case the tool has broken between breaking the block and now
            IBlockState state = event.getState();
            ItemStack stack = player.getHeldItemMainhand();
            IPlayerItem cap = player.getCapability(CapabilityPlayerItem.HARVEST_TOOL_CAPABILITY, null);
            if (cap != null)
                stack = cap.getStack();

            // Final case: Get variables for the required and current harvest levels + tools
            int neededHarvestLevel = state.getBlock().getHarvestLevel(event.getState());
            String neededToolClass = state.getBlock().getHarvestTool(event.getState());

            if (neededToolClass != null)
            {
                for (String toolClass : stack.getItem().getToolClasses(stack))
                {
                    if (neededToolClass.equals(toolClass))
                    {
                        if (stack.getItem().getHarvestLevel(stack, toolClass, null, null) >= neededHarvestLevel)
                        {
                            return;
                        }
                    }
                    // Metal Pickaxes and above are allowed to function as shovels with no mining speed penalty + block drops.
                    else if (neededToolClass.equals("shovel") && toolClass.equals("pickaxe") && stack.getItem().getHarvestLevel(stack, toolClass, null, null) >= 1)
                    {
                        return;
                    }
                    // Tinkers Construct Mattock
                    else if ((neededToolClass.equals("shovel") && toolClass.equals("mattock")) || (neededToolClass.equals("axe") && toolClass.equals("mattock")))
                    {
                        return;
                    }
                }
                // todo: whitelist for blocks that always break
                //if (isWhitelisted(state))
                //    return;
                event.getDrops().clear();
            }
        }
    }

    @SubscribeEvent
    public static void playerInteractEvent(PlayerInteractEvent.RightClickBlock event)
    {

        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()) { return; }

        EntityPlayer player = event.getEntityPlayer();
        if (event.getHand() == EnumHand.OFF_HAND)
        {
            ItemStack mainStack = player.getHeldItem(EnumHand.MAIN_HAND);
            if (mainStack.getItem() == Items.FLINT)
            {
                event.setCanceled(true);
                return;
            }
            else if (mainStack.getItem() == Items.COAL && mainStack.getMetadata() == 1)
            {
                // Charcoal was placed with the main hand, so cancel the event
                event.setCanceled(true);
                return;
            }
        }

        World world = event.getWorld();
        BlockPos pos = event.getPos();
        IBlockState state = world.getBlockState(pos);

        // Flint shard creation
        if (stack.getItem() == Items.FLINT)
        {

            if (state.getMaterial() == Material.ROCK && state.isNormalCube() && event.getFace() == EnumFacing.UP)
            {
                if (!world.isRemote)
                {
                    if (Math.random() < ModConfig.GENERAL.flintKnappingChance)
                    {
                        if (Math.random() < ModConfig.GENERAL.flintKnappingSuccessChance)
                            CoreHelpers.dropItemInWorldExact(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, new ItemStack(ModItems.FLINT_SHARD));

                        player.setHeldItem(event.getHand(), CoreHelpers.consumeItem(player, stack));
                    }
                    world.playSound(null, pos, ModSounds.KNAPPING, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
            }
        }
        //todo: charcoal pile placement
        // Creating a charcoal pile by placing charcoal
        /*else if (stack.getItem() == Items.COAL && stack.getMetadata() == 1)
        {
            EnumFacing facing = event.getFace();
            if (facing != null)
            {
                if (world.getBlockState(pos.down().offset(facing)).isNormalCube()
                        && world.getBlockState(pos.offset(facing)).getBlock().isReplaceable(world, pos.offset(facing)))
                {

                    if (world.getBlockState(pos).getBlock() instanceof BlockCharcoalPile || world.getBlockState(pos).getBlock() instanceof BlockCharcoalPile)
                    {
                        if (world.getBlockState(pos).getValue(LAYERS) != 8)
                        {
                            return;
                        }
                    }
                    if (!world.isRemote)
                    {
                        world.setBlockState(pos.offset(facing), ModBlocks.charcoalPile.getDefaultState());

                        if (!player.isCreative())
                        {
                            player.setHeldItem(event.getHand(), Helpers.consumeItem(stack));
                        }
                        world.playSound(null, pos.offset(facing), SoundEvents.BLOCK_GRAVEL_PLACE, SoundCategory.BLOCKS, 1.0F, 0.5F);
                        event.setCanceled(true);
                    }

                }
            }
        }
        // todo: log pile placement
        else if (Helpers.doesStackMatchOre(stack, "logWood"))
        {
            EnumFacing facing = event.getFace();
            if (facing != null)
            {
                if (world.getBlockState(pos.down().offset(facing)).isNormalCube()
                        && world.getBlockState(pos.offset(facing)).getBlock().isReplaceable(world, pos.offset(facing)) &&
                        player.isSneaking())
                {

                    if (!world.isRemote)
                    {
                        world.setBlockState(pos.offset(facing), ModBlocks.woodPile.getStateForPlacement(world, pos, facing, 0, 0, 0, 0, player));

                        TileEntity te = world.getTileEntity(pos.offset(facing));
                        if (te instanceof TELogPile)
                        {
                            ((TELogPile) te).insertLog(Helpers.copyStack(stack, 1));
                        }

                        if (!player.isCreative())
                        {
                            player.setHeldItem(event.getHand(), Helpers.consumeItem(stack));
                        }
                        world.playSound(null, pos.offset(facing), SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    }
                    event.setCanceled(true);
                }
            }
        }*/
    }

    @SubscribeEvent
    public static void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> event)
    {
        if (event.getObject() instanceof EntityPlayer)
        {
            event.addCapability(CapabilityPlayerItem.KEY, new CapabilityPlayerItem.Instance());
        }
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        NoTreePunching.getLog().info("Registering items...");
        RegistryHelper.get(MOD_ID).initItems(event);
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        RegistryHelper.get(MOD_ID).initBlocks(event);
    }

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event)
    {
        RegistryHelper.get(MOD_ID).initSounds(event);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerModels(ModelRegistryEvent event)
    {
        RegistryHelper.get(MOD_ID).initModels(event);
    }
}
