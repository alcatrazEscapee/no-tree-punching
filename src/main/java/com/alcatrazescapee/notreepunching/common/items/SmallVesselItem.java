/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Copyright (c) 2019. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.items;

import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
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
import com.alcatrazescapee.notreepunching.Config;
import com.alcatrazescapee.notreepunching.common.ModItemGroups;
import com.alcatrazescapee.notreepunching.common.container.SmallVesselContainer;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

public class SmallVesselItem extends Item
{
    public static final int SLOT_ROWS = 3, SLOT_COLUMNS = 3, SLOTS = SLOT_ROWS * SLOT_COLUMNS;

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

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt)
    {
        return new SmallVesselItemStackHandler(stack, SLOTS);
    }

    static final class SmallVesselItemStackHandler extends ItemStackItemHandler implements INamedContainerProvider
    {
        SmallVesselItemStackHandler(ItemStack stack, int slots)
        {
            super(stack, slots);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack)
        {
            return !Config.SERVER.smallCeramicVesselBlacklist.get().test(stack);
        }

        @Override
        public ITextComponent getDisplayName()
        {
            return stack.getDisplayName();
        }

        @Override
        public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player)
        {
            return new SmallVesselContainer(windowId, playerInventory);
        }
    }
}
