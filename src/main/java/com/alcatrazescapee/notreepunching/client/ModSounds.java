package com.alcatrazescapee.notreepunching.client;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import com.alcatrazescapee.notreepunching.platform.RegistryHolder;
import com.alcatrazescapee.notreepunching.platform.RegistryInterface;
import com.alcatrazescapee.notreepunching.platform.XPlatform;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

public final class ModSounds
{
    public static final RegistryInterface<SoundEvent> SOUNDS = XPlatform.INSTANCE.registryInterface(Registry.SOUND_EVENT);

    public static final RegistryHolder<SoundEvent> KNAPPING = register("knapping");

    @SuppressWarnings("SameParameterValue")
    private static RegistryHolder<SoundEvent> register(String name)
    {
        return SOUNDS.register(name, () -> new SoundEvent(new ResourceLocation(MOD_ID, name)));
    }
}