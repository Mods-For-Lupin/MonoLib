package io.github.jason13official.monolib;

import io.github.jason13official.monolib.impl.common.CommonModConfig;
import io.github.jason13official.monolib.impl.common.ModConfig;
import io.github.jason13official.monolib.impl.common.config.DeveloperConfigTest;
import io.github.jason13official.monolib.impl.common.config.ModConfigIO;
import io.github.jason13official.monolib.impl.common.sailing.Sailing;
import io.github.jason13official.monolib.impl.common.util.GsonConfigMapper;
import io.github.jason13official.monolib.platform.Services;
import net.minecraft.resources.ResourceLocation;

public class MonoLib {

  @SuppressWarnings("removal") // GsonConfigMapper marked for removal
  public static void initConfig() {

    // backwards compatibility to eventually remove
    GsonConfigMapper.register(identifier("server").toString(), ModConfig.class, "monolib-server.json");
    GsonConfigMapper.loadAll(Services.PLATFORM.getConfigDirectory());
    final ModConfig config = GsonConfigMapper.get(MonoLib.identifier("server").toString());
    CommonModConfig.DEBUG.set(config.additionalDebugLogs);
    CommonModConfig.VERIFY_JARS.set(config.verifyModFilenames);

    // development environment only test, "monolib_test-common.toml"
    DeveloperConfigTest.performGetterSetterTest();

    // new mod config loading as of 4.1.0 (common on both, client on client, server on both)
    // TODO impl synchronization of server config to clients (packets on world join/config reload)
    ModConfigIO.load(Services.PLATFORM.getConfigDirectory());
  }

  public static void init() {
    Sailing.register(Constants.MOD_ID, createFilename(Constants.MOD_ID, "1.20.1", "4.0.2"));
  }

  public static ResourceLocation identifier(final String path) {
    return new ResourceLocation(Constants.MOD_ID, path);
  }

  public static String createFilename(String modId, String mcVersion, String modVersion) {
    return modId + "-merged-" + mcVersion + "-" + modVersion + ".jar";
  }
}