package xyz.tuxinal.elytralock.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.player.Player;

import xyz.tuxinal.elytralock.ElytraLock;

@Mixin(Player.class)
@Environment(EnvType.CLIENT)
public class PlayerMixin {
    @Inject(method = "tryToStartFallFlying()Z", at = @At("HEAD"), cancellable = true)
    public void injected(CallbackInfoReturnable<Boolean> info) {
        if (!ElytraLock.getConfig().enabled) {
            return;
        }
        if (ElytraLock.locked) {
            info.setReturnValue(false);
        }
    }
}
