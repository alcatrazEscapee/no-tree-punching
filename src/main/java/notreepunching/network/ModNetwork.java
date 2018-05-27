package notreepunching.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class ModNetwork {

    public static final SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel("ntp");

    public static void preInit(){
        int id = 0;

        // Bellows
        network.registerMessage(PacketUpdateBellows.Handler.class, PacketUpdateBellows.class, ++id, Side.CLIENT);
        network.registerMessage(PacketRequestBellows.Handler.class, PacketRequestBellows.class, ++id, Side.SERVER);

        // Grindstone
        network.registerMessage(PacketUpdateGrindstone.Handler.class, PacketUpdateGrindstone.class, ++id, Side.CLIENT);
        network.registerMessage(PacketRequestGrindstone.Handler.class, PacketRequestGrindstone.class, ++id, Side.SERVER);

    }

}
