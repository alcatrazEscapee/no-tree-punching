package com.alcatrazescapee.notreepunching.common.items;

import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import com.alcatrazescapee.notreepunching.common.blockentity.LargeVesselBlockEntity;
import com.alcatrazescapee.notreepunching.util.Helpers;
import com.alcatrazescapee.notreepunching.util.inventory.ItemStackInventory;
import com.alcatrazescapee.notreepunching.util.inventory.ItemStackListInventory;

public class LargeVesselBlockItem extends BlockItem
{
    public LargeVesselBlockItem(Block blockIn, Properties builder)
    {
        super(blockIn, builder);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag)
    {
        final CompoundTag rootTag = stack.getTag();
        if (rootTag != null && rootTag.contains("BlockEntityTag", Tag.TAG_COMPOUND))
        {
            final CompoundTag blockEntityTag = rootTag.getCompound("BlockEntityTag");
            final ItemStackInventory inventory = ItemStackListInventory.create(LargeVesselBlockEntity.SLOTS, blockEntityTag);
            Helpers.addInventoryTooltip(inventory, tooltip);
        }
    }
}