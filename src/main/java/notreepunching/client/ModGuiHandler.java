package notreepunching.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import notreepunching.block.tile.TileEntityGrindstone;
import notreepunching.block.tile.TileEntityWoodPile;
import notreepunching.client.container.ContainerFirepit;
import notreepunching.client.container.ContainerForge;
import notreepunching.block.tile.TileEntityForge;
import notreepunching.client.container.ContainerGrindstone;
import notreepunching.client.container.ContainerWoodPile;
import notreepunching.client.gui.GuiFirepit;
import notreepunching.block.tile.TileEntityFirepit;
import notreepunching.client.gui.GuiForge;
import notreepunching.client.gui.GuiGrindstone;
import notreepunching.client.gui.GuiWoodPile;

public class ModGuiHandler implements IGuiHandler {

    public static final byte FIREPIT = 0;
    public static final byte FORGE = 1;
    public static final byte WOODPILE = 2;
    public static final byte GRINDSTONE = 3;

    @Override
    public Container getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(new BlockPos(x,y,z));
        if(te == null) return null;
        switch (ID) {
            case FIREPIT:
                return new ContainerFirepit(player.inventory, (TileEntityFirepit) te);
            case FORGE:
                return new ContainerForge(player.inventory, (TileEntityForge) te);
            case WOODPILE:
                return new ContainerWoodPile(player.inventory, (TileEntityWoodPile) te);
            case GRINDSTONE:
                return new ContainerGrindstone(player.inventory, (TileEntityGrindstone) te);
            default:
                return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(new BlockPos(x,y,z));
        if(te == null) return null;
        switch (ID) {
            case FIREPIT:
                return new GuiFirepit(getServerGuiElement(ID, player, world, x, y, z), player.inventory, (TileEntityFirepit) te);
            case FORGE:
                return new GuiForge(getServerGuiElement(ID, player, world, x,y,z), player.inventory, (TileEntityForge) te);
            case WOODPILE:
                return new GuiWoodPile(getServerGuiElement(ID, player, world, x,y,z), player.inventory, (TileEntityWoodPile) te);
            case GRINDSTONE:
                return new GuiGrindstone(getServerGuiElement(ID, player, world, x,y,z), player.inventory, (TileEntityGrindstone) te);
            default:
                return null;
        }
    }
}