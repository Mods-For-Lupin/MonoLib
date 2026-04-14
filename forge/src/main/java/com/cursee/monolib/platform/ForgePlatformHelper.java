package com.cursee.monolib.platform;

import com.cursee.monolib.platform.services.IPlatformHelper;
import io.github.jason13official.monolib.platform.Services;
import java.nio.file.Path;
import java.util.List;
import net.minecraft.world.item.CreativeModeTab.Builder;

public class ForgePlatformHelper implements IPlatformHelper {

  @Override
  public String getPlatformName() {
    return Services.PLATFORM.getPlatformName();
  }

  @Override
  public boolean isModLoaded(String modId) {
    return Services.PLATFORM.isModLoaded(modId);
  }

  @Override
  public boolean isDevelopmentEnvironment() {
    return Services.PLATFORM.isDevelopmentEnvironment();
  }

  @Override
  public String getGameDirectory() {
    return Services.PLATFORM.getGameDirectory().toString();
  }

  @Override
  public List<Path> getInstalledModPaths() {
    return Services.PLATFORM.getInstalledModPaths();
  }

  @Override
  public Builder tabBuilder() {
    return Services.PLATFORM.tabBuilder();
  }

}
