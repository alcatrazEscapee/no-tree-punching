package notreepunching.event;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import notreepunching.block.BlockCharcoalPile;
import notreepunching.block.ModBlocks;
import notreepunching.client.sound.Sounds;
import notreepunching.config.Config;
import notreepunching.item.ModItems;
import notreepunching.util.ItemUtil;
import notreepunching.util.MiscUtil;

import static notreepunching.block.BlockCharcoalPile.LAYERS;

public class PlayerEventHandler {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void playerInteractEvent(PlayerInteractEvent.RightClickBlock event){
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        IBlockState state = world.getBlockState(pos);
        ItemStack stack = event.getItemStack();
        EntityPlayer player = event.getEntityPlayer();

        if(stack.isEmpty()){ return; }

        if(event.getHand() == EnumHand.OFF_HAND){
            ItemStack mainStack = player.getHeldItem(EnumHand.MAIN_HAND);
            if(mainStack.getItem() == Items.FLINT){
                event.setCanceled(true);
                return;
            }
            else if(mainStack.getItem() == Items.COAL && mainStack.getMetadata() == 1){
                // Charcoal was placed with the main hand, so cancel the event
                event.setCanceled(true);
                return;
            }
        }

        // Flint shard creation
        if(stack.getItem() == Items.FLINT){

            if(world.getBlockState(pos).getMaterial() == Material.ROCK && state.getBlock().isFullCube(state)){
                if(!world.isRemote){
                    if(Math.random()< 0.7) {
                        if(Math.random() < Config.Balance.FLINT_CHANCE) {
                            // Create flint shard
                            ItemStack stack2 = new ItemStack(ModItems.flintShard, 2);
                            EntityItem item = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, stack2);
                            world.spawnEntity(item);
                        }

                        // Remove one flint
                        if(!player.isCreative()) {
                            player.setHeldItem(event.getHand(), ItemUtil.consumeItem(stack));
                        }
                    }
                    world.playSound(null,pos, Sounds.FLINT_KNAPPING, SoundCategory.BLOCKS,0.5F,1.0F);
                }
            }
        }
        // Creating a forge by placing charcoal
        else if(stack.getItem() == Items.COAL && stack.getMetadata() == 1){


            EnumFacing facing = event.getFace();
            if (facing != null) {
                if (world.getBlockState(pos.down().offset(facing)).getBlock().isFullCube(world.getBlockState(pos.down().offset(facing)))
                        && world.getBlockState(pos.offset(facing)).getBlock() == Blocks.AIR) {

                    if(world.getBlockState(pos).getBlock() instanceof BlockCharcoalPile || world.getBlockState(pos).getBlock() instanceof BlockCharcoalPile){
                        if(world.getBlockState(pos).getValue(LAYERS) != 8){
                            return;
                        }
                    }
                    if(!world.isRemote) {
                        world.setBlockState(pos.offset(facing), ModBlocks.charcoalPile.getDefaultState());

                        if (!player.isCreative()) {
                            player.setHeldItem(event.getHand(), ItemUtil.consumeItem(stack));
                        }
                        world.playSound(null, pos.offset(facing), SoundEvents.BLOCK_GRAVEL_PLACE, SoundCategory.BLOCKS, 1.0F, 0.5F);
                    }

                }
            }
        }

        else if(MiscUtil.doesStackMatchOre(stack, "logWood")){
            EnumFacing facing = event.getFace();
            if (facing != null) {
                if (world.getBlockState(pos.down().offset(facing)).getBlock().isFullCube(world.getBlockState(pos.down().offset(facing)))
                        && world.getBlockState(pos.offset(facing)).getBlock() == Blocks.AIR &&
                        player.isSneaking()) {

                    if(!world.isRemote) {
                        world.setBlockState(pos.offset(facing), ModBlocks.woodPile.getDefaultState());

                        if (!player.isCreative()) {
                            player.setHeldItem(event.getHand(), ItemUtil.consumeItem(stack));
                        }
                        world.playSound(null, pos.offset(facing), SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    }
                    event.setCanceled(true);
                    return;
                }
            }
        }
    }

}
