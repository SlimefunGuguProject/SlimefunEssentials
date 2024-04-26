package me.justahuman.slimefun_essentials.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.NonNull;
import me.justahuman.slimefun_essentials.utils.JsonUtils;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SlimefunRecipeCategory {
    private static final Map<String, SlimefunRecipeCategory> recipeCategories = new LinkedHashMap<>();
    private static final Map<String, SlimefunRecipeCategory> emptyCategories = new HashMap<>();
    private static final Map<String, String> toCopy = new HashMap<>();

    private final SlimefunItemStack itemStack;
    private final String type;
    private final Integer speed;
    private final Integer energy;
    private final List<SlimefunRecipe> childRecipes;
    private SlimefunRecipe recipe = null;

    public SlimefunRecipeCategory(SlimefunItemStack itemStack, String type, Integer speed, Integer energy, List<SlimefunRecipe> childRecipes) {
        this.itemStack = itemStack;
        this.type = type;
        this.speed = speed;
        this.energy = energy;
        this.childRecipes = childRecipes;
    }

    public String id() {
        return this.itemStack.id();
    }

    public SlimefunItemStack slimefunItem() {
        return this.itemStack;
    }

    public ItemStack itemStack() {
        return this.itemStack.itemStack();
    }

    public String type() {
        return this.type;
    }

    public Integer speed() {
        return this.speed == null ? 1 : this.speed;
    }

    public Integer energy() {
        return this.energy;
    }

    public SlimefunRecipe recipe() {
        return this.recipe;
    }

    public List<SlimefunRecipe> childRecipes() {
        return this.childRecipes;
    }
    
    public static void deserialize(String id, JsonObject categoryObject) {
        final SlimefunItemStack itemStack = ResourceLoader.getSlimefunItem(id);
        final String type = JsonUtils.getStringOrDefault(categoryObject, "type", "process");
        final Integer speed = JsonUtils.getIntegerOrDefault(categoryObject, "speed", null);
        final Integer energy = JsonUtils.getIntegerOrDefault(categoryObject, "energy", null);
        final List<SlimefunRecipe> recipes = new ArrayList<>();

        final SlimefunRecipeCategory category = new SlimefunRecipeCategory(itemStack, type, speed, energy, recipes);
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
                for (SlimefunRecipe slimefunRecipe : parent.childRecipes()) {
                    target.childRecipes().add(slimefunRecipe.copy(target));
                }
            }
        }
        toCopy.clear();

        for (SlimefunRecipeCategory category : recipeCategories.values()) {
            for (SlimefunRecipe recipe : category.childRecipes()) {
                final int weight = weight(recipe);
                for (SlimefunRecipeComponent output : recipe.outputs()) {
                    final List<String> multiId = output.getMultiId();
                    if (multiId != null) {
                        for (String id : multiId) {
                            final SlimefunRecipeCategory forCategory = fromId(id);
                            if (forCategory != null && weight >= weight(forCategory.recipe)) {
                                forCategory.recipe = recipe;
                            }
                        }
                    } else {
                        final SlimefunRecipeCategory forCategory = fromId(output.getId());
                        if (forCategory != null && weight >= weight(forCategory.recipe)) {
                            forCategory.recipe = recipe;
                        }
                    }
                }
            }
        }
    }

    public static int weight(SlimefunRecipe recipe) {
        if (recipe == null) {
            return 0;
        }

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
            final SlimefunItemStack itemStack = ResourceLoader.getSlimefunItem(id);
            final SlimefunRecipeCategory category = new SlimefunRecipeCategory(itemStack, "empty", null, null, new ArrayList<>());
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
}
