package io.github.jason13official.monolib.impl.common.command.data.arg;

import io.github.jason13official.monolib.api.common.command.IEnumCommandArg;
import io.github.jason13official.monolib.impl.common.command.data.MonoLibDataCommand.ItemFormat;
import io.github.jason13official.monolib.impl.common.serialization.codecs.map.MapCodecs;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import java.util.Arrays;
import java.util.Locale;
import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * Adapted from Darkhax's <a href="https://github.com/Darkhax-Minecraft/Bookshelf">Bookshelf</a>
 */
public enum FormatArgument implements IEnumCommandArg {

  ITEM_ID((stack, level) -> {
    final String s = BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();
    return Component.literal(s).withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, s)));
  }),

  AS_STRING((stack, level) -> {
    final String s = stack.toString();
    return Component.literal(s).withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, s)));
  }),

  AS_INGREDIENT(fromCodec(MapCodecs.INGREDIENT.get(), (stack, level) -> Ingredient.of(stack.getItem()))),

  FULL_JSON(fromCodec(MapCodecs.ITEM_STACK.get(), (stack, level) -> stack)),

  TAGS(((stack, level) -> {
    final StringJoiner joiner = new StringJoiner("\n");
    stack.getTags().forEach(itemTagKey -> joiner.add(itemTagKey.location().toString()));
    return Component.literal(joiner.toString()).withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, joiner.toString())));
  })),

  NBT_TAG((stack, level) -> {
    final CompoundTag tag = stack.getTag();
    final String s = tag != null ? tag.toString() : "{}";
    return Component.literal(s).withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, s)));
  });

  private final ItemFormat format;

  FormatArgument(ItemFormat format) {

    this.format = format;
  }

  private static <T> ItemFormat fromCodec(Codec<T> codec, BiFunction<ItemStack, ServerLevel, T> mapper) {

    return (stack, level) -> {

      if (stack.isEmpty()) {
        return Component.literal("Item must not be empty or air!").withStyle(ChatFormatting.RED);
      }

      final T value = mapper.apply(stack, level);
      final JsonElement json = codec.encodeStart(RegistryOps.create(JsonOps.INSTANCE, level.registryAccess()), value).getOrThrow(false, err -> {});
      final Gson gson = new GsonBuilder().setPrettyPrinting().create();

      return Component.literal(gson.toJson(json)).withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, gson.toJson(json))));
    };
  }

  public static CompletableFuture<Suggestions> suggestion(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
    Arrays.stream(FormatArgument.values()).forEach(format -> builder.suggest(format.getCommandName()));
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

  public ItemFormat getFormat() {

    return this.format;
  }
}
