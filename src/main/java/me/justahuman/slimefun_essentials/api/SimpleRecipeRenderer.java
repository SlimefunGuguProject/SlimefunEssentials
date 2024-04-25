package me.justahuman.slimefun_essentials.api;

import lombok.Getter;

@Getter
public abstract class SimpleRecipeRenderer implements RecipeRenderer {
    protected final Type type;

    protected SimpleRecipeRenderer(Type type) {
        this.type = type;
    }
}
