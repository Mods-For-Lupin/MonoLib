package io.github.jason13official.monolib;

import io.github.jason13official.monolib.impl.common.command.ModCommands;
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
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.registries.RegisterEvent;

@Mod(Constants.MOD_ID)
public class MonoLibForge {

  public static IEventBus EVENT_BUS;

  public MonoLibForge(final FMLJavaModLoadingContext context) {
    EVENT_BUS = context.getModEventBus();

    bind(Registries.BLOCK, ModBlocks::register);
    bind(Registries.ENTITY_TYPE, ModEntities::register);
    bind(Registries.ITEM, ModItems::register);
    bind(Registries.PARTICLE_TYPE, ModParticles::register);
    bind(Registries.BLOCK_ENTITY_TYPE, ModTiles::register);
    bind(Registries.MENU, ModMenus::register);
    bind(Registries.CREATIVE_MODE_TAB, ModTabs::register);

    EVENT_BUS.addListener((Consumer<FMLCommonSetupEvent>) event -> MonoLib.init());

    MinecraftForge.EVENT_BUS.addListener((Consumer<RegisterCommandsEvent>) event ->
        ModCommands.register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection()));

    MinecraftForge.EVENT_BUS.addListener((Consumer<AddReloadListenerEvent>) event -> {
      event.addListener(new ResourceReloadListener());
    });

    MinecraftForge.EVENT_BUS.addListener((Consumer<ServerStartedEvent>) event -> {
      Sailing.verifyAndAlert();
    });

    if (FMLLoader.getDist() == Dist.CLIENT) {
      new MonoLibClientForge(EVENT_BUS);
    }
  }

  @Deprecated
  @SuppressWarnings("all")
  public MonoLibForge() {
    this(FMLJavaModLoadingContext.get());
  }

  public <T> void bind(ResourceKey<Registry<T>> registryKey, Consumer<BiConsumer<T, ResourceLocation>> source) {

    EVENT_BUS.addListener((Consumer<RegisterEvent>) event -> {
      if (registryKey.equals(event.getRegistryKey())) {
        source.accept((t, rl) -> event.register(registryKey, rl, () -> t));
      }
    });
  }

  public static class ResourceReloadListener extends SimplePreparableReloadListener<Void> {

    @Override
    public String getName() {
      return MonoLib.identifier(Constants.MOD_ID).toString();
    }

    @Override
    protected void apply(Void unused, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
      // load config on resource reload
      GsonConfigMapper.loadAll(Services.PLATFORM.getConfigDirectory());
    }

    @Override
    protected Void prepare(ResourceManager resourceManager, ProfilerFiller profilerFiller) {
      return null;
    }
  }
}