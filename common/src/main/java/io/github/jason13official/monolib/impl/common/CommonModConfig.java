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

  private static boolean verifyJars = false;
  public static Commented<Boolean> VERIFY_JARS = new Commented<>(
      "verify_jars",
      () -> verifyJars,
      value -> verifyJars = value,
      "Whether to scan for JAR filenames registered to MonoLib's \"Sailing\" anti-piracy subsystem. \n Check out stopmodreposts.org for information on why this feature exists"
  );
}
