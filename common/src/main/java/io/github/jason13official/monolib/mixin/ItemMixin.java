package io.github.jason13official.monolib.mixin;

import io.github.jason13official.monolib.Constants;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Item.class)
public class ItemMixin {

  @Inject(at = @At("TAIL"), method = "<clinit>")
  private static void init(CallbackInfo info) {

    Constants.LOG.info("Mixins applied to net.minecraft.world.item.Item");
  }
}