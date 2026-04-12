package com.cursee.monolib.core.sailing.warden;

import io.github.jason13official.monolib.Constants;
import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Adapted from Mycelium Mod Network's <a href="https://github.com/Mycelium-Mod-Network/Warden">Warden</a>, to identify potentially unsafe downloads.
 */
public class SailingWarden {

    public static final Map<String, String> UNSAFE_PATH_TO_UNSAFE_HOST_MAP = new HashMap<>();

    public static void processDirectoryOrFilePathStrings(String... filepathArguments) {
        final DomainRules rules = DomainRules.builtin();
        for (String filepath : filepathArguments) {
            try {
                if (!checkFile(rules, Paths.get(filepath).toFile())) {
                    System.out.println("No matching files were found.");
                }
            }
            catch (InvalidPathException e) {
                throw new IllegalArgumentException("Invalid path specified. '" + filepath + "'");
            }
        }

        if (!SailingWarden.UNSAFE_PATH_TO_UNSAFE_HOST_MAP.isEmpty()) {
            Constants.LOG.info("Unsafe download(s):");
            for (String key : SailingWarden.UNSAFE_PATH_TO_UNSAFE_HOST_MAP.keySet()) {
                Constants.LOG.info("- {} from {}", key, SailingWarden.UNSAFE_PATH_TO_UNSAFE_HOST_MAP.get(key));
            }
        }
    }

    private static boolean checkFile(DomainRules rules, File target) {

        boolean hasMatch = false;

        if (!target.exists()) {
            throw new IllegalArgumentException("The file does not exist! '" + target.getAbsolutePath() + "'");
        }
        else if (target.isFile()) {
            final ZoneIdentifier zoneId = ZoneIdentifier.of(target);
            if (zoneId != null && rules.test(zoneId)) {

                // System.out.println("File='" + target + "' host='" + zoneId.getHost() + "' referrer='" + zoneId.getReferrer() + "'.");

                UNSAFE_PATH_TO_UNSAFE_HOST_MAP.put(target.getName(), zoneId.getHost());
                hasMatch = true;
            }
        }
        else if (target.isDirectory()) {
            for (File subTarget : Objects.requireNonNull(target.listFiles())) {
                hasMatch = hasMatch || checkFile(rules, subTarget);
            }
        }

        return hasMatch;
    }
}
