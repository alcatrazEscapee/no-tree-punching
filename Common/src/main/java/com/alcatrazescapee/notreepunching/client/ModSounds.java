package com.alcatrazescapee.notreepunching.client;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;

import com.alcatrazescapee.notreepunching.platform.RegistryHolder;
import com.alcatrazescapee.notreepunching.platform.RegistryInterface;
import com.alcatrazescapee.notreepunching.platform.XPlatform;
import com.alcatrazescapee.notreepunching.util.Helpers;

public final class ModSounds
{
    public static final RegistryInterface<SoundEvent> SOUNDS = XPlatform.INSTANCE.registryInterface(BuiltInRegistries.SOUND_EVENT);

    public static final RegistryHolder<SoundEvent> KNAPPING = register("knapping");

    @SuppressWarnings("SameParameterValue")
    private static RegistryHolder<SoundEvent> register(String name)
    {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(Helpers.identifier(name)));
    }
}