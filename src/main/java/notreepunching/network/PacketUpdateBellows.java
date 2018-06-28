/*
 *
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 *
 */

package notreepunching.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import notreepunching.block.tile.TileEntityBellows;

public class PacketUpdateBellows implements IMessage {

    private double step;
    private double height;
    private int facing;
    private BlockPos pos;

    public PacketUpdateBellows(TileEntityBellows te){
        this(te.getPos(), te.getFacing(), te.getStep(), te.getHeight());
    }

    public PacketUpdateBellows(BlockPos pos, int facing, double step, double height){
        this.step = step;
        this.height = height;
        this.pos = pos;
        this.facing = facing;
    }

    // Needed for forge to call this class
    public PacketUpdateBellows(){}

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(step);
        buf.writeDouble(height);
        buf.writeLong(pos.toLong());
        buf.writeInt(facing);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        step = buf.readDouble();
        height = buf.readDouble();
        pos = BlockPos.fromLong(buf.readLong());
        facing = buf.readInt();
    }

    public static class Handler implements IMessageHandler<PacketUpdateBellows, IMessage> {

        @Override
        public IMessage onMessage(PacketUpdateBellows message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                TileEntityBellows te = (TileEntityBellows) Minecraft.getMinecraft().world.getTileEntity(message.pos);
                if(te != null) {
                    te.setStep(message.step);
                    te.setFacing(message.facing);
                    te.setHeight(message.height);
                }
            });
            return null;
        }

    }
}
