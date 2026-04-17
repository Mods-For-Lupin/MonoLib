package io.github.jason13official.monolib.impl.common.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import oshi.util.tuples.Triplet;

public class MonoLibFabricAnvilMenuEvents {

  public static final Event<AnvilCreateResultEvent> CREATE_RESULT = EventFactory.createArrayBacked(AnvilCreateResultEvent.class, events -> (anvilMenu, leftStack, rightStack, output, itemName, baseCost, player) -> {

    for (AnvilCreateResultEvent event : events) {
      var result = event.createResult(anvilMenu, leftStack, rightStack, output, itemName, baseCost, player);
      if (result != null) return result;
    }

    return null;
  });

  /** Similar to Forge's AnvilRepairEvent, but does not modify the break chance applied to the anvil due to restrictions around injecting into/redirecting calls/modifying constants into synthetic static methods created via lambda expressions. */
  public static final Event<AnvilOnTakeEvent> ON_TAKE = EventFactory.createArrayBacked(AnvilOnTakeEvent.class, events -> (anvilMenu, player, output, left, right) -> {
    for (AnvilOnTakeEvent event : events) {
      event.onTake(anvilMenu, player, output, left, right);
    }
  });

  @FunctionalInterface
  public interface AnvilCreateResultEvent {
    Triplet<Integer, Integer, ItemStack> createResult(AnvilMenu anvilMenu, ItemStack leftStack, ItemStack rightStack, ItemStack output, String itemName, int baseCost, Player player);
  }

  @FunctionalInterface
  public interface AnvilOnTakeEvent {
    void onTake(AnvilMenu anvilMenu, Player player, ItemStack output, ItemStack left, ItemStack right);
  }
}
