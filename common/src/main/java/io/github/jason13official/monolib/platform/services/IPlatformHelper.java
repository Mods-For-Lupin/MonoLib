package io.github.jason13official.monolib.platform.services;

import java.nio.file.Path;
import java.util.List;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;

public interface IPlatformHelper {

  /**
   * Gets the name of the current platform
   *
   * @return The name of the current platform.
   */
  String getPlatformName();

  /**
   * Checks if a mod with the given id is loaded.
   *
   * @param modId The mod to check if it is loaded.
   * @return True if the mod is loaded, false otherwise.
   */
  boolean isModLoaded(String modId);

  /**
   * Check if the game is currently in a development environment.
   *
   * @return True if in a development environment, false otherwise.
   */
  boolean isDevelopmentEnvironment();

  /**
   * Gets the name of the environment type as a string.
   *
   * @return The name of the environment type.
   */
  default String getEnvironmentName() {

    return isDevelopmentEnvironment() ? "development" : "production";
  }

  Path getGameDirectory();

  default Path getConfigDirectory() {

    return getGameDirectory().resolve("config");
  }

  List<Path> getInstalledModPaths();

  @Deprecated
  CreativeModeTab.Builder tabBuilder();

  // Item createSpawnEgg(EntityType<? extends Mob> type, int background, int highlight, Item.Properties properties);
}