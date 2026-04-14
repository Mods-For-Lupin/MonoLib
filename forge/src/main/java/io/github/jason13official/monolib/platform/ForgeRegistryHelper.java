package io.github.jason13official.monolib.platform;

import io.github.jason13official.monolib.platform.services.IRegistryHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.common.ForgeSpawnEggItem;

public class ForgeRegistryHelper implements IRegistryHelper {

  @Override
  public SpawnEggItem createSpawnEgg(EntityType<? extends Mob> type, int background, int highlight, Properties properties) {
    return new ForgeSpawnEggItem(() -> type, background, highlight, properties);
  }
}
