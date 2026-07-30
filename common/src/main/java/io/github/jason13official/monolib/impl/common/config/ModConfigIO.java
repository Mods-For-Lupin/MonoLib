package io.github.jason13official.monolib.impl.common.config;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import io.github.jason13official.monolib.Constants;
import io.github.jason13official.monolib.api.common.config.ConfigGetterSetter.Commented;
import io.github.jason13official.monolib.impl.client.ClientModConfig;
import io.github.jason13official.monolib.impl.common.CommonModConfig;
import io.github.jason13official.monolib.impl.common.ServerModConfig;
import io.github.jason13official.monolib.platform.Services;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class ModConfigIO {

  public static final String COMMON_FILENAME = Constants.MOD_ID + "-common.toml";
  public static final String CLIENT_FILENAME = Constants.MOD_ID + "-client.toml";
  public static final String SERVER_FILENAME = Constants.MOD_ID + "-server.toml";

  public static void load(Path configDir) {

    File configDirectory = new File(configDir.toUri());
    if (!configDirectory.isDirectory() && !configDirectory.mkdirs()) {
      Constants.LOG.info("Failed to get or create config directory {}", configDirectory.getAbsolutePath());
      return;
    }

    Config.setInsertionOrderPreserved(true);

    // only load config from classes that have public fields
    if (CommonModConfig.class.getFields().length > 0) {
      Constants.dev("Common config had fields for reference.");
      ModConfigIO.loadConfig(configDir, COMMON_FILENAME, CommonModConfig.class);
    }
    if (ClientModConfig.class.getFields().length > 0 && Services.PLATFORM.isClientSide()) {
      Constants.dev("Client config had fields for reference.");
      ModConfigIO.loadConfig(configDir, CLIENT_FILENAME, ClientModConfig.class);
    }
    if (ServerModConfig.class.getFields().length > 0) {
      Constants.dev("Server config had fields for reference.");
      ModConfigIO.loadConfig(configDir, SERVER_FILENAME, ServerModConfig.class);
    }
  }

  private static void loadConfig(Path configDir, String filename, Class<?> clazz) {

    if (!hasConfigFields(clazz)) {
      Constants.dev("Skipping " + filename + " as " + clazz.getName() + " has no public static fields of io.github.jason13official.monolib.api.common.config.Commented");
      return;
    }

    Path configFilepath = configDir.resolve(filename);
    File configFile = new File(configFilepath.toUri());

    try (CommentedFileConfig config = CommentedFileConfig.builder(configFile).build()) {

      if (Files.exists(configFilepath)) {
        config.load();
      }

      for (Field field : clazz.getFields()) {

        Commented<?> commented = (Commented<?>) field.get(null);

        loadEntry(config, commented);
        saveEntry(config, commented);
      }

      config.save();
    } catch (Exception e) {

      Constants.LOG.info("Failed to get or create config file {}", configFile.getAbsolutePath());
      e.printStackTrace();
    }
  }

  /// write current in-memory values of [Commented] fields of given classes to their
  /// respective files (for use after a config command)
  public static void save(Class<?> clazz) {

    String filename = filenameFor(clazz);
    if (filename == null) {
      Constants.LOG.info("Cannot save config for {} as it is not a registered config class", clazz.getName());
      return;
    }

    Path configDir = Services.PLATFORM.getConfigDirectory();
    File configDirectory = new File(configDir.toUri());
    if (!configDirectory.isDirectory() && !configDirectory.mkdirs()) {
      Constants.LOG.info("Failed to get or create config directory {}", configDirectory.getAbsolutePath());
      return;
    }

    Path configFilepath = configDir.resolve(filename);
    File configFile = new File(configFilepath.toUri());

    try (CommentedFileConfig config = CommentedFileConfig.builder(configFile).build()) {

      if (Files.exists(configFilepath)) {
        config.load();
      }

      for (Field field : clazz.getFields()) {

        Commented<?> commented = (Commented<?>) field.get(null);
        saveEntry(config, commented);
      }

      config.save();
    } catch (Exception e) {

      Constants.LOG.info("Failed to save config file {}", configFile.getAbsolutePath());
      e.printStackTrace();
    }
  }

  private static String filenameFor(Class<?> clazz) {

    if (clazz == CommonModConfig.class) {
      return COMMON_FILENAME;
    }
    if (clazz == ClientModConfig.class) {
      return CLIENT_FILENAME;
    }
    if (clazz == ServerModConfig.class) {
      return SERVER_FILENAME;
    }
    return null;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  private static void loadEntry(CommentedFileConfig config, Commented commented) {
    Object value = config.getOrElse(commented.key(), commented.get());
    commented.set(value);
  }

  private static void saveEntry(CommentedFileConfig config, Commented<?> commented) {
    config.setComment(commented.key(), " " + commented.comment());
    config.set(commented.key(), commented.get());
  }

  /// check that the class contains public static [Commented] fields
  private static boolean hasConfigFields(Class<?> clazz) {
    return Arrays.stream(clazz.getFields())
        .filter(f -> Modifier.isStatic(f.getModifiers()))
        .anyMatch(f -> Commented.class.isAssignableFrom(f.getType()));
  }
}
