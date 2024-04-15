package me.justahuman.slimefun_essentials.compat.emi;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.api.stack.EmiStack;
import me.justahuman.slimefun_essentials.client.ResourceLoader;
import me.justahuman.slimefun_essentials.client.SlimefunCategory;
import me.justahuman.slimefun_essentials.client.SlimefunItemStack;
import me.justahuman.slimefun_essentials.client.SlimefunRecipe;
import me.justahuman.slimefun_essentials.compat.emi.handler.GridHandler;
import me.justahuman.slimefun_essentials.compat.emi.recipes.AncientAltarRecipe;
import me.justahuman.slimefun_essentials.compat.emi.recipes.GridRecipe;
import me.justahuman.slimefun_essentials.compat.emi.recipes.ProcessRecipe;
import me.justahuman.slimefun_essentials.compat.emi.recipes.ReactorRecipe;
import me.justahuman.slimefun_essentials.compat.emi.recipes.SmelteryRecipe;
import me.justahuman.slimefun_essentials.utils.TextureUtils;
import me.justahuman.slimefun_essentials.utils.Utils;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class EmiIntegration implements EmiPlugin {
    public static final EmiRecipeInterpreter RECIPE_INTERPRETER = new EmiRecipeInterpreter();
    private static final Comparison SLIMEFUN_ID = Comparison.compareData(stack -> Utils.getSlimefunId(stack.getNbt()));
    private static final Map<String, SlimefunEmiCategory> slimefunCategories = new HashMap<>();
    
    @Override
    public void register(EmiRegistry emiRegistry) {
        if (!Utils.shouldFunction()) {
            return;
        }

        for (SlimefunItemStack slimefunItemStack : ResourceLoader.getSlimefunItems().values()) {
            emiRegistry.setDefaultComparison(EmiStack.of(slimefunItemStack.itemStack()), SLIMEFUN_ID);
        }
        slimefunCategories.clear();

        for (SlimefunCategory slimefunCategory : SlimefunCategory.getSlimefunCategories().values()) {
            final String workstationId = slimefunCategory.id();
            final Identifier categoryIdentifier = Utils.newIdentifier(workstationId);
            final EmiStack workStation = RECIPE_INTERPRETER.emiStackFromId(workstationId + ":1");
            final SlimefunEmiCategory slimefunEmiCategory;
            if (slimefunCategories.containsKey(workstationId)) {
                slimefunEmiCategory = slimefunCategories.get(workstationId);
            } else {
                slimefunEmiCategory = new SlimefunEmiCategory(categoryIdentifier, workStation);
                slimefunCategories.put(workstationId, slimefunEmiCategory);
                emiRegistry.addCategory(slimefunEmiCategory);
                emiRegistry.addWorkstation(slimefunEmiCategory, workStation);
            }
            
            for (SlimefunRecipe slimefunRecipe : slimefunCategory.recipes()) {
                emiRegistry.addRecipe(getEmiRecipe(slimefunCategory, slimefunRecipe, slimefunEmiCategory));
            }
        }

        for (SlimefunItemStack slimefunItemStack : ResourceLoader.getSlimefunItems().values()) {
            emiRegistry.addEmiStack(EmiStack.of(slimefunItemStack.itemStack()));
        }

        emiRegistry.addRecipeHandler(ScreenHandlerType.GENERIC_3X3, new GridHandler());
        // TODO: Support machine categories for all mods
        // emiRegistry.addRecipeHandler(ScreenHandlerType.GENERIC_9X1, new MachineHandler());
        // emiRegistry.addRecipeHandler(ScreenHandlerType.GENERIC_9X2, new MachineHandler());
        // emiRegistry.addRecipeHandler(ScreenHandlerType.GENERIC_9X3, new MachineHandler());
        // emiRegistry.addRecipeHandler(ScreenHandlerType.GENERIC_9X4, new MachineHandler());
        // emiRegistry.addRecipeHandler(ScreenHandlerType.GENERIC_9X5, new MachineHandler());
        // emiRegistry.addRecipeHandler(ScreenHandlerType.GENERIC_9X6, new MachineHandler());
    }

    public static EmiRecipe getEmiRecipe(SlimefunCategory slimefunCategory, SlimefunRecipe slimefunRecipe, SlimefunEmiCategory category) {
        final String type = slimefunCategory.type();
        if (type.equals("ancient_altar")) {
            return new AncientAltarRecipe(slimefunCategory, slimefunRecipe, category);
        } else if (type.equals("smeltery")) {
            return new SmelteryRecipe(slimefunCategory, slimefunRecipe, category);
        } else if (type.equals("reactor")) {
            return new ReactorRecipe(slimefunCategory, slimefunRecipe, category);
        } else if (type.contains("grid")) {
            return new GridRecipe(slimefunCategory, slimefunRecipe, category, TextureUtils.getSideSafe(type));
        } else {
            return new ProcessRecipe(slimefunCategory, slimefunRecipe, category);
        }
    }
}
