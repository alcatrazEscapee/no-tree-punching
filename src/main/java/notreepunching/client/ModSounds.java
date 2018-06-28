/*
 *
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 *
 */

package notreepunching.client;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import notreepunching.NoTreePunching;
import notreepunching.registry.RegistryHandler;

//@ObjectHolder(NoTreePunching.MODID)
public class ModSounds {

    //@ObjectHolder("knapping")
    public static SoundEvent flintKnapping;
    //@ObjectHolder("bellows_out")
    public static SoundEvent bellowsOut;
    //@ObjectHolder("bellows_in")
    public static SoundEvent bellowsIn;
    //@ObjectHolder("grindstone")
    public static SoundEvent grindstone;

    public static void init(){
        flintKnapping = registerSound("knapping");

        bellowsOut = registerSound("bellows_out");
        bellowsIn = registerSound("bellows_in");

        grindstone = registerSound("grindstone");
    }

    private static SoundEvent registerSound(String name){
        ResourceLocation location = new ResourceLocation(NoTreePunching.MODID,name);
        SoundEvent sound = new SoundEvent(location).setRegistryName(location);
        RegistryHandler.SOUND_REGISTRY.add(sound);
        return sound;
    }
}
