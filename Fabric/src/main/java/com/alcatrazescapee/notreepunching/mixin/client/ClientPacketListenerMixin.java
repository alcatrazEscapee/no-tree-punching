package com.alcatrazescapee.notreepunching.mixin.client;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundUpdateTagsPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.alcatrazescapee.notreepunching.util.HarvestBlockHandler;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin
{
    @Inject(method = "handleUpdateTags", at = @At("RETURN"))
    private void onUpdateTagsOnClient(ClientboundUpdateTagsPacket packet, CallbackInfo ci)
    {
        HarvestBlockHandler.inferToolTypesFromTags();
    }
}
