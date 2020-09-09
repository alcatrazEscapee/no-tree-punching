/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.items;

import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;

import com.alcatrazescapee.notreepunching.common.ModTags;
import com.alcatrazescapee.notreepunching.common.tileentity.LargeVesselTileEntity;
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
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt)
    {
        // This tag is used because minecraft expects it
        if (nbt != null && nbt.contains("BlockEntityTag"))
        {
            nbt = nbt.getCompound("BlockEntityTag");
        }
        return new LargeVesselItemHandler(nbt, stack, LargeVesselTileEntity.SLOTS);
    }

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
                    TranslationTextComponent textComponent = new TranslationTextComponent(MOD_ID + ".tooltip.small_vessel_more", totalCount - displayCount);
                    textComponent.setStyle(textComponent.getStyle().applyFormat(TextFormatting.ITALIC));
                    tooltip.add(textComponent);
                }
            });
        }
    }

    static class LargeVesselItemHandler extends ItemStackItemHandler
    {
        LargeVesselItemHandler(@Nullable CompoundNBT capNbt, ItemStack stack, int slots)
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