package me.justahuman.slimefun_essentials.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.Getter;
import me.justahuman.slimefun_essentials.utils.JsonUtils;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SlimefunRecipeComponent {
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
    
    public static SlimefunRecipeComponent deserialize(JsonArray complex, JsonElement componentElement) {
        if (componentElement instanceof JsonPrimitive componentPrimitive && componentPrimitive.isString()) {
            return new SlimefunRecipeComponent(complex, componentPrimitive.getAsString());
        } else if (componentElement instanceof JsonArray componentArray) {
            final List<String> multiId = new ArrayList<>();
            for (JsonElement idElement : componentArray) {
                if (idElement instanceof JsonPrimitive idPrimitive && idPrimitive.isString()) {
                    multiId.add(idPrimitive.getAsString());
                }
            }
            return new SlimefunRecipeComponent(complex, multiId);
        }
        return null;
    }
}
