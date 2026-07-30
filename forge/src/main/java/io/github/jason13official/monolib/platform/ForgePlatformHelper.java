package io.github.jason13official.monolib.platform;

import io.github.jason13official.monolib.Constants;
import io.github.jason13official.monolib.platform.services.IPlatformHelper;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTab.Builder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

public class ForgePlatformHelper implements IPlatformHelper {

  @Override
  public String getPlatformName() {

    return "Forge";
  }

  @Override
  public boolean isModLoaded(String modId) {

    return ModList.get().isLoaded(modId);
  }

  @Override
  public boolean isDevelopmentEnvironment() {

    return !FMLLoader.isProduction();
  }

  @Override
  public Path getGameDirectory() {

    return FMLLoader.getGamePath();
  }

  @Override
  public List<Path> getInstalledModPaths() {

    List<Path> paths = new ArrayList<>();

    FMLLoader.getLoadingModList().getMods().forEach(modInfo -> {
      paths.add(modInfo.getOwningFile().getFile().getFilePath());
    });

    return paths;
  }

  @Override
  public boolean isClientSide() {

    return FMLLoader.getDist() == Dist.CLIENT;
  }

  @Deprecated
  @Override
  public Builder tabBuilder() {

    return CreativeModeTab.builder();
  }
}