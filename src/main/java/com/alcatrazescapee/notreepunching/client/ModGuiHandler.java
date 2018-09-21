/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.client;

import javax.annotation.Nullable;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import com.alcatrazescapee.alcatrazcore.client.gui.GuiContainerCore;
import com.alcatrazescapee.alcatrazcore.util.CoreHelpers;
import com.alcatrazescapee.notreepunching.common.blocks.ModBlocks;
import com.alcatrazescapee.notreepunching.common.container.ContainerLargeVessel;
import com.alcatrazescapee.notreepunching.common.container.ContainerSmallVessel;
import com.alcatrazescapee.notreepunching.common.items.ItemSmallVessel;
import com.alcatrazescapee.notreepunching.common.tile.TileLargeVessel;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

public class ModGuiHandler implements IGuiHandler
{
    public static final int LARGE_VESSEL = 0;
    public static final int SMALL_VESSEL = 1;
    public static final int FIRE_PIT = 2;

    private static final ResourceLocation LARGE_VESSEL_BACKGROUND = new ResourceLocation(MOD_ID, "textures/gui/large_vessel.png");
    private static final ResourceLocation SMALL_VESSEL_BACKGROUND = new ResourceLocation(MOD_ID, "textures/gui/small_vessel.png");

    @Nullable
    @Override
    public Container getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        switch (ID)
        {
            case LARGE_VESSEL:
                TileLargeVessel teLargeVessel = CoreHelpers.getTE(world, new BlockPos(x, y, z), TileLargeVessel.class);
                return new ContainerLargeVessel(player.inventory, teLargeVessel);
            case SMALL_VESSEL:
                ItemStack stack = player.getHeldItemMainhand();
                if (!(stack.getItem() instanceof ItemSmallVessel))
                    stack = player.getHeldItemOffhand();
                return new ContainerSmallVessel(player.inventory, stack);
            case FIRE_PIT:
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public GuiContainer getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        switch (ID)
        {
            case LARGE_VESSEL:
                return new GuiContainerCore(getServerGuiElement(ID, player, world, x, y, z), player.inventory, LARGE_VESSEL_BACKGROUND, ModBlocks.CERAMIC_LARGE_VESSEL.getTranslationKey());
            case SMALL_VESSEL:
                ItemStack stack = player.getHeldItemMainhand();
                if (!(stack.getItem() instanceof ItemSmallVessel))
                    stack = player.getHeldItemOffhand();
                return new GuiContainerCore(getServerGuiElement(ID, player, world, x, y, z), player.inventory, SMALL_VESSEL_BACKGROUND, stack.getTranslationKey());
            case FIRE_PIT:
            default:
                return null;
        }
    }
}