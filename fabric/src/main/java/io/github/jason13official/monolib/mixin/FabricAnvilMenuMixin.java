package io.github.jason13official.monolib.mixin;

import com.cursee.monolib.callback.AnvilEventsFabric;
import com.cursee.monolib.core.event.FabricModAnvilEvents;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import oshi.util.tuples.Triplet;

@Deprecated(forRemoval = true)
@Mixin(value = AnvilMenu.class, priority = 1002) /// apply after Collective's injection at a similar point
public abstract class FabricAnvilMenuMixin extends ItemCombinerMenu {

    @Shadow private String itemName;
    @Shadow private int repairItemCountCost;
    @Final @Shadow private DataSlot cost;

    /// START CREATE_RESULT EVENT

    /** This differs slightly from Collective, which targets TAIL rather than RETURN */
    @Inject(method = "createResult()V", at = @At(value= "RETURN"))
    public void monolib$createResult(CallbackInfo info) {

        AnvilMenu instance = (AnvilMenu) (Object) this;
        Container injected$inputSlots = this.inputSlots;

        ItemStack injected$slotLeft = injected$inputSlots.getItem(0);
        ItemStack injected$slotRight = injected$inputSlots.getItem(1);
        ItemStack injected$slotOutput = this.resultSlots.getItem(0);

        int injected$baseCost = injected$slotLeft.getBaseRepairCost() + (injected$slotRight.isEmpty() ? 0 : injected$slotRight.getBaseRepairCost());

        Triplet<Integer, Integer, ItemStack> injected$triple = FabricModAnvilEvents.CREATE_RESULT.invoker().createResult(instance, injected$slotLeft, injected$slotRight, injected$slotOutput, itemName, injected$baseCost, this.player);

        /// Catch events registered to deprecated handlers
        if (injected$triple == null) {
            injected$triple = AnvilEventsFabric.UPDATE.invoker().onUpdate(instance, injected$slotLeft, injected$slotRight, injected$slotOutput, itemName, injected$baseCost, this.player);
            if (injected$triple == null) {
                injected$triple = AnvilEventsFabric.UPDATE.invoker().onUpdate(instance, injected$slotLeft, injected$slotRight, injected$slotOutput, itemName, injected$baseCost, this.player);
            }
        }

        if (injected$triple == null) return;

        if (injected$triple.getA() >= 0) cost.set(injected$triple.getA());

        if (injected$triple.getB() >= 0) repairItemCountCost = injected$triple.getB();

        if (injected$triple.getC() != null) this.resultSlots.setItem(0, injected$triple.getC());
    }

    /// END CREATE_RESULT EVENT
    /// blank
    /// START ON_TAKE EVENT

    @Inject(method = "onTake", at = @At("HEAD"))
    private void monolib$onTakeHEAD(Player player, ItemStack stack, CallbackInfo ci) {
        AnvilMenu instance = (AnvilMenu) (Object) this;
        FabricModAnvilEvents.ON_TAKE.invoker().onTake(instance, player, stack, this.inputSlots.getItem(0), this.inputSlots.getItem(1));
    }

    /// END ON_TAKE EVENT

    public FabricAnvilMenuMixin(MenuType<?> menuType, int containerID, Inventory inventory, ContainerLevelAccess containerLevelAccess) {
        super(menuType, containerID, inventory, containerLevelAccess);
    }
}
