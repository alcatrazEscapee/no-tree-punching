/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package notreepunching.client;

import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import alcatrazcore.util.RegistryHelper;

import static alcatrazcore.util.CoreHelpers.getNull;
import static notreepunching.NoTreePunching.MOD_ID;

@GameRegistry.ObjectHolder(MOD_ID)
public class ModSounds
{
    public static final SoundEvent KNAPPING = getNull();
    public static final SoundEvent BELLOWS_OUT = getNull();
    public static final SoundEvent BELLOWS_IN = getNull();
    public static final SoundEvent GRINDSTONE = getNull();

    public static void preInit()
    {
        RegistryHelper r = RegistryHelper.get(MOD_ID);

        r.registerSound("knapping");
        r.registerSound("bellows_out");
        r.registerSound("bellows_in");
        r.registerSound("grindstone");
    }
}
