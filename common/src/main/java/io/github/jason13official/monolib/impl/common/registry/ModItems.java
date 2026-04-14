package io.github.jason13official.monolib.impl.common.registry;

import io.github.jason13official.monolib.MonoLib;
import io.github.jason13official.monolib.platform.Services;
import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;

public class ModItems {

  public static void register(BiConsumer<Item, ResourceLocation> consumer) {

    consumer.accept(Services.registry().createSpawnEgg(EntityType.COW, 0x000000, 0xFFFFFF, new Properties()), MonoLib.identifier("safe_service_egg"));
  }
}
