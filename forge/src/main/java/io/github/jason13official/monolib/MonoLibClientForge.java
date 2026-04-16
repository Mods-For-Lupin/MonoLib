package io.github.jason13official.monolib;

import io.github.jason13official.monolib.impl.client.event.MonoLibMinecraftEvents.ClientStartedEvent;
import io.github.jason13official.monolib.impl.common.sailing.Sailing;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class MonoLibClientForge {

  public MonoLibClientForge(final IEventBus modEventBus) {

    modEventBus.addListener((Consumer<FMLClientSetupEvent>) event -> {
      MonoLibClient.init();
    });

    MonoLibForge.EVENT_BUS.addListener((Consumer<ClientStartedEvent>) event -> {
      Sailing.verifyAndAlert();
    });
  }
}
