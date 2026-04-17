package com.cursee.monolib.platform;

import com.cursee.monolib.platform.services.IPlatformHelper;
import com.cursee.monolib.platform.services.IRegisterHelper;
import io.github.jason13official.monolib.Constants;
import java.util.ServiceLoader;

@Deprecated
public class Services {

  @Deprecated
  public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);

  /// compat, was previously similar to platform helper above but since
  /// moving to lazy init it's uglyyyyy
  public static IRegisterHelper REGISTER = load(IRegisterHelper.class);

  /// Only use this after mixins are applied; service loading is eager
  /// and may load classes sooner than anticipated.
  @Deprecated
  public static IRegisterHelper register() {

    if (REGISTER == null) {
      REGISTER = load(IRegisterHelper.class);
    }

    return REGISTER;
  }

  @Deprecated
  public static <T> T load(Class<T> clazz) {

    final T loadedService = ServiceLoader.load(clazz)
        .findFirst()
        .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
    Constants.LOG.debug("Loaded {} for service {}", loadedService, clazz);
    return loadedService;
  }
}