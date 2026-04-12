package io.github.jason13official.monolib;

import net.minecraft.resources.ResourceLocation;


public class MonoLib {

  public static void init() {
  }

  public static ResourceLocation identifier(final String path) {
    return new ResourceLocation(Constants.MOD_ID, path);
  }
}