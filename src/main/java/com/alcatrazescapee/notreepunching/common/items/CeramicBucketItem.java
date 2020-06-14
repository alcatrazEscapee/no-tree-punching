/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.common.items;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ForgeRegistries;

import com.alcatrazescapee.notreepunching.Config;
import com.alcatrazescapee.notreepunching.NoTreePunching;
import com.alcatrazescapee.notreepunching.common.ModTags;

public class CeramicBucketItem extends Item
{
    public CeramicBucketItem(Properties properties)
    {
        super(properties);
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items)
    {
        if (isInGroup(group))
        {
            items.add(new ItemStack(this));
            for (Fluid fluid : ForgeRegistries.FLUIDS.getValues())
            {
                if (ModTags.Fluids.CERAMIC_BUCKETABLE.contains(fluid))
                {
                    ItemStack stack = new ItemStack(this);
                    stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(handler -> {
                        FluidStack fluidStack = new FluidStack(fluid, 1000);
                        if (handler.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE) > 0)
                        {
                            items.add(stack);
                        }
                    });
                }
            }
        }
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack)
    {
        return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).map(handler -> {
            if (handler instanceof FluidHandlerItemStackSimple)
            {
                ITextComponent fluidName = ((FluidHandlerItemStackSimple) handler).getFluid().getDisplayName();
                fluidName.appendSibling(super.getDisplayName(stack));
                return fluidName;
            }
            return super.getDisplayName(stack);
        }).orElseThrow(() -> new IllegalStateException("No fluid handler on ceramic bucket?"));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        // todo: fluid interactions
        /*
        ItemStack heldItem = player.getHeldItem(hand);
        FluidStack fluidStack = getFluid(heldItem);
        ActionResult<ItemStack> result;

        // If the bucket is full, call the super method to try and empty it
        if (fluidStack != null)
        {
            result = super.onItemRightClick(world, player, hand);
            if (result.getType() == EnumActionResult.PASS)
            {
                player.setActiveHand(hand);
                return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
            }
            else
            {
                return result;
            }
        }
        else
        {
            // If the bucket is empty, try and fill it
            RayTraceResult target = this.rayTrace(world, player, true);

            //noinspection ConstantConditions
            if (target == null || target.typeOfHit != RayTraceResult.Type.BLOCK)
            {
                return new ActionResult<>(EnumActionResult.PASS, heldItem);
            }

            BlockPos pos = target.getBlockPos();

            ItemStack singleBucket = heldItem.copy();
            singleBucket.setCount(1);

            FluidActionResult filledResult = FluidUtil.tryPickUpFluid(singleBucket, player, world, pos, target.sideHit);
            if (filledResult.isSuccess())
            {
                ItemStack filledBucket = filledResult.result;

                if (player.capabilities.isCreativeMode)
                    return new ActionResult<>(EnumActionResult.SUCCESS, heldItem);

                heldItem.shrink(1);
                if (heldItem.isEmpty())
                    return new ActionResult<>(EnumActionResult.SUCCESS, filledBucket);

                ItemHandlerHelper.giveItemToPlayer(player, filledBucket);

                return new ActionResult<>(EnumActionResult.SUCCESS, heldItem);
            }
            return new ActionResult<>(EnumActionResult.PASS, heldItem);
        }*/
        return new ActionResult<>(ActionResultType.PASS, playerIn.getHeldItem(handIn));
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt)
    {
        return new FluidHandlerItemStackSimple(stack, 1000);
    }
}
