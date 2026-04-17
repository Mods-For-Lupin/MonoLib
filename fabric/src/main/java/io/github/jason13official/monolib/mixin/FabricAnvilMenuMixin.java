package io.github.jason13official.monolib.mixin;

import com.cursee.monolib.callback.AnvilEventsFabric;
import com.cursee.monolib.core.event.FabricModAnvilEvents;
import io.github.jason13official.monolib.impl.common.event.MonoLibFabricAnvilMenuEvents;
import java.util.Objects;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import oshi.util.tuples.Triplet;

@Mixin(AnvilMenu.class)
public abstract class FabricAnvilMenuMixin extends ItemCombinerMenu {

  @Shadow
  private String itemName;

  @Shadow
  @Final
  private DataSlot cost;

  @Shadow
  private int repairItemCountCost;

  /// unused constructor
  public FabricAnvilMenuMixin(@Nullable MenuType<?> type, int containerId, Inventory playerInventory, ContainerLevelAccess access) {
    super(type, containerId, playerInventory, access);
  }

  @Inject(at = @At("RETURN"), method = "createResult")
  private void monolib$createResult(CallbackInfo ci) {

    AnvilMenu self = (AnvilMenu) (Object) this;
    Container inputSlots = this.inputSlots;

    ItemStack input, extra, output;
    input = inputSlots.getItem(0);
    extra = inputSlots.getItem(1);
    output = this.resultSlots.getItem(0);

    int baseCost = Objects.requireNonNull(input.get(DataComponents.REPAIR_COST)) + (extra.isEmpty() ? 0 : Objects.requireNonNull(extra.get(DataComponents.REPAIR_COST)));

    Triplet<Integer, Integer, ItemStack> result = MonoLibFabricAnvilMenuEvents.CREATE_RESULT.invoker().createResult(self, input, extra, output, this.itemName, baseCost, this.player);

    // check for deprecated event handlers, this should be removed "eventually"
    if (result == null) {
      result = AnvilEventsFabric.UPDATE.invoker().onUpdate(self, input, extra, output, this.itemName, baseCost, this.player);
      if (result == null) {
        FabricModAnvilEvents.CREATE_RESULT.invoker().createResult(self, input, extra, output, this.itemName, baseCost, this.player);
      }
    }

    if (result == null) {
      return;
    }

    if (result.getA() > 0) {
      this.cost.set(result.getA());
    }

    if (result.getB() > 0) {
      this.repairItemCountCost = result.getB();
    }

    if (result.getC() != null) {
      this.resultSlots.setItem(0, result.getC());
    }
  }

  @Inject(at = @At("HEAD"), method = "onTake")
  private void monolib$onTake(Player player, ItemStack stack, CallbackInfo ci) {
    AnvilMenu self = (AnvilMenu) (Object) this;
    MonoLibFabricAnvilMenuEvents.ON_TAKE.invoker().onTake(self, player, stack, this.inputSlots.getItem(0), this.inputSlots.getItem(1));
  }
}
