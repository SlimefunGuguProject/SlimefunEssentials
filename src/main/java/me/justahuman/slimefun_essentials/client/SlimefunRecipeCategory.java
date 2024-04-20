package me.justahuman.slimefun_essentials.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.NonNull;
import me.justahuman.slimefun_essentials.utils.JsonUtils;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public record SlimefunRecipeCategory(String id, String type, Integer speed, Integer energy, List<SlimefunRecipe> recipes) {
    private static final Map<String, SlimefunRecipeCategory> recipeCategories = new LinkedHashMap<>();
    
    public static void deserialize(String id, JsonObject categoryObject) {
        final String type = JsonUtils.getStringOrDefault(categoryObject, "type", "process");
        final Integer speed = JsonUtils.getIntegerOrDefault(categoryObject, "speed", null);
        final Integer energy = JsonUtils.getIntegerOrDefault(categoryObject, "energy", null);
        final List<SlimefunRecipe> recipes = new ArrayList<>();
        for (JsonElement recipeElement : JsonUtils.getArrayOrDefault(categoryObject, "recipes", new JsonArray())) {
            if (! (recipeElement instanceof JsonObject recipeObject)) {
                continue;
            }
    
            recipes.add(SlimefunRecipe.deserialize(recipeObject, energy));
        }
        
        final String copy = JsonUtils.getStringOrDefault(categoryObject, "copy", "");
        final List<SlimefunRecipe> copiedRecipes = recipeCategories.containsKey(copy) ? recipeCategories.get(copy).recipes() : new ArrayList<>();
        recipes.addAll(copiedRecipes);
    
        recipeCategories.put(id, new SlimefunRecipeCategory(id, type, speed, energy, recipes));
    }
    
    /**
     * Returns an unmodifiable version of {@link SlimefunRecipeCategory#recipeCategories}
     *
     * @return {@link Map}
     */
    @NonNull
    public static Map<String, SlimefunRecipeCategory> getRecipeCategories() {
        return Collections.unmodifiableMap(recipeCategories);
    }

    public static void clear() {
        recipeCategories.clear();
    }

    public ItemStack getItemFromId() {
        return ResourceLoader.getSlimefunItem(this.id).itemStack();
    }

    public boolean hasSpeed() {
        return this.speed != null && this.speed != 1;
    }
}
