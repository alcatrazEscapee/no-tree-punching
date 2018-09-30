/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.common.items;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;

import com.alcatrazescapee.alcatrazcore.client.IModelProvider;
import com.alcatrazescapee.notreepunching.ModConfig;

@ParametersAreNonnullByDefault
public class ItemCeramicBucket extends UniversalBucket implements IModelProvider
{
    public ItemCeramicBucket()
    {
        super(Fluid.BUCKET_VOLUME, ItemStack.EMPTY, false);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModel()
    {
        //noinspection ConstantConditions
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(this.getRegistryName().toString()));
    }

    @Override
    public void getSubItems(@Nullable CreativeTabs tab, NonNullList<ItemStack> subItems)
    {
        if (!this.isInCreativeTab(tab)) return;

        subItems.add(new ItemStack(this));

        for (Fluid fluid : FluidRegistry.getRegisteredFluids().values())
        {
            // Add all fluids that the bucket can be filled with
            FluidStack fs = new FluidStack(fluid, getCapacity());
            ItemStack stack = new ItemStack(this);
            IFluidHandlerItem fluidHandler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
            if (fluidHandler != null && fluidHandler.fill(fs, true) == fs.amount)
            {
                ItemStack filled = fluidHandler.getContainer();
                subItems.add(filled);
            }
        }
    }

    @Nonnull
    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("deprecation")
    public String getItemStackDisplayName(ItemStack stack)
    {
        FluidStack fluidStack = getFluid(stack);
        String unlocalisedName = this.getUnlocalizedNameInefficiently(stack);

        // If the bucket is empty, translate the unlocalised name directly
        if (fluidStack == null)
        {
            return I18n.translateToLocal(unlocalisedName + ".name").trim();
        }

        // If there's a fluid-specific translation, use it
        String fluidUnlocalisedName = unlocalisedName + ".filled." + fluidStack.getFluid().getName() + ".name";
        if (I18n.canTranslate(fluidUnlocalisedName))
        {
            return I18n.translateToLocal(fluidUnlocalisedName);
        }

        // Else translate the filled name directly, formatting it with the fluid name
        return I18n.translateToLocalFormatted(unlocalisedName + ".filled.name", fluidStack.getLocalizedName());
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        ItemStack heldItem = player.getHeldItem(hand);
        FluidStack fluidStack = getFluid(heldItem);

        // If the bucket is full, call the super method to try and empty it
        if (fluidStack != null) return super.onItemRightClick(world, player, hand);

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
    }

    @Nullable
    @Override
    public FluidStack getFluid(ItemStack container)
    {
        return FluidUtil.getFluidContained(container);
    }

    @Override
    @Nonnull
    public ItemStack getEmpty()
    {
        return new ItemStack(this);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
    {
        // FluidBucketWrapper only works with Forge's UniversalBucket instance, use a different IFluidHandlerItem implementation instead
        return new FluidHandlerItemStackSimple(stack, Fluid.BUCKET_VOLUME)
        {
            @Override
            public boolean canFillFluidType(FluidStack fluid)
            {
                for (String s : ModConfig.GENERAL.ceramicBucketValidFluids)
                {
                    if (s.equals(fluid.getFluid().getName()))
                    {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    @Nonnull
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.DRINK;
    }

    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 32;
    }
}
