package com.cursee.monolib.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import oshi.util.tuples.Triplet;

@Deprecated(since = "2.0.0", forRemoval = true)
public class AnvilEventsFabric {

    @Deprecated(since = "2.0.0", forRemoval = true)
    public static final Event<Update> UPDATE = EventFactory.createArrayBacked(Update.class, callbacks -> (anvilmenu, left, right, output, itemName, baseCost, player) -> {

        for (Update callback : callbacks) {

            Triplet<Integer, Integer, ItemStack> triple = callback.onUpdate(anvilmenu, left, right, output, itemName, baseCost, player);

            if (triple != null) return triple;
        }

        return null;
    });

    @Deprecated(since = "2.0.0", forRemoval = true)
    @FunctionalInterface
    public interface Update {
        @Deprecated(since = "2.0.0", forRemoval = true)
        Triplet<Integer, Integer, ItemStack> onUpdate(AnvilMenu anvilmenu, ItemStack slotLeft, ItemStack slotRight, ItemStack slotOutput, String itemName, int baseCost, Player player);
    }
}
