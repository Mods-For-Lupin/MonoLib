package io.github.jason13official.monolib.api.common.command;

import com.mojang.brigadier.Command;
import net.minecraft.commands.CommandSourceStack;

/**
 * Allows an enum to be used as a branching command path. <br /> Adapted from Darkhax-Minecraft's <a href="https://github.com/Darkhax-Minecraft/Bookshelf">Bookshelf</a>
 */
public interface IEnumCommandArg extends Command<CommandSourceStack> {

  String getCommandName();
}
