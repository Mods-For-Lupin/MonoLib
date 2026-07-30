package io.github.jason13official.monolib.platform.services;

import java.nio.file.Path;
import java.util.List;
import net.minecraft.world.item.CreativeModeTab;

public interface IPlatformHelper {

  String getPlatformName();

  boolean isModLoaded(String modId);

  boolean isDevelopmentEnvironment();

  default String getEnvironmentName() {
    return isDevelopmentEnvironment() ? "development" : "production";
  }

  Path getGameDirectory();

  default Path getConfigDirectory() {
    return getGameDirectory().resolve("config");
  }

  List<Path> getInstalledModPaths();

  boolean isClientSide();

  @Deprecated
  CreativeModeTab.Builder tabBuilder();
}
