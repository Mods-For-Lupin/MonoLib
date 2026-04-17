package io.github.jason13official.monolib;

import io.github.jason13official.monolib.impl.client.event.MonoLibMinecraftEvents.ClientStartedEvent;
import io.github.jason13official.monolib.impl.common.sailing.Sailing;
import java.util.function.Consumer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

public class MonoLibClientNeoForge {

  public MonoLibClientNeoForge(final IEventBus modEventBus) {

    modEventBus.addListener((Consumer<FMLClientSetupEvent>) event -> MonoLibClient.init());

    MonoLibNeoForge.EVENT_BUS.addListener((Consumer<ClientStartedEvent>) event -> {
      Sailing.verifyAndAlert();
    });
  }
}
