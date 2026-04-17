package com.cursee.monolib.platform.services;

import java.nio.file.Path;
import java.util.List;
import net.minecraft.world.item.CreativeModeTab;

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
}