/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.common.items;

import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;

import com.alcatrazescapee.core.common.inventory.ItemStackItemHandler;
import com.alcatrazescapee.notreepunching.common.ModItemGroups;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

public class SmallVesselItem extends Item
{
    public SmallVesselItem()
    {
        super(new Properties().group(ModItemGroups.ITEMS).maxStackSize(1));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if (playerIn instanceof ServerPlayerEntity && !playerIn.isSneaking())
        {
            stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
                if (handler instanceof INamedContainerProvider)
                {
                    NetworkHooks.openGui((ServerPlayerEntity) playerIn, (INamedContainerProvider) handler);
                }
            });
        }
        return new ActionResult<>(ActionResultType.SUCCESS, stack);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt)
    {
        return new ItemStackItemHandler(stack, 9);
    }

    /**
     * Copy pasta from {@link net.minecraft.block.ShulkerBoxBlock#addInformation(ItemStack, IBlockReader, List, ITooltipFlag)}
     */
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        if (CapabilityItemHandler.ITEM_HANDLER_CAPABILITY != null)
        {
            stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(handler -> {
                int displayCount = 0;
                int totalCount = 0;

                for (int i = 0; i < handler.getSlots(); i++)
                {
                    ItemStack contentStack = handler.getStackInSlot(i);
                    if (!contentStack.isEmpty())
                    {
                        ++totalCount;
                        if (displayCount <= 4)
                        {
                            ++displayCount;
                            ITextComponent itextcomponent = contentStack.getDisplayName().deepCopy();
                            itextcomponent.appendText(" x").appendText(String.valueOf(contentStack.getCount()));
                            tooltip.add(itextcomponent);
                        }
                    }
                }

                if (totalCount > displayCount)
                {
                    tooltip.add((new TranslationTextComponent(MOD_ID + ".tooltip.small_vessel_more", totalCount - displayCount)).applyTextStyle(TextFormatting.ITALIC));
                }
            });
        }
    }
}
