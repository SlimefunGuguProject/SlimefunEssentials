package me.justahuman.slimefun_essentials.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.NonNull;
import me.justahuman.slimefun_essentials.utils.JsonUtils;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public record SlimefunRecipeCategory(String id, String type, Integer speed, Integer energy, List<SlimefunRecipe> recipesFor, List<SlimefunRecipe> recipes) {
    private static final Map<String, SlimefunRecipeCategory> recipeCategories = new LinkedHashMap<>();
    private static final Map<String, SlimefunRecipeCategory> emptyCategories = new HashMap<>();
    private static final Map<String, String> toCopy = new HashMap<>();
    
    public static void deserialize(String id, JsonObject categoryObject) {
        final String type = JsonUtils.getStringOrDefault(categoryObject, "type", "process");
        final Integer speed = JsonUtils.getIntegerOrDefault(categoryObject, "speed", null);
        final Integer energy = JsonUtils.getIntegerOrDefault(categoryObject, "energy", null);
        final List<SlimefunRecipe> recipes = new ArrayList<>();

        final SlimefunRecipeCategory category = new SlimefunRecipeCategory(id, type, speed, energy, new ArrayList<>(), recipes);
        for (JsonElement recipeElement : JsonUtils.getArrayOrDefault(categoryObject, "recipes", new JsonArray())) {
            if (recipeElement instanceof JsonObject recipeObject) {
                recipes.add(SlimefunRecipe.deserialize(category, recipeObject, energy));
            }
        }

        toCopy.put(id, JsonUtils.getStringOrDefault(categoryObject, "copy", ""));
        recipeCategories.put(id, category);
    }

    public static void finalizeCategories() {
        for (Map.Entry<String, String> copyMap : toCopy.entrySet()) {
            final SlimefunRecipeCategory target = recipeCategories.get(copyMap.getKey());
            final SlimefunRecipeCategory parent = recipeCategories.get(copyMap.getValue());
            if (target != null && parent != null) {
                for (SlimefunRecipe slimefunRecipe : parent.recipes()) {
                    target.recipes().add(slimefunRecipe.copy(target));
                }
            }
        }
        toCopy.clear();

        for (SlimefunRecipeCategory category : recipeCategories.values()) {
            for (SlimefunRecipe recipe : category.recipes()) {
                for (SlimefunRecipeComponent output : recipe.outputs()) {
                    final List<String> multiId = output.getMultiId();
                    if (multiId != null) {
                        for (String id : multiId) {
                            final SlimefunRecipeCategory forCategory = fromId(id);
                            if (forCategory != null && !forCategory.recipesFor().contains(recipe)) {
                                forCategory.recipesFor().add(recipe);
                            }
                        }
                    } else {
                        final SlimefunRecipeCategory forCategory = fromId(output.getId());
                        if (forCategory != null && !forCategory.recipesFor().contains(recipe)) {
                            forCategory.recipesFor().add(recipe);
                        }
                    }
                }
            }
        }

        for (SlimefunRecipeCategory category : recipeCategories.values()) {
            category.recipesFor().sort(Comparator.comparingInt(SlimefunRecipeCategory::weight));
        }
    }

    public static int weight(SlimefunRecipe recipe) {
        final String type = recipe.parent().type();
        if (type.contains("grid")) {
            return 10;
        } else if (type.equals("ancient_altar")) {
            return 9;
        } else if (type.equals("smeltery")) {
            return 8;
        } else if (type.equals("reactor")) {
            return 7;
        } else {
            return 0;
        }
    }

    public static SlimefunRecipeCategory fromId(String component) {
        if (!component.contains(":")) {
            return null;
        }

        final String id = component.split(":")[0];
        if (recipeCategories.containsKey(id)) {
            return recipeCategories.get(id);
        } else if (emptyCategories.containsKey(id)) {
            return emptyCategories.get(id);
        } else if (ResourceLoader.getSlimefunItem(id) != null) {
            final SlimefunRecipeCategory category = new SlimefunRecipeCategory(id, "empty", null, null, new ArrayList<>(), new ArrayList<>());
            emptyCategories.put(id, category);
        }
        return null;
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

    public static Map<String, SlimefunRecipeCategory> getAllCategories() {
        final Map<String, SlimefunRecipeCategory> categories = new HashMap<>(recipeCategories);
        categories.putAll(emptyCategories);
        return categories;
    }

    public ItemStack getItemFromId() {
        return ResourceLoader.getSlimefunItem(this.id).itemStack();
    }

    public Integer speed() {
        return this.speed == null ? 1 : this.speed;
    }
}
