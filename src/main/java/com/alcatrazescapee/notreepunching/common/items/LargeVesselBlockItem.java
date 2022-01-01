/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.items;

import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;

import com.alcatrazescapee.notreepunching.common.ModTags;
import com.alcatrazescapee.notreepunching.common.blockentity.LargeVesselBlockEntity;
import com.alcatrazescapee.notreepunching.util.ItemStackItemHandler;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

public class LargeVesselBlockItem extends BlockItem
{
    public LargeVesselBlockItem(Block blockIn, Properties builder)
    {
        super(blockIn, builder);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt)
    {
        // This tag is used because minecraft expects it
        if (nbt != null && nbt.contains("BlockEntityTag"))
        {
            nbt = nbt.getCompound("BlockEntityTag");
        }
        return new LargeVesselItemHandler(nbt, stack, LargeVesselBlockEntity.SLOTS);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
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
                            MutableComponent textComponent = contentStack.getHoverName().plainCopy();
                            textComponent.append(" x").append(String.valueOf(contentStack.getCount()));
                            tooltip.add(textComponent);
                        }
                    }
                }

                if (totalCount > displayCount)
                {
                    TranslatableComponent textComponent = new TranslatableComponent(MOD_ID + ".tooltip.small_vessel_more", totalCount - displayCount);
                    textComponent.setStyle(textComponent.getStyle().applyFormat(ChatFormatting.ITALIC));
                    tooltip.add(textComponent);
                }
            });
        }
    }

    static class LargeVesselItemHandler extends ItemStackItemHandler
    {
        LargeVesselItemHandler(@Nullable CompoundTag capNbt, ItemStack stack, int slots)
        {
            super(capNbt, stack, slots);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack)
        {
            return ModTags.Items.LARGE_VESSEL_BLACKLIST.contains(stack.getItem());
        }
    }
}