package io.github.jason13official.monolib.impl.common.event;

import io.github.jason13official.monolib.MonoLibNeoForge;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

public class MonoLibNeoForgeAnvilBlockEvents {

  public static class Hooks {

    public static void onLand(AnvilBlock anvilBlock, Level level, BlockPos pos, BlockState blockState, BlockState replaceable, FallingBlockEntity fallingBlock) {
      MonoLibNeoForge.EVENT_BUS.post(new AnvilOnLandEvent(anvilBlock, level, pos, blockState, replaceable, fallingBlock));
    }

    public static void onBrokenAfterFall(AnvilBlock anvilBlock, Level level, BlockPos pos, FallingBlockEntity fallingBlock) {
      MonoLibNeoForge.EVENT_BUS.post(new AnvilOnBrokenAfterFallEvent(anvilBlock, level, pos, level.getBlockState(pos), fallingBlock));
    }
  }

  public static class AnvilOnLandEvent extends BlockEvent implements IModBusEvent {

    private final AnvilBlock anvilBlock;
    private final BlockState replaceable;
    private final FallingBlockEntity fallingBlock;

    public AnvilOnLandEvent(AnvilBlock anvilBlock, LevelAccessor level, BlockPos pos, BlockState state, BlockState replaceable, FallingBlockEntity fallingBlock) {
      super(level, pos, state);
      this.anvilBlock = anvilBlock;
      this.replaceable = replaceable;
      this.fallingBlock = fallingBlock;
    }

    public AnvilBlock getAnvilBlock() {
      return anvilBlock;
    }

    public BlockState getReplaceable() {
      return replaceable;
    }

    public FallingBlockEntity getFallingBlock() {
      return fallingBlock;
    }
  }

  public static class AnvilOnBrokenAfterFallEvent extends BlockEvent implements IModBusEvent {

    private final AnvilBlock anvilBlock;
    private final FallingBlockEntity fallingBlock;

    public AnvilOnBrokenAfterFallEvent(AnvilBlock anvilBlock, LevelAccessor level, BlockPos pos, BlockState state, FallingBlockEntity fallingBlock) {
      super(level, pos, state);
      this.anvilBlock = anvilBlock;
      this.fallingBlock = fallingBlock;
    }

    public AnvilBlock getAnvilBlock() {
      return anvilBlock;
    }

    public FallingBlockEntity getFallingBlock() {
      return fallingBlock;
    }
  }
}
