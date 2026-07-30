package io.github.jason13official.monolib.impl.common.config;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import io.github.jason13official.monolib.Constants;
import io.github.jason13official.monolib.api.common.EnvironmentSide;
import io.github.jason13official.monolib.api.common.config.ConfigGetterSetter.Commented;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ModSidedConfigIO {

  public static void load(Path configDir, String filenameBase, EnvironmentSide side, List<Commented<?>> commentedGetterSetters) {

    String suffix = side.name().toLowerCase();
    String filename = filenameBase + "-" + suffix + ".toml";

    File configDirectory = new File(configDir.toUri());
    if (!configDirectory.isDirectory() && !configDirectory.mkdirs()) {

      Constants.LOG.info("Failed to get or create config directory for {} {}", filename, configDirectory.getAbsolutePath());
      return;
    }

    Path configFilepath = configDir.resolve(filename);
    File configFile = new File(configFilepath.toUri());

    Config.setInsertionOrderPreserved(true);
    try (CommentedFileConfig config = CommentedFileConfig.builder(configFile).build()) {

      if (Files.exists(configFilepath)) {
        config.load();
      }

      System.out.println("iterating over getter setters");
      commentedGetterSetters.forEach(commented -> {

        // get
        if (config.contains(commented.key())) {
          commented.set(config.get(commented.key()));
        }

        // set
        config.setComment(commented.key(), " " + commented.comment());
        config.set(commented.key(), commented.get());
      });

      config.save();
    } catch (Exception e) {

      Constants.LOG.info(e.getMessage());
    }
  }
}
