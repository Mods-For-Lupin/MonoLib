package io.github.jason13official.monolib.impl.common.registry;

import com.mojang.brigadier.CommandDispatcher;
import io.github.jason13official.monolib.Constants;
import io.github.jason13official.monolib.MonoLib;
import io.github.jason13official.monolib.impl.common.CommonModConfig;
import io.github.jason13official.monolib.impl.common.command.config.MonoLibConfigCommand;
import io.github.jason13official.monolib.impl.common.command.data.MonoLibDataCommand;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands.CommandSelection;

public class ModCommands {

  public static final int FAILURE = 0;

  public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher, CommandBuildContext commandBuildContext, CommandSelection commandSelection) {

    if (CommonModConfig.DEBUG.get()) {
      Constants.LOG.info("Registering MonoLib commands...");
    }

    MonoLibDataCommand.register(commandDispatcher);
    MonoLibConfigCommand.register(commandDispatcher);

    if (CommonModConfig.DEBUG.get()) {
      Constants.LOG.info("Registered all MonoLib commands.");
    }
  }
}
