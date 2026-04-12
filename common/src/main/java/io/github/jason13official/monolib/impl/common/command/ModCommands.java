package io.github.jason13official.monolib.impl.common.command;

import com.mojang.brigadier.CommandDispatcher;
import io.github.jason13official.monolib.Constants;
import io.github.jason13official.monolib.MonoLib;
import io.github.jason13official.monolib.impl.common.ModConfig;
import io.github.jason13official.monolib.impl.common.command.data.MonoLibDataCommand;
import io.github.jason13official.monolib.impl.common.util.GsonConfigMapper;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands.CommandSelection;

public class ModCommands {

  public static final int FAILURE = 0;

  public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher, CommandBuildContext commandBuildContext, CommandSelection commandSelection) {

    final ModConfig config = GsonConfigMapper.get(MonoLib.identifier("server"));
    if (config.additionalDebugLogs) {
      Constants.LOG.info("Registering MonoLib commands...");
    }

    MonoLibDataCommand.register(commandDispatcher);

    if (config.additionalDebugLogs) {
      Constants.LOG.info("Registered all MonoLib commands.");
    }
  }
}
