package notreepunching.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import notreepunching.block.tile.TileEntityBellows;

public class PacketUpdateBellows implements IMessage {

    private boolean power;
    private BlockPos pos;

    public PacketUpdateBellows(TileEntityBellows te){
        this(te.getPower(), te.getPos());
    }
    public PacketUpdateBellows(boolean power, BlockPos pos){
        this.power = power;
        this.pos = pos;
    }
    // Needed for forge to call this class
    public PacketUpdateBellows(){}

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(power);
        buf.writeLong(pos.toLong());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        power = buf.readBoolean();
        pos = BlockPos.fromLong(buf.readLong());
    }

    public static class Handler implements IMessageHandler<PacketUpdateBellows, IMessage> {

        @Override
        public IMessage onMessage(PacketUpdateBellows message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                TileEntityBellows te = (TileEntityBellows) Minecraft.getMinecraft().world.getTileEntity(message.pos);
                if(te != null) {
                    te.setPower(message.power);
                }
            });
            return null;
        }

    }
}
