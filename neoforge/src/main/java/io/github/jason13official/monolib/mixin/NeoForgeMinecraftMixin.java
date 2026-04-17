package io.github.jason13official.monolib.mixin;

import io.github.jason13official.monolib.impl.client.event.MonoLibMinecraftEvents;
import net.minecraft.client.Minecraft;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class NeoForgeMinecraftMixin {

  @Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;gameThread:Ljava/lang/Thread;", shift = Shift.AFTER, ordinal = 0, opcode = Opcodes.PUTFIELD), method = "run")
  private void monolib$clientStarted(CallbackInfo ci) {

    Minecraft self = (Minecraft) (Object) this;
    MonoLibMinecraftEvents.Hooks.onClientStarted(self);
  }
}
