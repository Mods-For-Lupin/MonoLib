package io.github.jason13official.monolib.platform;

import io.github.jason13official.monolib.platform.services.IRegistryHelper;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.CreativeModeTab.Builder;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.SpawnEggItem;

public class FabricRegistryHelper implements IRegistryHelper {

  @Override
  public SpawnEggItem createSpawnEgg(EntityType<? extends Mob> type, int background, int highlight, Properties properties) {

    return new SpawnEggItem(type, background, highlight, properties);
  }

  @Override
  public Builder tabBuilder() {

    return FabricItemGroup.builder();
  }
}
