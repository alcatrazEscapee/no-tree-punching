package com.alcatrazescapee.notreepunching.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.alcatrazescapee.notreepunching.EventHandler;

@Mixin(ServerPlayerGameMode.class)
public abstract class ServerPlayerGameModeMixin
{
    @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
    private void triggerFlintKnapping(ServerPlayer player, Level level, ItemStack stack, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir)
    {
        final InteractionResult result = EventHandler.onRightClickBlock(level, hit.getBlockPos(), player, hand, stack, hit.getDirection());
        if (result != null)
        {
            cir.setReturnValue(result);
        }
    }
}
