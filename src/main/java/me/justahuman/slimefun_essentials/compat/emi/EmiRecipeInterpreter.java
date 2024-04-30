package me.justahuman.slimefun_essentials.compat.emi;

import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import me.justahuman.slimefun_essentials.api.IdInterpreter;
import me.justahuman.slimefun_essentials.client.SlimefunRecipe;
import me.justahuman.slimefun_essentials.client.SlimefunRecipeComponent;
import me.justahuman.slimefun_essentials.utils.Utils;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;

import java.util.ArrayList;
import java.util.List;

public class EmiRecipeInterpreter implements IdInterpreter<EmiIngredient> {
    public List<EmiIngredient> getInputIngredients(SlimefunRecipe slimefunRecipe) {
        return slimefunRecipe.inputs() != null && !slimefunRecipe.inputs().isEmpty() ? slimefunRecipe.inputs().stream().map(this::emiIngredientFromComponent).toList() : new ArrayList<>();
    }

    public List<EmiStack> getOutputStacks(SlimefunRecipe slimefunRecipe) {
        return slimefunRecipe.outputs() != null && !slimefunRecipe.outputs().isEmpty() ? slimefunRecipe.outputs().stream().map(this::emiStackFromComponent).toList() : new ArrayList<>();
    }

    public EmiStack emiStackFromComponent(SlimefunRecipeComponent component) {
        final EmiIngredient emiIngredient = emiIngredientFromComponent(component);
        if (emiIngredient instanceof EmiStack emiStack) {
            return emiStack;
        }

        Utils.warn("Invalid EmiStack Component: " + component);
        return EmiStack.EMPTY;
    }

    public EmiIngredient emiIngredientFromComponent(SlimefunRecipeComponent component) {
        final List<String> multiId = component.getMultiId();
        if (multiId != null) {
            final List<EmiIngredient> multiStack = new ArrayList<>();
            for (String id : multiId) {
                multiStack.add(interpretId(component, id, EmiStack.EMPTY));
            }
            return EmiIngredient.of(multiStack, multiStack.isEmpty() ? 1 : multiStack.get(0).getAmount());
        }

        return interpretId(component, component.getId(), EmiStack.EMPTY);
    }

    @Override
    public EmiIngredient fromTag(int chance, TagKey<Item> tagKey, int amount, EmiIngredient defaultValue) {
        return EmiIngredient.of(tagKey, amount).setChance(chance / 100F);
    }

    @Override
    public EmiIngredient fromItemStack(int chance, ItemStack itemStack, int amount, EmiIngredient defaultValue) {
        return EmiStack.of(itemStack, amount).setChance(chance / 100F);
    }

    @Override
    public EmiIngredient fromFluid(int chance, Fluid fluid, int amount, EmiIngredient defaultValue) {
        return EmiStack.of(fluid).setChance(chance / 100F);
    }

    @Override
    public EmiIngredient fromEntityType(int chance, EntityType<?> entityType, int amount, EmiIngredient defaultValue) {
        return new EntityEmiStack(entityType, amount).setChance(chance / 100F);
    }
}
