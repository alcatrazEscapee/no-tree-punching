package notreepunching.event;

import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import notreepunching.client.sound.Sounds;
import notreepunching.config.Config;
import notreepunching.item.ModItems;

public class PlayerEventHandler {

    @SubscribeEvent
    public void playerInteractEvent(PlayerInteractEvent event){
        // Control for flint shard creation
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        if(event.getItemStack().getItem() == Items.FLINT && world.getBlockState(pos).getMaterial() == Material.ROCK){
            if(!world.isRemote){
                if(Math.random()< 0.4) {
                    if(Math.random() < Config.Balance.FLINT_CHANCE) {
                        // Create flint shard
                        ItemStack stack = new ItemStack(ModItems.flintShard, 2);
                        EntityItem item = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, stack);
                        world.spawnEntity(item);
                    }

                    // Remove one flint
                    ItemStack stack2 = event.getItemStack();
                    stack2.shrink(1);
                    if (stack2.getCount() == 0) {
                        stack2 = ItemStack.EMPTY;
                    }
                    event.getEntityPlayer().setHeldItem(event.getHand(), stack2);
                }
            }
            System.out.println("TRYING TO PLAY A SOUND");
            world.playSound(event.getEntityPlayer(),pos, Sounds.FLINT_KNAPPING, SoundCategory.BLOCKS,1.0F,1.0F);
        }
    }
}
