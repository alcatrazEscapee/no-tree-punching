package notreepunching.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class ModNetwork {

    public static final SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel("ntp");

    public static void preInit(){
        // Bellows
        network.registerMessage(PacketUpdateBellows.Handler.class, PacketUpdateBellows.class, 0, Side.CLIENT);
        network.registerMessage(PacketRequestBellows.Handler.class, PacketRequestBellows.class, 1, Side.SERVER);

    }

}
