package io.github.jason13official.monolib.impl.client.event;

import io.github.jason13official.monolib.MonoLibNeoForge;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;

public class MonoLibMinecraftEvents {

  public static class Hooks {

    public static void onClientStarted(Minecraft minecraft) {
      MonoLibNeoForge.EVENT_BUS.post(new ClientStartedEvent(minecraft));
    }
  }

  public static class ClientStartedEvent extends Event implements IModBusEvent {

    private final Minecraft minecraft;

    public ClientStartedEvent(Minecraft minecraft) {
      this.minecraft = minecraft;
    }

    public Minecraft getClientInstance() {
      return minecraft;
    }
  }
}
