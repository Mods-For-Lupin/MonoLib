package io.github.jason13official.monolib.impl.common.command.data.arg;

import io.github.jason13official.monolib.api.common.command.IEnumCommandArg;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public enum SlotArgument implements IEnumCommandArg {

  HEAD(EquipmentSlot.HEAD), CHEST(EquipmentSlot.CHEST), LEGS(EquipmentSlot.LEGS), FEET(EquipmentSlot.FEET), MAIN_HAND(EquipmentSlot.MAINHAND), OFFHAND(EquipmentSlot.OFFHAND);

  private final EquipmentSlot slot;

  SlotArgument(EquipmentSlot slot) {
    this.slot = slot;
  }

  public static CompletableFuture<Suggestions> suggestion(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
    Arrays.stream(SlotArgument.values()).forEach(slot -> builder.suggest(slot.getCommandName()));
    return builder.buildFuture();
  }

  @Override
  public String getCommandName() {

    return this.name().toLowerCase(Locale.ROOT);
  }

  @Override
  public int run(CommandContext<CommandSourceStack> commandContext) {

    // NO-OP, just return successful execution

    return Command.SINGLE_SUCCESS;
  }

  /// Uses `this.slot`, an instance of EquipmentSlot, to get the appropriate ItemStack instance from the given entity
  public ItemStack getItemFromEntity(LivingEntity entity) {

    return entity.getItemBySlot(this.slot);
  }
}
