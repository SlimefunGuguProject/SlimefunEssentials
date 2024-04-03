package me.justahuman.slimefun_essentials.compat.jei.ingredient_handlers;

import me.justahuman.slimefun_essentials.client.ResourceLoader;
import me.justahuman.slimefun_essentials.client.SlimefunItemStack;
import me.justahuman.slimefun_essentials.utils.Utils;
import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class SlimefunStackType implements IIngredientType<SlimefunItemStack> {
    @Override
    @NotNull
    public Optional<SlimefunItemStack> castIngredient(@Nullable Object ingredient) {
        if (!(ingredient instanceof ItemStack itemStack)) {
            return Optional.empty();
        }

        final String id = Utils.getSlimefunId(itemStack);
        if (id != null && ResourceLoader.getSlimefunItems().containsKey(id)) {
            return Optional.of(ResourceLoader.getSlimefunItem(id));
        }
        return Optional.empty();
    }

    @Override
    @NotNull
    public Class<? extends SlimefunItemStack> getIngredientClass() {
        return SlimefunItemStack.class;
    }
}
