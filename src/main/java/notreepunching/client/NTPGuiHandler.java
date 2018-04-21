package notreepunching.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import notreepunching.block.firepit.ContainerFirepit;
import notreepunching.block.firepit.GuiFirepit;
import notreepunching.block.firepit.TileEntityFirepit;

public class NTPGuiHandler implements IGuiHandler {

    public static final int FIREPIT = 0;

    @Override
    public Container getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case FIREPIT:
                return new ContainerFirepit(player.inventory, (TileEntityFirepit) world.getTileEntity(new BlockPos(x, y, z)));
            default:
                return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case FIREPIT:
                return new GuiFirepit(getServerGuiElement(ID, player, world, x, y, z), player.inventory, (TileEntityFirepit) world.getTileEntity(new BlockPos(x,y,z)));
            default:
                return null;
        }
    }
}