package io.github.jason13official.monolib.platform;

import io.github.jason13official.monolib.Constants;
import io.github.jason13official.monolib.platform.services.IPlatformHelper;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModOrigin.Kind;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.CreativeModeTab.Builder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;

public class FabricPlatformHelper implements IPlatformHelper {

  @Override
  public String getPlatformName() {

    return "Fabric";
  }

  @Override
  public boolean isModLoaded(String modId) {

    return FabricLoader.getInstance().isModLoaded(modId);
  }

  @Override
  public boolean isDevelopmentEnvironment() {

    return FabricLoader.getInstance().isDevelopmentEnvironment();
  }

  @Override
  public Path getGameDirectory() {

    return FabricLoader.getInstance().getGameDir();
  }

  @Override
  public List<Path> getInstalledModPaths() {

    List<Path> paths = new ArrayList<>();

    FabricLoader.getInstance().getAllMods().forEach(modContainer -> {

      if (modContainer.getOrigin().getKind() == Kind.PATH) {
        // Constants.LOG.info("{}", modContainer.getOrigin().getPaths().get(0));
        paths.add(modContainer.getOrigin().getPaths().get(0));
      }
    });

    return paths;
  }

  @Deprecated
  @Override
  public Builder tabBuilder() {

    return FabricItemGroup.builder();
  }

  // @Override
  // public Item createSpawnEgg(EntityType<? extends Mob> type, int background, int highlight, Item.Properties properties) {
  //   return Items.STONE;
  // }
}
