package io.github.jason13official.monolib;

import net.minecraft.resources.Identifier;

public class MonoLib {

  public static void init() {
  }

  public static Identifier identifier(final String path) {
    return Identifier.fromNamespaceAndPath(Constants.MOD_ID, path);
  }
}