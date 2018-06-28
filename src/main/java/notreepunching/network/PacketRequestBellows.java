/*
 *
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 *
 */

package notreepunching.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import notreepunching.block.tile.TileEntityBellows;

public class PacketRequestBellows implements IMessage {

    private BlockPos pos;
    private int dimension;

    public PacketRequestBellows(BlockPos pos, int dimension) {
        this.pos = pos;
        this.dimension = dimension;
    }

    public PacketRequestBellows(TileEntityBellows te) {
        this(te.getPos(), te.getWorld().provider.getDimension());
    }

    // Needs to be here for forge
    public PacketRequestBellows() { }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        buf.writeInt(dimension);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
        dimension = buf.readInt();
    }

    public static class Handler implements IMessageHandler<PacketRequestBellows, PacketUpdateBellows> {

        @Override
        public PacketUpdateBellows onMessage(PacketRequestBellows message, MessageContext ctx) {
            World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
            TileEntityBellows te = (TileEntityBellows) world.getTileEntity(message.pos);
            if (te != null) {
                return new PacketUpdateBellows(te);
            } else {
                return null;
            }
        }

    }

}
