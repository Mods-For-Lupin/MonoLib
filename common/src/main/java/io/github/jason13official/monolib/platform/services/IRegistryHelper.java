package io.github.jason13official.monolib.platform.services;

import java.util.function.Supplier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.SpawnEggItem;

public interface IRegistryHelper {

  SpawnEggItem createSpawnEgg(EntityType<? extends Mob> type, int background, int highlight, Item.Properties properties);

  CreativeModeTab.Builder tabBuilder();
}
