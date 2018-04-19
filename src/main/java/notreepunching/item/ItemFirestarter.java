package notreepunching.item;


import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import notreepunching.block.ModBlocks;

import java.util.ArrayList;
import java.util.List;

public class ItemFirestarter extends ItemTool {

    public String name;

    public ItemFirestarter(String name){
        super(ToolMaterial.WOOD, Sets.newHashSet());

        setRegistryName(name);
        setUnlocalizedName(name);
        this.name = name;

        this.setMaxDamage(10);
        this.setMaxStackSize(1);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 20;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn){
        RayTraceResult result = rayTrace(worldIn,playerIn,false);

        if(result.typeOfHit==RayTraceResult.Type.BLOCK){
            BlockPos pos = result.getBlockPos();
            Block block = worldIn.getBlockState(pos).getBlock();
            /*if(block instanceof BlockFirePit){
                //playerIn.setActiveHand(handIn);
                TileEntityFirePit te = (TileEntityFirePit)worldIn.getTileEntity(pos);
                te.lightFirepit();
                return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
            }*/
        }

        playerIn.setActiveHand(handIn);
        return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }

    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        stack.damageItem(1, attacker);
        return true;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        EntityPlayer player = (EntityPlayer) entityLiving;

        if(player != null){
            EntityPlayer player1 = (EntityPlayer) player;

            RayTraceResult result = rayTrace(worldIn,player1,false);

            if(result.typeOfHit==RayTraceResult.Type.BLOCK){ // If looking at a block

                BlockPos pos = result.getBlockPos();

                //System.out.println("DISTANCE TO TARGET: "+result.hitVec.lengthVector());

                if(!worldIn.isRemote){

                    List<EntityItem> list = worldIn.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.up(), pos.up().add(1, 1, 1)));
                    List<EntityItem> toUse = new ArrayList<EntityItem>();

                    NonNullList<ItemStack> oreList=null;

                    int sticks = 3;
                    int logs = 1;
                    int thatch = 1;

                    for(EntityItem drop : list){
                        Item item = drop.getItem().getItem();
                        if(item == ModItems.grassString && thatch>0){ // Add thatch
                            thatch = Math.max(thatch-drop.getItem().getCount(),0);
                            toUse.add(drop);
                        }
                        oreList= OreDictionary.getOres("logWood"); // Add wood types
                        for(ItemStack logType: oreList){
                            if(logs > 0 && item == logType.getItem()){
                                logs = Math.max(logs-drop.getItem().getCount(),0);
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
                    if(sticks == 0 && logs == 0 && thatch == 0){
                        // Commence Firepit making
                        worldIn.setBlockState(pos.up(), ModBlocks.firepit.getDefaultState());
                        stack.damageItem(1,entityLiving);

                        sticks = 3;
                        logs = 1;
                        thatch = 1;
                        int remove;

                        for(EntityItem drop2 : toUse) {
                            int removeFromStack = Math.min(3, drop2.getItem().getCount());
                            drop2.getItem().shrink(removeFromStack);
                            if (drop2.getItem().getCount() == 0) {
                                drop2.setDead();
                            }

                            Item item = drop2.getItem().getItem();

                            if (item == ModItems.grassString && thatch > 0) { // Remove thatch items from world
                                remove = Math.min(drop2.getItem().getCount(), thatch);
                                thatch -= remove;
                                drop2.getItem().shrink(removeFromStack);
                                if (drop2.getItem().getCount() == 0) {
                                    drop2.setDead();
                                }
                            }
                            oreList = OreDictionary.getOres("logWood"); // Remove log items from world
                            for (ItemStack logType : oreList) {
                                if (logs > 0 && item == logType.getItem()) {
                                    remove = Math.min(drop2.getItem().getCount(), logs);
                                    logs -= remove;
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

