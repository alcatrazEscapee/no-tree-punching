/*
 *
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 *
 */

package notreepunching.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import notreepunching.block.tile.*;
import notreepunching.client.container.*;
import notreepunching.client.gui.*;

public class ModGuiHandler implements IGuiHandler {

    public static final byte FIREPIT = 0;
    public static final byte FORGE = 1;
    public static final byte WOODPILE = 2;
    public static final byte GRINDSTONE = 3;
    public static final byte BLAST_FURNACE = 4;
    public static final byte LARGE_VESSEL = 5;
    public static final byte SMALL_VESSEL = 6;

    @Override
    public Container getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(new BlockPos(x,y,z));
        if(te == null && ID != SMALL_VESSEL) return null;
        switch (ID) {
            case FIREPIT:
                return new ContainerFirepit(player.inventory, (TileEntityFirepit) te);
            case FORGE:
                return new ContainerForge(player.inventory, (TileEntityForge) te);
            case WOODPILE:
                return new ContainerWoodPile(player.inventory, (TileEntityWoodPile) te);
            case GRINDSTONE:
                return new ContainerGrindstone(player.inventory, (TileEntityGrindstone) te);
            case BLAST_FURNACE:
                return new ContainerBlastFurnace(player.inventory, (TileEntityBlastFurnace) te);
            case LARGE_VESSEL:
                return new ContainerLargeVessel(player.inventory, (TileEntityLargeVessel) te);
            case SMALL_VESSEL:
                return new ContainerSmallVessel(player.inventory, player.getHeldItemMainhand());
            default:
                return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(new BlockPos(x,y,z));
        Container container = getServerGuiElement(ID, player, world, x,y,z);
        if(te == null && ID != SMALL_VESSEL) return null;
        switch (ID) {
            case FIREPIT:
                return new GuiFirepit(container, player.inventory, (TileEntityFirepit) te);
            case FORGE:
                return new GuiForge(container, player.inventory, (TileEntityForge) te);
            case WOODPILE:
                return new GuiWoodPile(container, player.inventory);
            case GRINDSTONE:
                return new GuiGrindstone(container, player.inventory, (TileEntityGrindstone) te);
            case BLAST_FURNACE:
                return new GuiBlastFurnace(container, player.inventory, (TileEntityBlastFurnace) te);
            case LARGE_VESSEL:
                return new GuiLargeVessel(container, player.inventory);
            case SMALL_VESSEL:
                return new GuiSmallVessel(container, player.inventory);
            default:
                return null;
        }
    }
}