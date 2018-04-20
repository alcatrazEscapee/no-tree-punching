package notreepunching.item;


import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import notreepunching.NoTreePunching;
import notreepunching.block.ModBlocks;
import notreepunching.config.Config;

import java.util.ArrayList;
import java.util.List;

import static notreepunching.block.BlockFirepit.BURNING;

public class ItemFirestarter extends ItemTool {

    public String name;

    public ItemFirestarter(String name){
        super(ToolMaterial.WOOD, Sets.newHashSet());

        setRegistryName(name);
        setUnlocalizedName(name);
        this.name = name;
        setCreativeTab(NoTreePunching.NTP_Tab);

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
        System.out.println("Using the firestarter");
        /*RayTraceResult result = rayTrace(worldIn,playerIn,false);
        if(result.typeOfHit==RayTraceResult.Type.BLOCK){
            BlockPos pos = result.getBlockPos();
            Block block = worldIn.getBlockState(pos).getBlock();
            if(block instanceof BlockFirePit){
                //playerIn.setActiveHand(handIn);
                TileEntityFirePit te = (TileEntityFirePit)worldIn.getTileEntity(pos);
                te.lightFirepit();
                return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
            }
        }*/

        playerIn.setActiveHand(handIn);
        return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }

    @SideOnly(Side.CLIENT)
    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
        if(Math.random()<0.3) {
            World world = player.getEntityWorld();
            EntityPlayer player1 = (EntityPlayer) player;
            RayTraceResult result = rayTrace(world, player1, false);
            if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
                Vec3d v = result.hitVec;
                BlockPos pos = new BlockPos(v.x, v.y, v.z);
                NoTreePunching.proxy.generateParticle(world, pos, EnumParticleTypes.SMOKE_LARGE);
            }
        }
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        EntityPlayer player = (EntityPlayer) entityLiving;

        if(player != null){

            RayTraceResult result = rayTrace(worldIn,player,false);

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
                                break;
                            }
                        }
                        oreList= OreDictionary.getOres("stickWood"); // Add wood types
                        for(ItemStack stickType: oreList){
                            if(sticks > 0 && item == stickType.getItem()){
                                sticks = Math.max(sticks-drop.getItem().getCount(),0);
                                toUse.add(drop);
                                break;
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

                            Item item = drop2.getItem().getItem();

                            if (item == ModItems.grassString && thatch > 0) { // Remove thatch items from world
                                remove = Math.min(drop2.getItem().getCount(), thatch);
                                thatch -= remove;
                                drop2.getItem().shrink(remove);
                                if (drop2.getItem().getCount() == 0) {
                                    drop2.setDead();
                                }
                            }
                            oreList = OreDictionary.getOres("logWood"); // Remove log items from world
                            for (ItemStack logType : oreList) {
                                if (logs > 0 && item == logType.getItem()) {
                                    remove = Math.min(drop2.getItem().getCount(), logs);
                                    logs -= remove;
                                    drop2.getItem().shrink(remove);
                                    if (drop2.getItem().getCount() == 0) {
                                        drop2.setDead();
                                    }
                                    break;
                                }
                            }
                            oreList = OreDictionary.getOres("stickWood"); // Remove stick items from world
                            for (ItemStack stickType : oreList) {
                                if (sticks > 0 && item == stickType.getItem()) {
                                    remove = Math.min(drop2.getItem().getCount(), sticks);
                                    sticks -= remove;
                                    drop2.getItem().shrink(remove);
                                    if (drop2.getItem().getCount() == 0) {
                                        drop2.setDead();
                                    }
                                    break;
                                }
                            }
                        }

                    }
                    else{
                        // No firepit to make, just light the fking place up
                        if(Math.random() < Config.Balance.FIRE_CHANCE){
                            worldIn.setBlockState(pos.up(), Blocks.FIRE.getDefaultState());
                        }
                        stack.damageItem(1,entityLiving);
                    }
                }
            }
        }
        return stack;
    }

}

