package me.justahuman.slimefun_essentials.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.NonNull;
import me.justahuman.slimefun_essentials.utils.JsonUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public record SlimefunItemGroup(Identifier identifier, ItemStack itemStack, List<String> content, List<String> requirements) {
    private static final Map<String, SlimefunItemGroup> itemGroups = new LinkedHashMap<>();

    public static void deserialize(String addon, String id, JsonObject groupObject) {
        final Identifier identifier = new Identifier(addon, id);
        final ItemStack itemStack = JsonUtils.deserializeItem(JsonUtils.getObjectOrDefault(groupObject, "item", null));
        final List<String> content = new ArrayList<>();
        final List<String> requirements = new ArrayList<>();

        for (JsonElement element : JsonUtils.getArrayOrDefault(groupObject, "items", new JsonArray())) {
            if (element instanceof JsonPrimitive primitive && primitive.isString()) {
                content.add(primitive.getAsString());
            }
        }

        for (JsonElement element : JsonUtils.getArrayOrDefault(groupObject, "nested", new JsonArray())) {
            if (element instanceof JsonPrimitive primitive && primitive.isString()) {
                content.add(primitive.getAsString());
            }
        }

        for (JsonElement element : JsonUtils.getArrayOrDefault(groupObject, "locked", new JsonArray())) {
            if (element instanceof JsonPrimitive primitive && primitive.isString()) {
                requirements.add(primitive.getAsString());
            }
        }

        if (groupObject.has("sub")) {
            requirements.add("sub");
        }

        itemGroups.put(identifier.toString(), new SlimefunItemGroup(identifier, itemStack, content, requirements));
    }

    /**
     * Returns an unmodifiable version of {@link SlimefunItemGroup#itemGroups}
     *
     * @return {@link Map}
     */
    @NonNull
    public static Map<String, SlimefunItemGroup> getItemGroups() {
        return Collections.unmodifiableMap(itemGroups);
    }

    public static void clear() {
        itemGroups.clear();
    }

    public static void addParents() {
        for (SlimefunItemGroup parent : itemGroups.values()) {
            for (String content : parent.content()) {
                if (content.contains(":")) {
                    final SlimefunItemGroup child = itemGroups.get(content);
                    if (child != null) {
                        child.requirements().add("parent: " + parent.identifier().toString());
                    }
                }
            }
        }
    }
}
