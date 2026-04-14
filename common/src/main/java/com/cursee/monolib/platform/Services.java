package com.cursee.monolib.platform;

import com.cursee.monolib.platform.services.IPlatformHelper;
import com.cursee.monolib.platform.services.IRegisterHelper;
import io.github.jason13official.monolib.Constants;
import java.util.ServiceLoader;

@Deprecated
public class Services {

  @Deprecated
  public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);

  @Deprecated
  public static final IRegisterHelper REGISTER = load(IRegisterHelper.class);

  @Deprecated
  public static <T> T load(Class<T> clazz) {

    final T loadedService = ServiceLoader.load(clazz)
        .findFirst()
        .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
    Constants.LOG.debug("Loaded {} for service {}", loadedService, clazz);
    return loadedService;
  }
}