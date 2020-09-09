/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.items;

import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;

import com.alcatrazescapee.notreepunching.common.ModItemGroup;
import com.alcatrazescapee.notreepunching.common.ModTags;
import com.alcatrazescapee.notreepunching.common.container.SmallVesselContainer;
import com.alcatrazescapee.notreepunching.util.Helpers;
import com.alcatrazescapee.notreepunching.util.ItemStackItemHandler;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

public class SmallVesselItem extends Item
{
    public static final int SLOT_ROWS = 3, SLOT_COLUMNS = 3, SLOTS = SLOT_ROWS * SLOT_COLUMNS;

    public SmallVesselItem()
    {
        super(new Properties().tab(ModItemGroup.ITEMS).stacksTo(1));
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        ItemStack stack = playerIn.getItemInHand(handIn);
        if (playerIn instanceof ServerPlayerEntity && !playerIn.isShiftKeyDown())
        {
            stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> NetworkHooks.openGui((ServerPlayerEntity) playerIn, Helpers.getStackContainerProvider(stack, (windowID, playerInventory, player) -> new SmallVesselContainer(windowID, playerInventory))));
        }
        return new ActionResult<>(ActionResultType.SUCCESS, stack);
    }

    /**
     * Copy pasta from {@link net.minecraft.block.ShulkerBoxBlock#appendHoverText(ItemStack, IBlockReader, List, ITooltipFlag)}
     */
    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
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
                            IFormattableTextComponent textComponent = contentStack.getHoverName().plainCopy();
                            textComponent.append(" x").append(String.valueOf(contentStack.getCount()));
                            tooltip.add(textComponent);
                        }
                    }
                }

                if (totalCount > displayCount)
                {
                    IFormattableTextComponent textComponent = new TranslationTextComponent(MOD_ID + ".tooltip.small_vessel_more", totalCount - displayCount);
                    textComponent.setStyle(textComponent.getStyle().applyFormat(TextFormatting.ITALIC));
                    tooltip.add(textComponent);
                }
            });
        }
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt)
    {
        return new SmallVesselItemStackHandler(nbt, stack, SLOTS);
    }

    static final class SmallVesselItemStackHandler extends ItemStackItemHandler
    {
        SmallVesselItemStackHandler(@Nullable CompoundNBT capNbt, ItemStack stack, int slots)
        {
            super(capNbt, stack, slots);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack)
        {
            return !ModTags.Items.SMALL_VESSEL_BLACKLIST.contains(stack.getItem());
        }
    }
}