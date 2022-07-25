package com.alcatrazescapee.notreepunching.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.alcatrazescapee.notreepunching.EventHandler;
import com.alcatrazescapee.notreepunching.util.Helpers;

@Mixin(Player.class)
public abstract class PlayerMixin
{
    @Inject(method = "hasCorrectToolForDrops", at = @At("TAIL"), cancellable = true)
    private void checkUsingCorrectToolForHarvestCheck(BlockState state, CallbackInfoReturnable<Boolean> cir)
    {
        final boolean oldValue = cir.getReturnValue();
        final boolean newValue = EventHandler.modifyHarvestCheck(Helpers.cast(this), state, null, oldValue);
        if (oldValue != newValue)
        {
            cir.setReturnValue(newValue);
        }
    }

    @Inject(method = "getDestroySpeed", at = @At("TAIL"), cancellable = true)
    private void modifyBreakSpeed(BlockState state, CallbackInfoReturnable<Float> cir)
    {
        final float oldSpeed = cir.getReturnValue();
        final float newSpeed = EventHandler.modifyBreakSpeed(Helpers.cast(this), state, null, oldSpeed);
        if (oldSpeed != newSpeed)
        {
            cir.setReturnValue(newSpeed);
        }
    }
}
