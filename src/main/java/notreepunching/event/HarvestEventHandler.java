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
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import notreepunching.NoTreePunching;
import notreepunching.config.Config;
import notreepunching.item.*;
import notreepunching.recipe.ModRecipes;
import scala.actors.threadpool.Arrays;

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

        // heldItemStack != ItemStack.EMPTY  && neededHarvestLevel >= 0
        if (neededToolClass != null) {
            for (String toolClass : heldItemStack.getItem().getToolClasses(heldItemStack)) {
                if (neededToolClass.equals(toolClass)) {
                    if (heldItemStack.getItem().getHarvestLevel(heldItemStack, toolClass, null, null) >= neededHarvestLevel) {
                        return;
                    }
                }else if(neededToolClass.equals("shovel") && toolClass.equals("pickaxe") && heldItemStack.getItem().getHarvestLevel(heldItemStack, toolClass, null, null) >= 1) {
                    return;
                }
            }
            // Always allow certian blocks to break at normal speed
            //Iterator itr = Arrays.asList(Config.VanillaTweaks.BREAKABLE).iterator();
            for(String name : Config.VanillaTweaks.BREAKABLE){
                if (block.getRegistryName().toString().equals(name)) {
                    return;
                }
            }

            switch (neededToolClass) {
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

            // Stone and its Variants drop the respective rock
            if(Config.VanillaTweaks.STONE_DROPS_ROCKS) {
                if(block == Blocks.STONE) {
                    int meta = block.getMetaFromState(event.getState());
                    if (meta == 0) { // Stone
                        event.getDrops().clear();
                        event.getDrops().add(new ItemStack(ModItems.rockStone,3,0));
                    } else if (meta == 1) { // Granite
                        event.getDrops().clear();
                        event.getDrops().add(new ItemStack(ModItems.rockStone,3,3));
                    } else if (meta == 3) { // Diorite
                        event.getDrops().clear();
                        event.getDrops().add(new ItemStack(ModItems.rockStone,3,2));
                    } else if (meta == 5) { // Andesite
                        event.getDrops().clear();
                        event.getDrops().add(new ItemStack(ModItems.rockStone,3,1));
                    }
                }
            }
            if(NoTreePunching.replaceQuarkStones){
                int meta = block.getMetaFromState(event.getState());
                if(block.getRegistryName().toString().equals("quark:marble") && meta == 0){
                    event.getDrops().clear();
                    event.getDrops().add(new ItemStack(ModItems.rockStone,3,4));
                }
                else if(block.getRegistryName().toString().equals("quark:limestone") && meta == 0){
                    event.getDrops().clear();
                    event.getDrops().add(new ItemStack(ModItems.rockStone,3,5));
                }
            }
            if(NoTreePunching.replaceRusticStone){
                if(block.getRegistryName().toString().equals("rustic:slate")){
                    event.getDrops().clear();
                    event.getDrops().add(new ItemStack(ModItems.rockStone,3,6));
                }
            }

            //Allows mattock to drop the normal block drop
            if(heldItemStack.getItem() instanceof ItemMattock){
                ItemMattock mattock = (ItemMattock) heldItemStack.getItem();
                if(mattock.shouldBreakBlock(block)){
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


            //heldItemStack != ItemStack.EMPTY && neededHarvestLevel >= 0
            if (neededToolClass != null) {
                for (String toolClass : heldItemStack.getItem().getToolClasses(heldItemStack)) {
                    if (neededToolClass.equals(toolClass)) {
                        if (heldItemStack.getItem().getHarvestLevel(heldItemStack, toolClass, null, null) >= neededHarvestLevel) {
                            return;
                        }
                    }
                    // Metal Pickaxes and above are allowed to function as shovels with no mining speed penalty + block drops.
                    else if(neededToolClass.equals("shovel") && toolClass.equals("pickaxe") && heldItemStack.getItem().getHarvestLevel(heldItemStack, toolClass, null, null) >= 1) {
                        return;
                    }
                }

                //Iterator itr = Arrays.asList(Config.VanillaTweaks.BREAKABLE).iterator();
                String blockName=block.getRegistryName().getResourceDomain()+":"+block.getRegistryName().getResourcePath();
                for(String name : Config.VanillaTweaks.BREAKABLE) {
                    if(name.equals(blockName)) {
                        return;
                    }
                }
                /*while (itr.hasNext()) {
                    if (blockName.equals(itr.next())) {
                        return;
                    }
                }*/

                event.getDrops().clear();
            }
        }
    }

    @SubscribeEvent
    public void harvestBlockInitialCheck(PlayerEvent.HarvestCheck event){
        Block block = event.getTargetBlock().getBlock();
        String blockName=block.getRegistryName().getResourceDomain()+":"+block.getRegistryName().getResourcePath();
        for(String name : Config.VanillaTweaks.BREAKABLE){
            if(blockName.equals(name)){ event.setCanHarvest(true); }
        }
    }

    @SubscribeEvent
    public void blockEventCheck(BlockEvent.BreakEvent event){
        // Additional check for IC2 and the Damn Rubberwood
        Block block = event.getState().getBlock();
        if(block.getUnlocalizedName().equals("ic2.rubber_wood")){
            EntityPlayer player = event.getPlayer();
            if (player == null || player instanceof FakePlayer) {
                return;
            }
            // Always allow creative mode
            if (player.capabilities.isCreativeMode) {
                return;
            }
            ItemStack heldItemStack = player.getHeldItemMainhand();
            for (String toolClass : heldItemStack.getItem().getToolClasses(heldItemStack)) {
                if(toolClass.equals("axe")){ return; }
            }
            //Iterator itr = Arrays.asList(Config.VanillaTweaks.BREAKABLE).iterator();
            String blockName=block.getRegistryName().getResourceDomain()+":"+block.getRegistryName().getResourcePath();
            for(String name : Config.VanillaTweaks.BREAKABLE){
                if(blockName.equals(name)){ return; }
            }
            // Else, cancel the event and do a manual break, not triggering the IC2 breaking
            event.setCanceled(true);
            event.getWorld().setBlockState(event.getPos(),Blocks.AIR.getDefaultState());
        }
    }
}
