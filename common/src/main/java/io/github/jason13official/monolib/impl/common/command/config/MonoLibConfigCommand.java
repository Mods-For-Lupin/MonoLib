package io.github.jason13official.monolib.impl.common.command.config;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.jason13official.monolib.Constants;
import io.github.jason13official.monolib.api.common.config.ConfigGetterSetter.Commented;
import io.github.jason13official.monolib.impl.common.CommonModConfig;
import io.github.jason13official.monolib.impl.common.config.ModConfigIO;
import java.lang.reflect.Field;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

/// build `monolib config get/set` commands with reflection over
/// [Commented] fields of provided classes
public class MonoLibConfigCommand {

  public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

    if (CommonModConfig.DEBUG.get()) {
      Constants.LOG.info("Registering \"/monolib config\" command...");
    }

    LiteralArgumentBuilder<CommandSourceStack> config = Commands.literal("config");
    LiteralArgumentBuilder<CommandSourceStack> get = Commands.literal("get");
    LiteralArgumentBuilder<CommandSourceStack> set = Commands.literal("set");

    registerFields(get, set, CommonModConfig.class);

    config.then(get);
    config.then(set);

    var monolibComWithConfigSub = Commands.literal(Constants.MOD_ID).requires(source -> source.hasPermission(2)).then(config);

    dispatcher.register(monolibComWithConfigSub);
  }

  private static void registerFields(LiteralArgumentBuilder<CommandSourceStack> get, LiteralArgumentBuilder<CommandSourceStack> set, Class<?> clazz) {

    for (Field field : clazz.getFields()) {

      if (!Commented.class.isAssignableFrom(field.getType())) {
        continue;
      }

      try {

        Commented<?> commented = (Commented<?>) field.get(null);
        registerField(get, set, clazz, commented);

      } catch (IllegalAccessException e) {
        Constants.LOG.info("Failed to read config field {} on {}", field.getName(), clazz.getName());
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static void registerField(LiteralArgumentBuilder<CommandSourceStack> get, LiteralArgumentBuilder<CommandSourceStack> set, Class<?> clazz, Commented<?> commented) {

    Object sample = commented.get();

    if (sample instanceof Boolean) {
      registerBoolean(get, set, clazz, (Commented<Boolean>) commented);
    } else if (sample instanceof Integer) {
      registerInteger(get, set, clazz, (Commented<Integer>) commented);
    } else if (sample instanceof Long) {
      registerLong(get, set, clazz, (Commented<Long>) commented);
    } else if (sample instanceof Float) {
      registerFloat(get, set, clazz, (Commented<Float>) commented);
    } else if (sample instanceof Double) {
      registerDouble(get, set, clazz, (Commented<Double>) commented);
    } else if (sample instanceof Byte) {
      registerByte(get, set, clazz, (Commented<Byte>) commented);
    } else if (sample instanceof String) {
      registerString(get, set, clazz, (Commented<String>) commented);
    } else {
      Constants.LOG.info("Unsupported config value type {} for key {}", sample.getClass().getSimpleName(), commented.key());
    }
  }

  private static void registerGetter(LiteralArgumentBuilder<CommandSourceStack> get, Commented<?> commented) {

    get.then(Commands.literal(commented.key())
        .executes(ctx -> {
          ctx.getSource().sendSuccess(() -> Component.literal("Value " + commented.key() + " is " + commented.get()), false);
          return 1;
        }));
  }

  private static void registerBoolean(LiteralArgumentBuilder<CommandSourceStack> get, LiteralArgumentBuilder<CommandSourceStack> set, Class<?> clazz, Commented<Boolean> commented) {

    registerGetter(get, commented);

    set.then(Commands.literal(commented.key())
        .then(Commands.argument("value", BoolArgumentType.bool())
            .executes(ctx -> {
              boolean value = BoolArgumentType.getBool(ctx, "value");
              commented.set(value);
              ModConfigIO.save(clazz);
              ctx.getSource().sendSuccess(() -> Component.literal("Set " + commented.key() + " to " + value), true);
              return 1;
            })));
  }

  private static void registerInteger(LiteralArgumentBuilder<CommandSourceStack> get, LiteralArgumentBuilder<CommandSourceStack> set, Class<?> clazz, Commented<Integer> commented) {

    registerGetter(get, commented);

    set.then(Commands.literal(commented.key())
        .then(Commands.argument("value", IntegerArgumentType.integer())
            .executes(ctx -> {
              int value = IntegerArgumentType.getInteger(ctx, "value");
              commented.set(value);
              ModConfigIO.save(clazz);
              ctx.getSource().sendSuccess(() -> Component.literal("Set " + commented.key() + " to " + value), true);
              return 1;
            })));
  }

  private static void registerLong(LiteralArgumentBuilder<CommandSourceStack> get, LiteralArgumentBuilder<CommandSourceStack> set, Class<?> clazz, Commented<Long> commented) {

    registerGetter(get, commented);

    set.then(Commands.literal(commented.key())
        .then(Commands.argument("value", LongArgumentType.longArg())
            .executes(ctx -> {
              long value = LongArgumentType.getLong(ctx, "value");
              commented.set(value);
              ModConfigIO.save(clazz);
              ctx.getSource().sendSuccess(() -> Component.literal("Set " + commented.key() + " to " + value), true);
              return 1;
            })));
  }

  private static void registerFloat(LiteralArgumentBuilder<CommandSourceStack> get, LiteralArgumentBuilder<CommandSourceStack> set, Class<?> clazz, Commented<Float> commented) {

    registerGetter(get, commented);

    set.then(Commands.literal(commented.key())
        .then(Commands.argument("value", FloatArgumentType.floatArg())
            .executes(ctx -> {
              float value = FloatArgumentType.getFloat(ctx, "value");
              commented.set(value);
              ModConfigIO.save(clazz);
              ctx.getSource().sendSuccess(() -> Component.literal("Set " + commented.key() + " to " + value), true);
              return 1;
            })));
  }

  private static void registerDouble(LiteralArgumentBuilder<CommandSourceStack> get, LiteralArgumentBuilder<CommandSourceStack> set, Class<?> clazz, Commented<Double> commented) {

    registerGetter(get, commented);

    set.then(Commands.literal(commented.key())
        .then(Commands.argument("value", DoubleArgumentType.doubleArg())
            .executes(ctx -> {
              double value = DoubleArgumentType.getDouble(ctx, "value");
              commented.set(value);
              ModConfigIO.save(clazz);
              ctx.getSource().sendSuccess(() -> Component.literal("Set " + commented.key() + " to " + value), true);
              return 1;
            })));
  }

  private static void registerByte(LiteralArgumentBuilder<CommandSourceStack> get, LiteralArgumentBuilder<CommandSourceStack> set, Class<?> clazz, Commented<Byte> commented) {

    registerGetter(get, commented);

    set.then(Commands.literal(commented.key())
        .then(Commands.argument("value", IntegerArgumentType.integer(Byte.MIN_VALUE, Byte.MAX_VALUE))
            .executes(ctx -> {
              byte value = (byte) IntegerArgumentType.getInteger(ctx, "value");
              commented.set(value);
              ModConfigIO.save(clazz);
              ctx.getSource().sendSuccess(() -> Component.literal("Set " + commented.key() + " to " + value), true);
              return 1;
            })));
  }

  private static void registerString(LiteralArgumentBuilder<CommandSourceStack> get, LiteralArgumentBuilder<CommandSourceStack> set, Class<?> clazz, Commented<String> commented) {

    registerGetter(get, commented);

    set.then(Commands.literal(commented.key())
        .then(Commands.argument("value", StringArgumentType.greedyString())
            .executes(ctx -> {
              String value = StringArgumentType.getString(ctx, "value");
              commented.set(value);
              ModConfigIO.save(clazz);
              ctx.getSource().sendSuccess(() -> Component.literal("Set " + commented.key() + " to " + value), true);
              return 1;
            })));
  }
}
