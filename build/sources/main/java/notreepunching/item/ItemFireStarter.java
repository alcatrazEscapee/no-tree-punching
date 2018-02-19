package notreepunching.item;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import notreepunching.block.ModBlocks;

import java.util.ArrayList;
import java.util.List;

public class ItemFireStarter extends ItemBase {

    public ItemFireStarter(String name){
        super(name);

        setMaxDamage(5);
        setMaxStackSize(1);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 15;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn){
        System.out.println("TRYING TO USE THE THING");
        RayTraceResult result = rayTrace(worldIn,playerIn,false);

        if(result != null && result.typeOfHit==RayTraceResult.Type.BLOCK){
            BlockPos pos = result.getBlockPos();
            Block block = worldIn.getBlockState(pos).getBlock();
            //if(block instanceof BlockFirePit){
            //    //playerIn.setActiveHand(handIn);
            //    TileEntityFirePit te = (TileEntityFirePit)worldIn.getTileEntity(pos);
            //    te.lightFirepit();
            //    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
            //}
        }

        playerIn.setActiveHand(handIn);
        return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }

    //@Override
    //public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
    //    World world = player.world;
    //    stack.damageItem(1,player);
    //}

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        EntityPlayer player = (EntityPlayer) entityLiving;

        if(player instanceof EntityPlayer){
            EntityPlayer player1 = (EntityPlayer) player;

            RayTraceResult result = rayTrace(worldIn,player1,false);

            if(result != null && result.typeOfHit==RayTraceResult.Type.BLOCK){ // If looking at a block

                player1.getHeldItemMainhand().damageItem(1,player1);

                BlockPos pos = result.getBlockPos();

                //System.out.println("DISTANCE TO TARGET: "+result.hitVec.lengthVector());

                if(!worldIn.isRemote){
                    System.out.println("TRYING TO LIGHT A FIRE");

                    List<EntityItem> list = worldIn.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.up(), pos.up().add(1, 1, 1)));
                    List<EntityItem> toUse = new ArrayList<EntityItem>();

                    NonNullList<ItemStack> oreList=null;

                    int sticks = 3;
                    int planks = 3;
                    int grass = 3;

                    for(EntityItem drop : list){
                        Item item = drop.getItem().getItem();
                        if(item == ModItems.grassFiber && grass>0){ // Add grass
                            grass = Math.max(grass-drop.getItem().getCount(),0);
                            toUse.add(drop);
                        }
                        oreList= OreDictionary.getOres("plankWood"); // Add wood types
                        for(ItemStack logType: oreList){
                            if(planks > 0 && item == logType.getItem()){
                                planks = Math.max(planks-drop.getItem().getCount(),0);
                                toUse.add(drop);
                            }
                        }
                        oreList= OreDictionary.getOres("stickWood"); // Add wood types
                        for(ItemStack stickType: oreList){
                            if(sticks > 0 && item == stickType.getItem()){
                                sticks = Math.max(sticks-drop.getItem().getCount(),0);
                                toUse.add(drop);
                            }
                        }
                    }
                    if(sticks == 0 && planks == 0 && grass == 0){
                        // Commence Firepit making
                        worldIn.setBlockState(pos.up(), ModBlocks.firePit.getDefaultState());

                        sticks = 3;
                        planks = 3;
                        grass = 3;
                        int remove;

                        for(EntityItem drop2 : toUse) {
                            int removeFromStack = Math.min(3, drop2.getItem().getCount());
                            drop2.getItem().shrink(removeFromStack);
                            if (drop2.getItem().getCount() == 0) {
                                drop2.setDead();
                            }

                            Item item = drop2.getItem().getItem();

                            if (item == ModItems.grassFiber && grass > 0) { // Remove grass fiber items from world
                                remove = Math.min(drop2.getItem().getCount(), grass);
                                grass -= remove;
                                drop2.getItem().shrink(removeFromStack);
                                if (drop2.getItem().getCount() == 0) {
                                    drop2.setDead();
                                }
                            }
                            oreList = OreDictionary.getOres("logWood"); // Remove plank items from world
                            for (ItemStack logType : oreList) {
                                if (planks > 0 && item == logType.getItem()) {
                                    remove = Math.min(drop2.getItem().getCount(), planks);
                                    planks -= remove;
                                    // Not working as of yet
                                    //TileEntityFirePit te = (TileEntityFirePit) worldIn.getTileEntity(pos.up());
                                    //te.build(drop2.getItem());
                                    drop2.getItem().shrink(removeFromStack);
                                    if (drop2.getItem().getCount() == 0) {
                                        drop2.setDead();
                                    }
                                }
                            }
                            oreList = OreDictionary.getOres("stickWood"); // Remove stick items from world
                            for (ItemStack stickType : oreList) {
                                if (sticks > 0 && item == stickType.getItem()) {
                                    remove = Math.min(drop2.getItem().getCount(), sticks);
                                    sticks -= remove;
                                    drop2.getItem().shrink(removeFromStack);
                                    if (drop2.getItem().getCount() == 0) {
                                        drop2.setDead();
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
        return stack;
    }

}
