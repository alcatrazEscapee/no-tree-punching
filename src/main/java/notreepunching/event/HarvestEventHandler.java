package notreepunching.event;


import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import notreepunching.config.Config;
import notreepunching.item.*;
import notreepunching.recipe.ModRecipes;

import java.util.Iterator;

public class HarvestEventHandler {

    // Controls the slow mining speed of blocks that aren't the right tool
    @SubscribeEvent
    public void slowMining(PlayerEvent.BreakSpeed event){
        EntityPlayer player = event.getEntityPlayer();

        if (player == null || player instanceof FakePlayer) {
            return;
        }

        if (player.capabilities.isCreativeMode) {
            return;
        }
        // Get Variables for the block and item held
        Block block = event.getState().getBlock();
        ItemStack heldItemStack = player.getHeldItemMainhand();

        // Get variables for the required and current harvest levels + tools
        int neededHarvestLevel = block.getHarvestLevel(event.getState());
        String neededToolClass = block.getHarvestTool(event.getState());

        // Allows Mattocks to break at slightly higher than normal speeds
        if (heldItemStack.getItem() instanceof ItemMattock){
            ItemMattock mattock = (ItemMattock) heldItemStack.getItem();
            if(mattock.shouldBreakBlock(block)){
                event.setNewSpeed(event.getOriginalSpeed()*1.4F);
                return;
            }
        }

        // Allows knifes to break at normal speeds
        if (heldItemStack.getItem() instanceof ItemKnife) {
            ItemKnife knife = (ItemKnife) heldItemStack.getItem();
            if (knife.shouldBreakBlock(block)) {
                return;
            }
        }

        if (heldItemStack != null && neededToolClass != null && neededHarvestLevel >= 0) {
            for (String toolClass : heldItemStack.getItem().getToolClasses(heldItemStack)) {
                if (neededToolClass == toolClass) {
                    if (heldItemStack.getItem().getHarvestLevel(heldItemStack, toolClass, null, null) >= neededHarvestLevel) {
                        return;
                    }
                }else if(neededToolClass  == "shovel" && toolClass == "pickaxe" && heldItemStack.getItem().getHarvestLevel(heldItemStack, toolClass, null, null) >= 1) {
                    return;
                }
            }
            // Always allow certian blocks to break at normal speed
            Iterator<String> itr = Config.CFG_ALWAYS_BREAKABLE.iterator();
            while (itr.hasNext()) {
                if (block.getRegistryName().equals(itr.next())) {
                    return;
                }
            }

            switch (neededToolClass) {
                case "axe":
                    event.setNewSpeed(event.getOriginalSpeed() / 8);
                    break;
                case "shovel":
                    event.setNewSpeed(event.getOriginalSpeed() / 5);
                    break;
                case "pickaxe":
                    event.setNewSpeed(event.getOriginalSpeed() / 15);
                default:
                    event.setNewSpeed(event.getOriginalSpeed() / 5);
            }
        }

    }

    // Controls the drops of any block that is broken to require specific tools.
    @SubscribeEvent
    public void harvestBlock(BlockEvent.HarvestDropsEvent event) {

        EntityPlayer player = event.getHarvester();

        if (!event.getWorld().isRemote) {
            // Explosions, Quarries, etc. all are ok to break blocks
            if (player == null || player instanceof FakePlayer) {
                return;
            }
            // Always allow creative mode
            if (player.capabilities.isCreativeMode) {
                return;
            }

            // Get Variables for the block and item held
            Block block = event.getState().getBlock();
            ItemStack heldItemStack = player.getHeldItemMainhand();

            // Leaves now drop sticks 20% without a knife. 50% with a knife
            if (block instanceof BlockLeaves) {
                float stickDropChance = 0.2F;
            if(heldItemStack.getItem() instanceof ItemKnife){
                stickDropChance += 0.3F;
            }
                if (event.getWorld().rand.nextFloat() <= stickDropChance) {
                    event.getDrops().add(new ItemStack(Items.STICK));
                }
                return;
            }

            // Allows crude pick to have special drops when breaking blocks
            if(heldItemStack.getItem() instanceof ItemCrudePick){
                ItemCrudePick crudePick = (ItemCrudePick) heldItemStack.getItem();
                if(crudePick.shouldBreakBlock(block)){
                    if(block == Blocks.IRON_ORE){ // Iron ore will drop 1 - 2 iron ore pieces
                        event.getDrops().clear();
                        event.getDrops().add(new ItemStack(ModItems.poorIron,event.getWorld().rand.nextInt(2)+1));
                    }
                    else if(block == Blocks.COAL_ORE){ // Coal ore will drop 1 - 2 coal pieces
                        event.getDrops().clear();
                        event.getDrops().add(new ItemStack(ModItems.poorCoal,event.getWorld().rand.nextInt(2)+1));
                    }
                    else if(block == Blocks.STONE || block == Blocks.COBBLESTONE){ // Cobblestone and stone will drop 1-3 rocks (4 rocks = 1 cobble)
                        event.getDrops().clear();
                        event.getDrops().add(new ItemStack(ModItems.rock,event.getWorld().rand.nextInt(3)+1));
                    }
                    else if(block == Blocks.GRAVEL){ // Gravel will drop 0-2 rocks and 20% for 1 flint
                        event.getDrops().clear();
                        event.getDrops().add(new ItemStack(ModItems.rock,event.getWorld().rand.nextInt(3)));
                        if(event.getWorld().rand.nextFloat()>0.8){
                            event.getDrops().add(new ItemStack(Items.FLINT));
                        }
                    }
                    return;
                }
            }

            //Allows mattock to drop the normal block drop
            if(heldItemStack.getItem() instanceof ItemMattock){
                ItemMattock mattock = (ItemMattock) heldItemStack.getItem();
                if(mattock.shouldBreakBlock(block)){
                    return;
                }
            }

            //Allows crude axe to have special drops when breaking blocks
            if(heldItemStack.getItem() instanceof ItemCrudeAxe){
                ItemCrudeAxe crudeAxe = (ItemCrudeAxe) heldItemStack.getItem();
                if(crudeAxe.shouldBreakBlock(block)){
                    if(block instanceof BlockNewLog){
                        event.getDrops().clear();
                        event.getDrops().add(new ItemStack(Item.getItemFromBlock(Blocks.PLANKS),1,block.getMetaFromState(event.getState())+4));
                        event.getDrops().add(new ItemStack(Items.STICK,event.getWorld().rand.nextInt(3),0));
                    }else if(block instanceof BlockOldLog){
                        event.getDrops().clear();
                        event.getDrops().add(new ItemStack(Item.getItemFromBlock(Blocks.PLANKS),1,block.getMetaFromState(event.getState())));
                        event.getDrops().add(new ItemStack(Items.STICK,event.getWorld().rand.nextInt(3),0));
                    }
                    return;
                }
            }

            //Allows Knifes to have special drops when breaking blocks
            if (heldItemStack.getItem() instanceof ItemKnife) {
                ItemKnife knife = (ItemKnife) heldItemStack.getItem();
                if (knife.shouldBreakBlock(block)) {
                    if(block instanceof BlockDoublePlant || block instanceof BlockTallGrass){
                        if(event.getWorld().rand.nextFloat()>0.7){
                            event.getDrops().add(new ItemStack(ModItems.grassFiber,1,0));
                        }
                    }
                }
                if (knife.shouldDamageItem(block)) {
                    player.getHeldItemMainhand().damageItem(1, player);
                }
            }

            // Final case: Get variables for the required and current harvest levels + tools
            int neededHarvestLevel = block.getHarvestLevel(event.getState());
            String neededToolClass = block.getHarvestTool(event.getState());

            if (heldItemStack != null && neededToolClass != null && neededHarvestLevel >= 0) {
                for (String toolClass : heldItemStack.getItem().getToolClasses(heldItemStack)) {
                    if (neededToolClass == toolClass) {
                        if (heldItemStack.getItem().getHarvestLevel(heldItemStack, toolClass, null, null) >= neededHarvestLevel) {
                            return;
                        }
                    }
                    // Metal Pickaxes and above are allowed to function as shovels with no mining speed penalty + block drops.
                    else if(neededToolClass  == "shovel" && toolClass == "pickaxe" && heldItemStack.getItem().getHarvestLevel(heldItemStack, toolClass, null, null) >= 1) {
                        return;
                    }
                }

                Iterator<String> itr = Config.CFG_ALWAYS_BREAKABLE.iterator();
                while (itr.hasNext()) {
                    if (block.getRegistryName().equals(itr.next())) {
                        return;
                    }
                }

                event.getDrops().clear();
            }
        }
    }
}
