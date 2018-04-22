package notreepunching.client.sound;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import notreepunching.NoTreePunching;

@ObjectHolder(NoTreePunching.MODID)
public class Sounds {

    @ObjectHolder("knapping")
    public static final SoundEvent FLINT_KNAPPING = registerSound("knapping");

    private static SoundEvent registerSound(String name){
        ResourceLocation location = new ResourceLocation(NoTreePunching.MODID,name);
        SoundEvent sound = new SoundEvent(location).setRegistryName(location);
        return sound;
    }

    @Mod.EventBusSubscriber
    public static class RegistrationHandler {
        @SubscribeEvent
        public static void registerSoundEvents(RegistryEvent.Register<SoundEvent> event) {
            event.getRegistry().registerAll(
                    FLINT_KNAPPING
            );
        }
    }
}
