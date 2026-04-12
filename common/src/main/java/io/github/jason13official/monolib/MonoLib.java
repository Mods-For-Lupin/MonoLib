package io.github.jason13official.monolib;

import io.github.jason13official.monolib.impl.common.ModConfig;
import io.github.jason13official.monolib.impl.common.util.GsonConfigMapper;
import io.github.jason13official.monolib.platform.Services;
import net.minecraft.resources.ResourceLocation;


public class MonoLib {

  public static void init() {
    GsonConfigMapper.register(identifier("server").toString(), ModConfig.class, "monolib-server.json");

    // load config on mod loaded / startup
    GsonConfigMapper.loadAll(Services.PLATFORM.getConfigDirectory());
  }

  public static ResourceLocation identifier(final String path) {
    return new ResourceLocation(Constants.MOD_ID, path);
  }
}