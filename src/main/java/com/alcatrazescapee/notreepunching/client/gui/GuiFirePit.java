/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.client.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.alcatrazescapee.core.client.gui.GuiContainerTileCore;
import com.alcatrazescapee.notreepunching.common.tile.TileFirePit;

@SideOnly(Side.CLIENT)
public class GuiFirePit extends GuiContainerTileCore<TileFirePit>
{
    public GuiFirePit(TileFirePit tile, Container container, InventoryPlayer playerInv, ResourceLocation background, String titleKey)
    {
        super(tile, container, playerInv, background, titleKey);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        // Draw the fire / burn time indicator
        int burnTime = tile.getScaledBurnTicks();
        int cookTime = tile.getScaledCookTime();
        if (burnTime > 0)
        {
            drawTexturedModalRect(x + 81, y + 54 - burnTime, 176, 13 - burnTime, 14, burnTime);
        }
        if (cookTime > 0)
        {
            drawTexturedModalRect(x + 77, y + 23, 190, 0, cookTime, 16);
        }
    }
}
