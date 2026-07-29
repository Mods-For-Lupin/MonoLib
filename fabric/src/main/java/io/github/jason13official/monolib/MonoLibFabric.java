package io.github.jason13official.monolib;

import io.github.jason13official.monolib.impl.common.registry.ModCommands;
import io.github.jason13official.monolib.impl.common.registry.ModBlocks;
import io.github.jason13official.monolib.impl.common.registry.ModEntities;
import io.github.jason13official.monolib.impl.common.registry.ModItems;
import io.github.jason13official.monolib.impl.common.registry.ModMenus;
import io.github.jason13official.monolib.impl.common.registry.ModParticles;
import io.github.jason13official.monolib.impl.common.registry.ModTabs;
import io.github.jason13official.monolib.impl.common.registry.ModTiles;
import io.github.jason13official.monolib.impl.common.sailing.Sailing;
import io.github.jason13official.monolib.impl.common.util.GsonConfigMapper;
import io.github.jason13official.monolib.platform.Services;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;

public class MonoLibFabric implements ModInitializer {

  @Override
  public void onInitialize() {

    MonoLib.initConfig();

    bind(BuiltInRegistries.BLOCK, ModBlocks::register);
    bind(BuiltInRegistries.ENTITY_TYPE, ModEntities::register);
    bind(BuiltInRegistries.ITEM, ModItems::register);
    bind(BuiltInRegistries.PARTICLE_TYPE, ModParticles::register);
    bind(BuiltInRegistries.BLOCK_ENTITY_TYPE, ModTiles::register);
    bind(BuiltInRegistries.MENU, ModMenus::register);
    bind(BuiltInRegistries.CREATIVE_MODE_TAB, ModTabs::register);

    MonoLib.init();

    CommandRegistrationCallback.EVENT.register(ModCommands::register);

    ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new ResourceReloadListener());

    ServerLifecycleEvents.SERVER_STARTED.register(server -> {
      Sailing.verifyAndAlert();
    });
  }

  public <T> void bind(Registry<T> registry, Consumer<BiConsumer<T, ResourceLocation>> source) {

    source.accept((t, rl) -> Registry.register(registry, rl, t));
  }

  public static class ResourceReloadListener implements SimpleSynchronousResourceReloadListener {

    @Override
    public ResourceLocation getFabricId() {
      return MonoLib.identifier(Constants.MOD_ID);
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
      // load config on resource reload
      GsonConfigMapper.loadAll(Services.PLATFORM.getConfigDirectory());
    }
  }
}
