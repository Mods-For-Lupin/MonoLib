package io.github.jason13official.monolib.platform;

import io.github.jason13official.monolib.platform.services.IRegistryHelper;
import java.util.function.Supplier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTab.Builder;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.SpawnEggItem;

public class NeoForgeRegistryHelper implements IRegistryHelper {

  @Override
  public SpawnEggItem createSpawnEgg(EntityType<? extends Mob> type, int background, int highlight, Properties properties) {

    return new SpawnEggItem(properties.spawnEgg(type));
  }

  @Override
  public Builder tabBuilder() {
    return CreativeModeTab.builder();
  }
}
