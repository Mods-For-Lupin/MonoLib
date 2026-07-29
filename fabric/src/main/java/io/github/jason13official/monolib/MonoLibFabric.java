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
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

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

    ResourceLoader.get(PackType.SERVER_DATA).registerReloadListener(MonoLib.identifier(Constants.MOD_ID), new ResourceReloadListener());

    ServerLifecycleEvents.SERVER_STARTED.register(server -> Sailing.verifyAndAlert());
  }

  public <T> void bind(Registry<T> registry, Consumer<BiConsumer<T, Identifier>> source) {
    source.accept((t, rl) -> Registry.register(registry, rl, t));
  }

  public static class ResourceReloadListener extends SimplePreparableReloadListener<Void> {

    @Override
    public String getName() {
      return MonoLib.identifier(Constants.MOD_ID).toString();
    }

    @Override
    protected void apply(Void unused, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
      GsonConfigMapper.loadAll(Services.PLATFORM.getConfigDirectory());
    }

    @Override
    protected Void prepare(ResourceManager resourceManager, ProfilerFiller profilerFiller) {
      return null;
    }
  }
}
