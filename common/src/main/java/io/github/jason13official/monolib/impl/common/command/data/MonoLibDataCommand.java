package io.github.jason13official.monolib.impl.common.command.data;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.github.jason13official.monolib.Constants;
import io.github.jason13official.monolib.MonoLib;
import io.github.jason13official.monolib.impl.common.ModConfig;
import io.github.jason13official.monolib.impl.common.registry.ModCommands;
import io.github.jason13official.monolib.impl.common.command.data.arg.FormatArgument;
import io.github.jason13official.monolib.impl.common.command.data.arg.SlotArgument;
import io.github.jason13official.monolib.impl.common.util.GsonConfigMapper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Adapted from Darkhax-Minecraft's <a href="https://github.com/Darkhax-Minecraft/Bookshelf">Bookshelf</a>
 */
public class MonoLibDataCommand {

  private static final String DATA = "data";
  private static final String SLOT = "slot";
  private static final String FORMAT = "format";

  public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

    final ModConfig config = GsonConfigMapper.get(MonoLib.identifier("server").toString());
    if (config.additionalDebugLogs) {
      Constants.LOG.info("Registering \"/monolib data <slot> <format>\" command...");
    }

    var formatExecution = Commands.argument(FORMAT, StringArgumentType.word()).suggests(FormatArgument::suggestion).executes(MonoLibDataCommand::execute);
    var slotArgWithFormat = Commands.argument(SLOT, StringArgumentType.word()).suggests(SlotArgument::suggestion).then(formatExecution);
    var dataSubWithArgs = Commands.literal(DATA).then(slotArgWithFormat);
    var monolibComWithDataSub = Commands.literal(Constants.MOD_ID).then(dataSubWithArgs);

    dispatcher.register(monolibComWithDataSub);
  }

  public static int execute(CommandContext<CommandSourceStack> context) {

    final CommandSourceStack source = context.getSource();

    if (!(source.getEntity() instanceof Player player)) {
      return ModCommands.FAILURE;
    }

    try {

      String slotName = StringArgumentType.getString(context, SLOT);
      String formatName = StringArgumentType.getString(context, FORMAT);

      SlotArgument slotArgument = SlotArgument.valueOf(slotName.toUpperCase());
      FormatArgument formatArgument = FormatArgument.valueOf(formatName.toUpperCase());

      ItemStack itemStack = slotArgument.getItemFromEntity(player);

      source.sendSuccess(() -> formatArgument.getFormat().formatItem(itemStack, source.getLevel()), false);

    } catch (IllegalArgumentException e) {

      if (source.getEntity() instanceof Player erroredPlayer) {
        erroredPlayer.sendSystemMessage(Component.literal("Illegal arguments for 'monolib data' command: " + context.getInput()));
      }

      return ModCommands.FAILURE;
    }

    return Command.SINGLE_SUCCESS;
  }

  public interface ItemFormat {

    Component formatItem(ItemStack stack, ServerLevel level);
  }
}
