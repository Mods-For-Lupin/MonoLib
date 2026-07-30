package io.github.jason13official.monolib.impl.common;

import io.github.jason13official.monolib.api.common.config.ConfigGetterSetter.Commented;

public class CommonModConfig {

  private static boolean debug = false;
  public static Commented<Boolean> DEBUG = new Commented<>(
      "debug",
      () -> debug,
      value -> debug = value,
      "Whether to log additional information to the console."
  );
}
