package io.github.jason13official.monolib;

import io.github.jason13official.monolib.platform.Services;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {

  public static final String MOD_ID = "monolib";
  public static final String MOD_NAME = "MonoLib";
  public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

  public static void dev(String s) {

    if (Services.PLATFORM.isDevelopmentEnvironment()) {
      LOG.info(s);
    }
  }
}