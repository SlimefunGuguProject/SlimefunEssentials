package me.justahuman.slimefun_essentials.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SlimefunRecipeComponent {
    private final String id;
    private final List<String> multiId;
    
    public SlimefunRecipeComponent(String id) {
        this.id = id;
        this.multiId = null;
    }
    
    public SlimefunRecipeComponent(List<String> multiId) {
        this.id = null;
        this.multiId = multiId;
    }

    public JsonElement serialize() {
        if (this.id != null) {
            return new JsonPrimitive(this.id);
        } else if (this.multiId != null) {
            final JsonArray components = new JsonArray();
            for (String id : this.multiId) {
                components.add(id);
            }
            return components;
        }
        return null;
    }
    
    public static SlimefunRecipeComponent deserialize(JsonElement componentElement) {
        if (componentElement instanceof JsonPrimitive componentPrimitive && componentPrimitive.isString()) {
            return new SlimefunRecipeComponent(componentPrimitive.getAsString());
        } else if (componentElement instanceof JsonArray componentArray) {
            final List<String> multiId = new ArrayList<>();
            for (JsonElement idElement : componentArray) {
                if (idElement instanceof JsonPrimitive idPrimitive && idPrimitive.isString()) {
                    multiId.add(idPrimitive.getAsString());
                }
            }
            return new SlimefunRecipeComponent(multiId);
        }
        return null;
    }
}
