package notreepunching.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import notreepunching.block.tile.TileEntityGrindstone;

public class PacketRequestGrindstone implements IMessage {

    private BlockPos pos;
    private int dimension;

    public PacketRequestGrindstone(BlockPos pos, int dimension) {
        this.pos = pos;
        this.dimension = dimension;
    }
    public PacketRequestGrindstone(TileEntityGrindstone te) {
        this(te.getPos(), te.getWorld().provider.getDimension());
    }
    // Needs to be here for forge
    public PacketRequestGrindstone() { }

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

    public static class Handler implements IMessageHandler<PacketRequestGrindstone, PacketUpdateGrindstone> {

        @Override
        public PacketUpdateGrindstone onMessage(PacketRequestGrindstone message, MessageContext ctx) {
            World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
            TileEntityGrindstone te = (TileEntityGrindstone) world.getTileEntity(message.pos);
            if (te != null) {
                return new PacketUpdateGrindstone(te);
            } else {
                return null;
            }
        }

    }

}
