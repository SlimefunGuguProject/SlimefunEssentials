package me.justahuman.slimefun_essentials.compat.patchouli;

import me.justahuman.slimefun_essentials.api.IdInterpreter;
import me.justahuman.slimefun_essentials.client.SlimefunRecipeComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.TagKey;

import java.util.Arrays;

public class PatchouliIdInterpreter implements IdInterpreter<PatchouliWidget> {
    public PatchouliWidget fromRecipeComponent(SlimefunRecipeComponent component) {
        if (component.getId() != null) {
            return interpretId(component, component.getId(), PatchouliWidget.EMPTY);
        } else if (component.getMultiId() != null) {
            return PatchouliWidget.wrap(component.getMultiId().stream().map(id -> interpretId(component, id, PatchouliWidget.EMPTY)).toList());
        }
        return PatchouliWidget.EMPTY;
    }

    @Override
    public PatchouliWidget fromTag(int chance, TagKey<Item> tagKey, int amount, PatchouliWidget defaultValue) {
        return PatchouliWidget.wrap(Arrays.stream(Ingredient.fromTag(tagKey)
                .getMatchingStacks()).map(itemStack -> fromItemStack(chance, itemStack, amount, defaultValue)).toList());
    }

    @Override
    public PatchouliWidget fromItemStack(int chance, ItemStack itemStack, int amount, PatchouliWidget defaultValue) {
        return PatchouliWidget.wrap(itemStack.copyWithCount(amount));
    }

    @Override
    public PatchouliWidget fromFluid(int chance, Fluid fluid, int amount, PatchouliWidget defaultValue) {
        return fromItemStack(chance, fluid.getBucketItem().getDefaultStack(), amount, defaultValue);
    }

    @Override
    public PatchouliWidget fromEntityType(int chance, EntityType<?> entityType, int amount, PatchouliWidget defaultValue) {
        final ClientWorld world = MinecraftClient.getInstance().world;
        if (world == null) {
            return defaultValue;
        }

        try {
            Entity entity = entityType.create(world);
            return entity != null ? PatchouliWidget.wrap(entity, amount) : defaultValue;
        } catch (Exception ignored) {}
        return defaultValue;
    }
}
