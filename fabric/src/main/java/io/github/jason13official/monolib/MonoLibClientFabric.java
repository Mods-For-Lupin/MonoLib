package io.github.jason13official.monolib;

import io.github.jason13official.monolib.impl.common.sailing.Sailing;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

public class MonoLibClientFabric implements ClientModInitializer {

  @Override
  public void onInitializeClient() {

    MonoLibClient.init();

    ClientLifecycleEvents.CLIENT_STARTED.register(client -> Sailing.verifyAndAlert());
  }
}
