/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

public final class ModSounds
{
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MOD_ID);

    public static final RegistryObject<SoundEvent> KNAPPING = register("knapping");

    @SuppressWarnings("SameParameterValue")
    private static RegistryObject<SoundEvent> register(String name)
    {
        return SOUNDS.register(name, () -> new SoundEvent(new ResourceLocation(MOD_ID, name)));
    }
}