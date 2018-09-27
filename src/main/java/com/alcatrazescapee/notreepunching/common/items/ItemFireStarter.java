/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.common.items;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.alcatrazescapee.alcatrazcore.item.tool.ItemToolCore;
import com.alcatrazescapee.alcatrazcore.util.CoreHelpers;
import com.alcatrazescapee.notreepunching.ModConfig;
import com.alcatrazescapee.notreepunching.client.particle.ParticleManager;
import com.alcatrazescapee.notreepunching.common.blocks.ModBlocks;

@ParametersAreNonnullByDefault
public class ItemFireStarter extends ItemToolCore
{
    public ItemFireStarter()
    {
        super(ToolMaterial.WOOD, 0.5f, -2.5f);
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        playerIn.setActiveHand(handIn);
        return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }

    @Override
    @Nonnull
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
        EntityPlayer player = (EntityPlayer) entityLiving;
        if (player != null)
        {
            RayTraceResult result = rayTrace(worldIn, player, false);
            //noinspection ConstantConditions
            if (result == null)
            {
                return stack;
            }

            if (result.typeOfHit == RayTraceResult.Type.BLOCK)
            {
                // If looking at a block
                BlockPos pos = result.getBlockPos();
                if (!worldIn.isRemote)
                {
                    stack.damageItem(1, entityLiving);
                    List<EntityItem> entities = worldIn.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.up(), pos.add(1, 2, 1)));
                    int sticks = 3, logs = 1, thatch = 1;

                    for (EntityItem drop : entities)
                    {
                        ItemStack dropStack = drop.getItem();
                        if (CoreHelpers.doesStackMatchOre(dropStack, "kindling"))
                        {
                            thatch -= dropStack.getCount();
                        }
                        else if (CoreHelpers.doesStackMatchOre(dropStack, "logWood"))
                        {
                            logs -= dropStack.getCount();
                        }
                        else if (CoreHelpers.doesStackMatchOre(dropStack, "stickWood"))
                        {
                            sticks -= dropStack.getCount();
                        }
                    }
                    if (sticks <= 0 && logs <= 0 && thatch <= 0)
                    {
                        // Commence Firepit making
                        worldIn.setBlockState(pos.up(), ModBlocks.FIRE_PIT.getDefaultState());
                        entities.forEach(Entity::setDead);

                    }
                    else
                    {
                        // No firepit to make, try light a fire
                        if (itemRand.nextFloat() < ModConfig.GENERAL.fireStarterFireStartChance)
                        {
                            worldIn.setBlockState(pos.up(), Blocks.FIRE.getDefaultState());
                        }
                    }
                }
            }
        }
        return stack;
    }

    @Override
    @Nonnull
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BOW;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 20;
    }

    @SideOnly(Side.CLIENT)
    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count)
    {
        if (itemRand.nextFloat() < 0.3)
        {
            World world = player.getEntityWorld();
            EntityPlayer player1 = (EntityPlayer) player;
            RayTraceResult result = rayTrace(world, player1, false);
            // noinspection ConstantConditions
            if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK)
            {
                Vec3d v = result.hitVec;
                ParticleManager.generateFireStarterParticle(world, new BlockPos(v));
            }
        }
    }
}
