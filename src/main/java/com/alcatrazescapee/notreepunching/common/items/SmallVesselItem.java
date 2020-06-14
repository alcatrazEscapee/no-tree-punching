/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.common.items;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import com.alcatrazescapee.notreepunching.NoTreePunching;
import com.alcatrazescapee.notreepunching.client.ModGuiHandler;
import com.alcatrazescapee.notreepunching.common.ModItemGroups;

public class SmallVesselItem extends Item
{
    public SmallVesselItem()
    {
        super(new Properties().group(ModItemGroups.ITEMS).maxStackSize(1));
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if (!worldIn.isRemote)
        {
            if (!playerIn.isSneaking())
            {
                playerIn.openGui(NoTreePunching.getInstance(), ModGuiHandler.SMALL_VESSEL, worldIn, 0, 0, 0);
            }
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
    {
        return new ItemHandlerItem(nbt, 4);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        IItemHandler cap = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (cap != null)
        {
            boolean flag = true;
            for (int i = 0; i < cap.getSlots(); i++)
            {
                ItemStack stack1 = cap.getStackInSlot(i);
                if (!stack1.isEmpty())
                {
                    if (flag)
                    {
                        tooltip.add(TextFormatting.GREEN + "Contents:");
                        flag = false;
                    }
                    tooltip.add(String.format(TextFormatting.GRAY + "%dx %s", stack1.getCount(), stack1.getDisplayName()));
                }
            }
        }
    }
}
