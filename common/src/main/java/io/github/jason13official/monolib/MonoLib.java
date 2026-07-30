package io.github.jason13official.monolib;

import io.github.jason13official.monolib.impl.common.ModConfig;
import io.github.jason13official.monolib.impl.common.config.DeveloperConfigTest;
import io.github.jason13official.monolib.impl.common.config.ModConfigIO;
import io.github.jason13official.monolib.impl.common.sailing.Sailing;
import io.github.jason13official.monolib.impl.common.util.GsonConfigMapper;
import io.github.jason13official.monolib.platform.Services;
import net.minecraft.resources.Identifier;

public class MonoLib {

  public static void initConfig() {
    GsonConfigMapper.register(identifier("server").toString(), ModConfig.class, "monolib-server.json");
    GsonConfigMapper.loadAll(Services.PLATFORM.getConfigDirectory());

    // development environment only test, "monolib_test-common.toml"
    DeveloperConfigTest.performGetterSetterTest();

    // new mod config loading as of 4.1.0 (common on both, client on client, server on both)
    // TODO impl synchronization of server config to clients (packets on world join/config reload)
    ModConfigIO.load(Services.PLATFORM.getConfigDirectory());
  }

  public static void init() {
    Sailing.register(Constants.MOD_ID, createFilename(Constants.MOD_ID, "26.1.2", "4.0.2"));
  }

  public static Identifier identifier(final String path) {
    return Identifier.fromNamespaceAndPath(Constants.MOD_ID, path);
  }

  public static String createFilename(String modId, String mcVersion, String modVersion) {
    return modId + "-merged-" + mcVersion + "-" + modVersion + ".jar";
  }
}
