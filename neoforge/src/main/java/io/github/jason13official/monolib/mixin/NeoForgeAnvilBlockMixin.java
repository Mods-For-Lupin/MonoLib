package io.github.jason13official.monolib.mixin;

import io.github.jason13official.monolib.impl.common.event.MonoLibNeoForgeAnvilBlockEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilBlock.class)
public class NeoForgeAnvilBlockMixin {

  @Inject(method = "onLand(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/entity/item/FallingBlockEntity;)V", at = @At("TAIL"))
  public void monolib$onLand(Level level, BlockPos pos, BlockState state, BlockState replaceableState, FallingBlockEntity fallingBlock, CallbackInfo ci) {
    AnvilBlock instance = (AnvilBlock) (Object) this;
    MonoLibNeoForgeAnvilBlockEvents.Hooks.onLand(instance, level, pos, state, replaceableState, fallingBlock);
  }

  @Inject(method = "onBrokenAfterFall(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/item/FallingBlockEntity;)V", at = @At("TAIL"))
  public void monolib$onBrokenAfterFall(Level level, BlockPos pos, FallingBlockEntity fallingBlock, CallbackInfo ci) {
    AnvilBlock instance = (AnvilBlock) (Object) this;
    MonoLibNeoForgeAnvilBlockEvents.Hooks.onBrokenAfterFall(instance, level, pos, fallingBlock);
  }
}
