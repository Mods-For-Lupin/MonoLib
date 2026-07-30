package io.github.jason13official.monolib.impl.common.sailing;

import io.github.jason13official.monolib.Constants;
import io.github.jason13official.monolib.MonoLib;
import io.github.jason13official.monolib.impl.common.CommonModConfig;
import io.github.jason13official.monolib.platform.Services;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FilenameUtils;

/// Verifies that registered mods are being created from their respective JAR files.
///
/// <p>Mods use [Sailing#register] to link a mod ID to an expected filename which may contain
/// the String `merged` which will be swapped for mod-loader titles during check.</p>
public class Sailing {

  private static final Map<String, String> FILENAME_BY_MOD_ID = new LinkedHashMap<>();
  private static final List<String> LOADER_VARIANTS = List.of("merged", "fabric", "forge", "neoforge");
  private static boolean verified = false;

  public static void register(String modId, String expectedFilename) {
    if (!FILENAME_BY_MOD_ID.containsKey(modId)) {
      FILENAME_BY_MOD_ID.put(modId, expectedFilename);
    } else {
      Constants.LOG.info("Attempted to overwrite expected filename for mod ID {} in MonoLib's Sailing (Anti-Piracy) API", modId);
    }
  }

  public static void verifyAndAlert() {

    if (verified || !CommonModConfig.VERIFY_JARS.get()) {
      return;
    }

    Constants.LOG.info("Verifying registered mod filenames...");

    List<String> failedIds = new ArrayList<>();
    List<Path> installedModFilepaths = Services.PLATFORM.getInstalledModPaths();
    List<String> installedModFilenames = installedModFilepaths.stream().map(path -> FilenameUtils.getName(path.toString())).toList();

    FILENAME_BY_MOD_ID.forEach((modId, expectedFilename) -> {
      boolean found = LOADER_VARIANTS.stream().anyMatch(loader -> installedModFilenames.contains(expectedFilename.replace("-merged-", "-" + loader + "-")));
      if (!found) {
        failedIds.add(modId);
      }
    });

    if (failedIds.isEmpty()) {
      return;
    }

    failedIds.forEach(s -> {
      Constants.LOG.info("Mod ID: {} expected filename similar to {}", s, FILENAME_BY_MOD_ID.get(s));
    });

    Constants.LOG.info("You a receiving this message because one or more of your mod files may have been altered and possibly not downloaded from an original and safe source.");
    Constants.LOG.info("Unofficial sources can contain malicious software or host outdated versions of mods, as well as removing ad revenue from mod authors.");
    Constants.LOG.info("Check out https://stopmodreposts.github.io/ for more information on why this feature exists.");
    Constants.LOG.info("Disable this check by updating 'config/monolib-server.json'");

    Constants.LOG.warn("You a receiving this message because one or more of your mod files may have been altered and possibly not downloaded from an original and safe source.");
    Constants.LOG.warn("Unofficial sources can contain malicious software or host outdated versions of mods, as well as removing ad revenue from mod authors.");
    Constants.LOG.warn("Check out https://stopmodreposts.github.io/ for more information on why this feature exists.");
    Constants.LOG.info("Disable this check by updating 'config/monolib-server.json'");

    verified = true;
  }
}
