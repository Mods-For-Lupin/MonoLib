package io.github.jason13official.monolib.impl.common.sailing;

import io.github.jason13official.monolib.Constants;
import io.github.jason13official.monolib.impl.common.CommonModConfig;
import io.github.jason13official.monolib.platform.Services;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FilenameUtils;

/// registers a mod ID `monolib` to an expected filename `monolib-fabric-1.20.1-4.1.0.jar`
public class Sailing {

  private static final Map<String, String> FILENAME_BY_MOD_ID = new LinkedHashMap<>();
  private static final List<String> LOADER_STRINGS = List.of("fabric", "forge", "neoforge");
  private static final List<String> LOADER_VARIANTS = List.of("merged", "fabric", "forge", "neoforge");
  private static boolean verified = false;

  public static void register(String modId, String expectedFilename) {

    // ignore if verified or not meant to verify
    if (verified || !CommonModConfig.VERIFY_JARS.get()) {

      // explicitly set to true in case config is returning early
      verified = true;
      return;
    }

    FILENAME_BY_MOD_ID.putIfAbsent(modId, expectedFilename);
  }

  public static void verifyAndAlert() {

    // ignore if verified or not meant to verify
    if (verified || !CommonModConfig.VERIFY_JARS.get()) {

      // explicitly set to true in case config is returning early
      verified = true;
      return;
    }

    Path gameDir = Services.PLATFORM.getGameDirectory();
    File modsDirectory = new File(gameDir.resolve("mods").toUri());

    Constants.LOG.info("Verifying registered JAR filenames");

    if (!modsDirectory.isDirectory()) {

      Constants.LOG.info("Did not find expected \"mods\" folder, skipping verification. Tried: {}", modsDirectory.getAbsolutePath());
      return;
    }

    List<Path> modPaths = Services.PLATFORM.getInstalledModPaths();
    List<String> modFilenames = modPaths.stream().map(p -> FilenameUtils.getName(p.toString())).toList();

    List<String> notFoundModIds = new ArrayList<>();
    FILENAME_BY_MOD_ID.forEach((modId, expected) -> {

      // explicitly check the expected name first
      boolean found = modFilenames.contains(expected);

      // then try swapping loader for "merged" until found
      // i.e. some of our mods are released merged JARs examplemod-merged-1.20.1-0.1.0.jar
      // some are released loader-specific examplemod-fabric-1.20.1-0.1.0.jar, but they all follow
      // <mod_id>-<loader>-<mc_version>-<mod_version>.jar
      if (!found) {
        for (String loaderString : LOADER_STRINGS) {
          if (!found) {
            found = modFilenames.contains(expected.replace(loaderString, "merged"))
                || modFilenames.contains(expected.replace("merged", loaderString));
          }
        }
      }

      if (!found) notFoundModIds.add(modId);
    });

    if (notFoundModIds.isEmpty()) {

      Constants.LOG.info("Found all registered filenames.");
      verified = true;
      return;
    }

    for (String notFoundModId : notFoundModIds) {

      Constants.LOG.info("Failed to find expected filename {} (or \"merged\" variant) for mod ID that registered it {}", FILENAME_BY_MOD_ID.get(notFoundModId), notFoundModId);
    }

    Constants.LOG.info("You are receiving this message because one or more of your mod files may have been altered and possibly not downloaded from an original and safe source.");
    Constants.LOG.info("Unofficial sources can contain malicious software or host outdated versions of mods, as well as removing ad revenue from mod authors.");
    Constants.LOG.info("Check out https://stopmodreposts.github.io/ for more information on why this feature exists.");
    Constants.LOG.info("Disable this check by updating \"config/monolib-common.toml\"");

    verified = true;
  }
}
