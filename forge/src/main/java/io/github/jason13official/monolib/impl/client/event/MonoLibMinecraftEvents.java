package io.github.jason13official.monolib.impl.client.event;

import io.github.jason13official.monolib.MonoLibForge;
import net.minecraft.client.Minecraft;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;

public class MonoLibMinecraftEvents {

  public static class Hooks {

    public static void onClientStarted(Minecraft minecraft) {
      MonoLibForge.EVENT_BUS.post(new ClientStartedEvent(minecraft));
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
