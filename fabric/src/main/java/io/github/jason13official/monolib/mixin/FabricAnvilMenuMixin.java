package io.github.jason13official.monolib.mixin;

import io.github.jason13official.monolib.impl.common.event.MonoLibFabricAnvilMenuEvents;
import io.github.jason13official.monolib.impl.common.event.MonoLibFabricAnvilMenuEvents.AnvilMenuResult;
import java.util.Objects;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public abstract class FabricAnvilMenuMixin extends ItemCombinerMenu {

  @Shadow
  private String itemName;

  @Shadow
  @Final
  private DataSlot cost;

  @Shadow
  private int repairItemCountCost;

  public FabricAnvilMenuMixin(@Nullable MenuType<?> type, int containerId, Inventory playerInventory, ContainerLevelAccess access) {
    super(type, containerId, playerInventory, access, ItemCombinerMenuSlotDefinition.create()
        .withSlot(0, 27, 47, itemStack -> true)
        .withSlot(1, 76, 47, itemStack -> true)
        .withResultSlot(2, 134, 47)
        .build());
  }

  @Inject(at = @At("RETURN"), method = "createResult")
  private void monolib$createResult(CallbackInfo ci) {

    AnvilMenu self = (AnvilMenu) (Object) this;
    Container inputSlots = this.inputSlots;

    ItemStack input, extra, output;
    input = inputSlots.getItem(0);
    extra = inputSlots.getItem(1);
    output = this.resultSlots.getItem(0);

    int baseCost = input.getOrDefault(DataComponents.REPAIR_COST, 0) + (extra.isEmpty() ? 0 : extra.getOrDefault(DataComponents.REPAIR_COST, 0));

    AnvilMenuResult result = MonoLibFabricAnvilMenuEvents.CREATE_RESULT.invoker().createResult(self, input, extra, output, this.itemName, baseCost, this.player);

    if (result == null) {
      return;
    }

    if (result.cost() > 0) {
      this.cost.set(result.cost());
    }

    if (result.repairItemCountCost() > 0) {
      this.repairItemCountCost = result.repairItemCountCost();
    }

    if (result.output() != null) {
      this.resultSlots.setItem(0, result.output());
    }
  }

  @Inject(at = @At("HEAD"), method = "onTake")
  private void monolib$onTake(Player player, ItemStack stack, CallbackInfo ci) {
    AnvilMenu self = (AnvilMenu) (Object) this;
    MonoLibFabricAnvilMenuEvents.ON_TAKE.invoker().onTake(self, player, stack, this.inputSlots.getItem(0), this.inputSlots.getItem(1));
  }
}
