package io.github.jason13official.monolib.impl.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.jason13official.monolib.Constants;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.resources.Identifier;

@Deprecated(since = "4.1.0", forRemoval = true)
public class GsonConfigMapper {

  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
  private static final Map<String, ConfigEntry<?>> REGISTRY = new LinkedHashMap<>();

  public static <T> void register(String id, Class<T> clazz, String filename) {
    if (REGISTRY.containsKey(id)) {
      Constants.LOG.info("Config already registered for id: {}", id);
      return;
    }
    REGISTRY.put(id, new ConfigEntry<>(clazz, filename));
  }

  public static void loadAll(Path configDir) {
    for (ConfigEntry<?> entry : REGISTRY.values()) {
      entry.load(configDir);
    }
  }

  public static <T> T get(Identifier rl) {
    return get(rl.toString());
  }

  @SuppressWarnings("unchecked")
  public static <T> T get(String id) {
    ConfigEntry<?> entry = REGISTRY.get(id);
    if (entry == null) {
      throw new IllegalArgumentException("No config registered for id: " + id);
    }
    return (T) entry.getInstance();
  }

  public static Gson getGson() {
    return GSON;
  }

  private static final class ConfigEntry<T> {

    private final Class<T> clazz;
    private final String filename;
    private T instance;
    private boolean loaded = false;

    private ConfigEntry(Class<T> clazz, String filename) {
      this.clazz = clazz;
      this.filename = filename;
    }

    T getInstance() {
      if (!loaded) {
        throw new IllegalStateException("Config '" + filename + "' has not been loaded yet. " + "Ensure GsonConfigMapper.loadAll() is called before accessing configs.");
      }
      return instance;
    }

    void load(Path configDir) {
      Path file = configDir.resolve(filename);

      try {
        instance = clazz.getDeclaredConstructor().newInstance();
      } catch (ReflectiveOperationException e) {
        throw new RuntimeException("Config class must have a no-arg constructor: " + clazz.getName(), e);
      }

      if (Files.exists(file)) {
        try (Reader reader = Files.newBufferedReader(file)) {
          T fromDisk = GSON.fromJson(reader, clazz);
          if (fromDisk != null) {
            instance = fromDisk;
          }
        } catch (IOException e) {
          Constants.LOG.error("Failed to load config '{}', using defaults", filename, e);
        }
      }

      loaded = true;
      save(configDir);
    }

    private void save(Path configDir) {

      Constants.dev("Attempted to save " + this.filename + " using the deprecated config system, ignoring.");

//      Path file = configDir.resolve(filename);
//      try {
//        Files.createDirectories(configDir);
//        try (Writer writer = Files.newBufferedWriter(file)) {
//          GSON.toJson(instance, writer);
//        }
//      } catch (IOException e) {
//        Constants.LOG.error("Failed to save config '{}'", filename, e);
//      }
    }
  }
}
