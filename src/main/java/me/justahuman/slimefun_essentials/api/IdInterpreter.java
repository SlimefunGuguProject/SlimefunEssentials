package me.justahuman.slimefun_essentials.api;

import me.justahuman.slimefun_essentials.client.ResourceLoader;
import me.justahuman.slimefun_essentials.client.SlimefunItemStack;
import me.justahuman.slimefun_essentials.client.SlimefunRecipeComponent;
import me.justahuman.slimefun_essentials.utils.Utils;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public interface IdInterpreter<T> {
    default T interpretId(@NotNull SlimefunRecipeComponent component, @NotNull String id, @NotNull T defaultValue) {
        if (id.isEmpty() || id.isBlank()) {
            return defaultValue;
        }
        
        if (!id.contains(":")) {
            Utils.warn("Invalid Ingredient Id:" + id);
            return defaultValue;
        }

        int chance = 100;
        if (id.contains("%")) {
            try {
                chance = Integer.parseInt(id.substring(id.indexOf("%") + 1));
                id = id.substring(0, id.indexOf("%"));
            } catch (Exception ignored) {}
        }
        
        final String type = id.substring(0, id.indexOf(":"));
        final String count = id.substring(id.indexOf(":") + 1);
        int amount = 1;
        try {
            amount = Integer.parseInt(count);
        } catch (Exception ignored) {}
        
        if (ResourceLoader.getSlimefunItems().containsKey(type)) {
            return fromSlimefunItemStack(chance, ResourceLoader.getSlimefunItems().get(type).copy(), amount, defaultValue);
        }

        // Complex Item
        if (type.startsWith("?")) {
            int index = 0;
            try {
                index = Integer.parseInt(type.substring(1));
            } catch (Exception ignored) {}
            return fromItemStack(chance, component.getComplexStacks().get(index), amount, defaultValue);
        }
        // Entity
        else if (type.startsWith("@")) {
            final Identifier identifier = new Identifier("minecraft:" + type.substring(1));
            if (! Registries.ENTITY_TYPE.containsId(identifier)) {
                Utils.warn("Invalid Ingredient Entity Id: " + id);
                return defaultValue;
            }
            return fromEntityType(chance, Registries.ENTITY_TYPE.get(identifier), amount, defaultValue);
        }
        // Fluid
        else if (type.startsWith("~")) {
            final Identifier identifier = new Identifier("minecraft:" + type.substring(1));
            if (!Registries.FLUID.containsId(identifier)) {
                Utils.warn("Invalid Ingredient Fluid Id: " + id);
                return defaultValue;
            }
            return fromFluid(chance, Registries.FLUID.get(identifier), amount, defaultValue);
        }
        // Tag
        else if (type.startsWith("#")) {
            final Identifier identifier = new Identifier("minecraft:" + type.substring(1));
            return fromTag(chance, TagKey.of(Registries.ITEM.getKey(), identifier), amount, defaultValue);
        }
        // Item (Or Mistake)
        else {
            final Identifier identifier = new Identifier("minecraft:" + type.toLowerCase());
            if (!Registries.ITEM.containsId(identifier)) {
                Utils.warn("Invalid Ingredient ItemStack Id: " + id);
                return defaultValue;
            }
            return fromItemStack(chance, Registries.ITEM.get(identifier).getDefaultStack().copy(), amount, defaultValue);
        }
    }
    
    T fromTag(int chance, TagKey<Item> tagKey, int amount, T defaultValue);
    T fromItemStack(int chance, ItemStack itemStack, int amount, T defaultValue);
    default T fromSlimefunItemStack(int chance, SlimefunItemStack slimefunItemStack, int amount, T defaultValue) {
        return fromItemStack(chance, slimefunItemStack.itemStack(), amount, defaultValue);
    }
    T fromFluid(int chance, Fluid fluid, int amount, T defaultValue);
    T fromEntityType(int chance, EntityType<?> entityType, int amount, T defaultValue);
}