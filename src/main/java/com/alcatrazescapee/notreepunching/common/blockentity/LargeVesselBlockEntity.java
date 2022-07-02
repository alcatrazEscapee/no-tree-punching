package com.alcatrazescapee.notreepunching.common.blockentity;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.block.state.BlockState;

import com.alcatrazescapee.notreepunching.common.ModTags;
import com.alcatrazescapee.notreepunching.common.container.LargeVesselContainer;
import com.alcatrazescapee.notreepunching.util.Helpers;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

public class LargeVesselBlockEntity extends InventoryBlockEntity
{
    public static final int SLOT_COLUMNS = 5;
    public static final int SLOT_ROWS = 3;
    public static final int SLOTS = SLOT_COLUMNS * SLOT_ROWS;

    private static final Component NAME = new TranslatableComponent(MOD_ID + ".tile_entity.large_vessel");

    public LargeVesselBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.LARGE_VESSEL.get(), pos, state, SLOTS, NAME);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player)
    {
        return new LargeVesselContainer(this, playerInventory, windowId);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack)
    {
        return !Helpers.isItem(stack.getItem(), ModTags.Items.LARGE_VESSEL_BLACKLIST);
    }

    public boolean isEmpty()
    {
        for (int i = 0; i < inventory.getSlots(); i++)
        {
            if (!inventory.getStackInSlot(i).isEmpty())
            {
                return false;
            }
        }
        return true;
    }
}