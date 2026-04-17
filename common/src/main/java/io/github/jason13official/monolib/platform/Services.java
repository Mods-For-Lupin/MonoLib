package io.github.jason13official.monolib.platform;

import io.github.jason13official.monolib.Constants;
import io.github.jason13official.monolib.platform.services.IPlatformHelper;
import io.github.jason13official.monolib.platform.services.IRegistryHelper;
import java.util.ServiceLoader;

public class Services {

  public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);

  private static IRegistryHelper registryHelper;

  public static IRegistryHelper registry() {
    if (registryHelper == null) {
      registryHelper = load(IRegistryHelper.class);
    }
    return registryHelper;
  }

  public static <T> T load(Class<T> clazz) {
    final T loadedService = ServiceLoader.load(clazz)
        .findFirst()
        .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
    Constants.LOG.debug("Loaded {} for service {}", loadedService, clazz);
    return loadedService;
  }
}
