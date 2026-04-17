package io.github.jason13official.monolib.impl.common.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.state.BlockState;

public class MonoLibFabricAnvilBlockEvents {

  public static final Event<AnvilOnLandEvent> ON_LAND = EventFactory.createArrayBacked(AnvilOnLandEvent.class, events -> (anvilBlock, level, pos, state, replaceableState, fallingBlock) -> {
    for (AnvilOnLandEvent event : events) {
      event.onLand(anvilBlock, level, pos, state, replaceableState, fallingBlock);
    }
  });

  public static final Event<AnvilOnBrokenAfterFallEvent> ON_BROKEN_AFTER_FALL = EventFactory.createArrayBacked(AnvilOnBrokenAfterFallEvent.class, events -> (anvilBlock, level, pos, fallingBlock) -> {
    for (AnvilOnBrokenAfterFallEvent event : events) {
      event.onBrokenAfterFall(anvilBlock, level, pos, fallingBlock);
    }
  });

  @FunctionalInterface
  public interface AnvilOnLandEvent {

    void onLand(AnvilBlock anvilBlock, Level level, BlockPos pos, BlockState state, BlockState replaceableState, FallingBlockEntity fallingBlock);
  }

  @FunctionalInterface
  public interface AnvilOnBrokenAfterFallEvent {

    void onBrokenAfterFall(AnvilBlock anvilBlock, Level level, BlockPos pos, FallingBlockEntity fallingBlock);
  }
}
