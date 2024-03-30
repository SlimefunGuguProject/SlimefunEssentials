package me.justahuman.slimefun_essentials.compat.emi.handler;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import me.justahuman.slimefun_essentials.compat.emi.recipes.ProcessRecipe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.Slot;

import java.util.ArrayList;
import java.util.List;

public class MachineHandler implements StandardRecipeHandler<GenericContainerScreenHandler> {
    @Override
    public List<Slot> getInputSources(GenericContainerScreenHandler handler) {
        final List<Slot> slots = getCraftingSlots(handler);
        for (int i = handler.getRows() * 9; i < handler.getRows() * 9 + 36; i++) {
            slots.add(handler.getSlot(i));
        }
        return slots;
    }

    @Override
    public List<Slot> getCraftingSlots(GenericContainerScreenHandler handler) {
        final List<Slot> slots = new ArrayList<>();
        for (int r = 0; r < handler.getRows(); r++) {
            for (int i = 0; i < 9; i++) {
                final Slot slot = handler.getSlot(r * 9 + i);
                if (slot.getStack().isEmpty()) {
                    slots.add(slot);
                }
            }
        }
        return slots;
    }

    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        return recipe instanceof ProcessRecipe processRecipe
                && MinecraftClient.getInstance().currentScreen != null
                && MinecraftClient.getInstance().currentScreen.getTitle().contains(processRecipe.getCategory().getWorkstationName());
    }
}
