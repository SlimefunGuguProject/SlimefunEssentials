package me.justahuman.slimefun_essentials.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.Getter;
import me.justahuman.slimefun_essentials.utils.JsonUtils;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class SlimefunRecipeComponent {
    public static final SlimefunRecipeComponent EMPTY = new SlimefunRecipeComponent(new JsonArray(), "");
    private final List<ItemStack> complexStacks = new ArrayList<>();
    private final String id;
    private final List<String> multiId;
    
    public SlimefunRecipeComponent(JsonArray complex, String id) {
        this(complex, id, null);
    }
    
    public SlimefunRecipeComponent(JsonArray complex, List<String> multiId) {
        this(complex, null, multiId);
    }

    private SlimefunRecipeComponent(JsonArray complex, String id, List<String> multiId) {
        this.id = id;
        this.multiId = multiId;
        for (JsonElement element : complex) {
            if (element instanceof JsonObject object) {
                this.complexStacks.add(JsonUtils.deserializeItem(object));
            }
        }
    }
    
    public static SlimefunRecipeComponent deserialize(JsonArray complex, JsonElement element) {
        if (element instanceof JsonPrimitive primitive && primitive.isString()) {
            final String[] elements = primitive.getAsString().split(",");
            if (elements.length == 1) {
                return new SlimefunRecipeComponent(complex, primitive.getAsString());
            }
            return new SlimefunRecipeComponent(complex, Arrays.asList(elements));
        }
        return null;
    }
}
