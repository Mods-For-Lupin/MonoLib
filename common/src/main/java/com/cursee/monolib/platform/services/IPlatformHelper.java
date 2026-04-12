package com.cursee.monolib.platform.services;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.SpawnEggItem;

@Deprecated
public interface IPlatformHelper {

  @Deprecated
  String getPlatformName();

  @Deprecated
  boolean isModLoaded(String modId);

  @Deprecated
  boolean isDevelopmentEnvironment();

  @Deprecated
  default String getEnvironmentName() {

    return isDevelopmentEnvironment() ? "development" : "production";
  }

  @Deprecated
  String getGameDirectory();

  @Deprecated
  default Path getConfigDirectory() {

    return Path.of(getGameDirectory()).resolve("config");
  }

  @Deprecated
  List<Path> getInstalledModPaths();

  @Deprecated
  CreativeModeTab.Builder tabBuilder();

  @Deprecated
  SpawnEggItem createSpawnEggItem(Supplier<EntityType<? extends Mob>> typeSupplier, int background, int highlight, Properties properties);
}