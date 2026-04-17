package io.github.jason13official.monolib.impl.common.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;

public class MonoLibFabricAnvilMenuEvents {

  public static final Event<AnvilCreateResultEvent> CREATE_RESULT = EventFactory.createArrayBacked(AnvilCreateResultEvent.class, events -> (anvilMenu, leftStack, rightStack, output, itemName, baseCost, player) -> {

    for (AnvilCreateResultEvent event : events) {
      var result = event.createResult(anvilMenu, leftStack, rightStack, output, itemName, baseCost, player);
      if (result != null) return result;
    }

    return null;
  });

  public static final Event<AnvilOnTakeEvent> ON_TAKE = EventFactory.createArrayBacked(AnvilOnTakeEvent.class, events -> (anvilMenu, player, output, left, right) -> {
    for (AnvilOnTakeEvent event : events) {
      event.onTake(anvilMenu, player, output, left, right);
    }
  });

  @FunctionalInterface
  public interface AnvilCreateResultEvent {
    AnvilMenuResult createResult(AnvilMenu anvilMenu, ItemStack leftStack, ItemStack rightStack, ItemStack output, String itemName, int baseCost, Player player);
  }

  @FunctionalInterface
  public interface AnvilOnTakeEvent {
    void onTake(AnvilMenu anvilMenu, Player player, ItemStack output, ItemStack left, ItemStack right);
  }

  public record AnvilMenuResult(int cost, int repairItemCountCost, ItemStack output) {}
}
