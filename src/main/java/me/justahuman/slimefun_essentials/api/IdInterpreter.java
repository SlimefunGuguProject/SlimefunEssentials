package me.justahuman.slimefun_essentials.api;

import me.justahuman.slimefun_essentials.client.ResourceLoader;
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
    default T interpretId(@NotNull SlimefunRecipeComponent component, @NotNull String id, @NotNull T def) {
        if (id.isEmpty() || id.isBlank()) {
            return def;
        }
        
        if (!id.contains(":")) {
            Utils.warn("Invalid Ingredient Id:" + id);
            return def;
        }

        int chance = 100;
        if (id.contains("%")) {
            try {
                chance = Integer.parseInt(id.substring(id.indexOf('%') + 1));
                id = id.substring(0, id.indexOf('%'));
            } catch (Exception ignored) {}
        }

        int damage = 0;
        if (id.contains("^")) {
            try {
                damage = Integer.parseInt(id.substring(id.indexOf('^') + 1));
                id = id.substring(0, id.indexOf('^'));
            } catch (Exception ignored) {}
        }

        boolean consumed = true;
        if (id.contains("*")) {
            consumed = false;
            id = id.substring(0, id.indexOf('*'));
        }
        
        final String type = id.substring(0, id.indexOf(':'));
        final String count = id.substring(id.indexOf(':') + 1);
        int amount = 1;
        try {
            amount = Integer.parseInt(count);
        } catch (Exception ignored) {}

        // Slimefun Item
        if (ResourceLoader.getSlimefunItems().containsKey(type)) {
            final ItemStack itemStack = ResourceLoader.getSlimefunItems().get(type).copy().itemStack();
            if (damage > 0) {
                itemStack.setDamage(damage);
            }
            return fromItemStack(chance, consumed, itemStack, amount, def);
        }
        // Complex Item
        else if (type.startsWith("?")) {
            int index = 0;
            try {
                index = Integer.parseInt(type.substring(1));
            } catch (Exception ignored) {}
            return fromItemStack(chance, consumed, component.getComplexStacks().get(index), amount, def);
        }
        // Entity
        else if (type.startsWith("@")) {
            final Identifier identifier = new Identifier("minecraft:" + type.substring(1));
            if (! Registries.ENTITY_TYPE.containsId(identifier)) {
                Utils.warn("Invalid Ingredient Entity Id: " + id);
                return def;
            }
            return fromEntityType(chance, consumed, Registries.ENTITY_TYPE.get(identifier), amount, def);
        }
        // Fluid
        else if (type.startsWith("~")) {
            final Identifier identifier = new Identifier("minecraft:" + type.substring(1));
            if (!Registries.FLUID.containsId(identifier)) {
                Utils.warn("Invalid Ingredient Fluid Id: " + id);
                return def;
            }
            return fromFluid(chance, consumed, Registries.FLUID.get(identifier), amount, def);
        }
        // Tag
        else if (type.startsWith("#")) {
            final Identifier identifier = new Identifier("minecraft:" + type.substring(1));
            return fromTag(chance, consumed, TagKey.of(Registries.ITEM.getKey(), identifier), amount, def);
        }
        // Experience
        else if (type.equals("$")) {
            return fromEntityType(chance, consumed, EntityType.EXPERIENCE_ORB, amount, def);
        }
        // Item (Or Mistake)
        else {
            final Identifier identifier = new Identifier("minecraft:" + type.toLowerCase());
            if (!Registries.ITEM.containsId(identifier)) {
                Utils.warn("Invalid Ingredient ItemStack Id: " + id);
                return def;
            }

            final ItemStack itemStack = Registries.ITEM.get(identifier).getDefaultStack().copy();
            if (damage > 0) {
                itemStack.setDamage(damage);
            }
            return fromItemStack(chance, consumed, itemStack, amount, def);
        }
    }
    
    T fromTag(int chance, boolean consumed, TagKey<Item> tagKey, int amount, T def);
    T fromItemStack(int chance, boolean consumed, ItemStack itemStack, int amount, T def);
    T fromFluid(int chance, boolean consumed, Fluid fluid, int amount, T def);
    T fromEntityType(int chance, boolean consumed, EntityType<?> entityType, int amount, T def);
}