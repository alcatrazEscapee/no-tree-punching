package notreepunching.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import notreepunching.block.tile.TileEntityBellows;
import notreepunching.block.tile.TileEntityGrindstone;

public class PacketUpdateGrindstone implements IMessage {

    private int rotation;
    private boolean hasWheel;
    private BlockPos pos;

    public PacketUpdateGrindstone(TileEntityGrindstone te){
        this(te.getPos(), te.getRotation(), te.getHasWheel());
    }
    public PacketUpdateGrindstone(BlockPos pos, int rotation, boolean hasWheel){
        this.hasWheel = hasWheel;
        this.pos = pos;
        this.rotation = rotation;
    }
    // Needed for forge to call this class
    public PacketUpdateGrindstone(){}

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(hasWheel);
        buf.writeLong(pos.toLong());
        buf.writeInt(rotation);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        hasWheel = buf.readBoolean();
        pos = BlockPos.fromLong(buf.readLong());
        rotation = buf.readInt();
    }

    public static class Handler implements IMessageHandler<PacketUpdateGrindstone, IMessage> {

        @Override
        public IMessage onMessage(PacketUpdateGrindstone message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                TileEntityGrindstone te = (TileEntityGrindstone) Minecraft.getMinecraft().world.getTileEntity(message.pos);
                if(te != null) {
                    te.setRotation(message.rotation);
                    te.setHasWheel(message.hasWheel);
                }
            });
            return null;
        }

    }
}
