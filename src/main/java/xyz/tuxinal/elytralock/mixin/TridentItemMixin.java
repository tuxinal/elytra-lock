package xyz.tuxinal.elytralock.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.world.item.TridentItem;
import xyz.tuxinal.elytralock.ElytraLock;

@Mixin(TridentItem.class)
public class TridentItemMixin {
    @Inject(method = "releaseUsing(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;push(DDD)V"))
    private void inject(CallbackInfo ci) {
        var config = ElytraLock.getConfig();
        if (!(config.enabled && config.unlockUponRiptideThrow)) {
            return;
        }
        ElytraLock.locked = false;
    }
}
