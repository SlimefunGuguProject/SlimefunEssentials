package me.justahuman.slimefun_essentials.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class JsonUtils {
    private static final Gson gson = new Gson().newBuilder().setPrettyPrinting().create();

    public static JsonObject getObject(JsonObject parent, String key, JsonObject def) {
        return parent.get(key) instanceof JsonObject json ? json : def;
    }
    
    public static JsonArray getArray(JsonObject parent, String key, JsonArray def, boolean set) {
        final JsonArray result = getArray(parent, key, def);
        if (set) {
            parent.add(key, result);
        }
        return result;
    }
    
    public static JsonArray getArray(JsonObject parent, String key, JsonArray def) {
        final JsonElement value = parent.get(key);
        if (value instanceof JsonArray array) {
            return array;
        } else if (value == null) {
            return def;
        }

        final JsonArray array = new JsonArray();
        array.add(value);
        return array;
    }
    
    public static String getString(JsonObject parent, String key, String def) {
        return parent.get(key) instanceof JsonPrimitive primitive && primitive.isString() ? primitive.getAsString() : def;
    }
    
    public static Boolean getBool(JsonObject parent, String key, Boolean def, boolean set) {
        final Boolean result = getBool(parent, key, def);
        if (set) {
            parent.addProperty(key, result);
        }
        return result;
    }
    
    public static Boolean getBool(JsonObject parent, String key, Boolean def) {
        return parent.get(key) instanceof JsonPrimitive primitive && primitive.isBoolean() ? primitive.getAsBoolean() : def;
    }
    
    public static Long getLong(JsonObject parent, String key, Long def) {
        return parent.get(key) instanceof JsonPrimitive primitive && primitive.isNumber() ? primitive.getAsLong() : def;
    }
    
    public static Integer getInt(JsonObject parent, String key, Integer def) {
        return parent.get(key) instanceof JsonPrimitive primitive && primitive.isNumber() ? primitive.getAsInt() : def;
    }

    public static String serializeItem(ItemStack itemStack) {
        final JsonObject json = new JsonObject();
        final NbtCompound nbt = itemStack.getNbt();
        json.addProperty("item", Registries.ITEM.getId(itemStack.getItem()).toString());
        json.addProperty("amount", itemStack.getCount());
        if (nbt != null) {
            json.addProperty("nbt", nbt.toString());
        }
        return json.toString();
    }

    public static ItemStack deserializeItem(String string) {
        return deserializeItem(gson.fromJson(string, JsonObject.class));
    }
    
    public static ItemStack deserializeItem(JsonObject json) {
        if (json == null || json.isEmpty() || !json.has("item")) {
            return ItemStack.EMPTY;
        }

        final ItemStack itemStack = new ItemStack(Registries.ITEM.get(new Identifier(json.get("item").getAsString())));
        itemStack.setCount(JsonHelper.getInt(json, "amount", 1));
        if (JsonHelper.hasString(json, "nbt")) {
            itemStack.setNbt(parseNbt(json));
        }
        
        return itemStack;
    }
    
    public static NbtCompound parseNbt(JsonObject json) {
        return parseNbt(JsonHelper.getString(json, "nbt"));
    }
    
    public static NbtCompound parseNbt(String nbt) {
        try {
            return StringNbtReader.parse(nbt);
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
