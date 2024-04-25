package me.justahuman.slimefun_essentials.client;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.systems.RenderSystem;
import lombok.NonNull;
import me.justahuman.slimefun_essentials.utils.JsonUtils;
import me.justahuman.slimefun_essentials.utils.TextureUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public record SlimefunLabel(String id, Identifier light, Identifier dark, Identifier book, int u, int v, int width, int height) {
    private static final Map<String, SlimefunLabel> slimefunLabels = new LinkedHashMap<>();

    public SlimefunLabel(String id, int u, int v, int width, int height) {
        this(id, TextureUtils.WIDGETS, TextureUtils.WIDGETS_DARK, TextureUtils.WIDGETS_BOOK, u, v, width, height);
    }

    public SlimefunLabel(String id, Identifier light, Identifier dark, Identifier book, int u, int v) {
        this(id, light, dark, book, u, v, TextureUtils.LABEL_SIZE, TextureUtils.LABEL_SIZE);
    }

    public static void deserialize(String id, JsonObject labelObject) {
        slimefunLabels.put(id, new SlimefunLabel(
                id,
                new Identifier(JsonUtils.getStringOrDefault(labelObject, "light", "slimefun_essentials:textures/gui/widgets.png")),
                new Identifier(JsonUtils.getStringOrDefault(labelObject, "dark", "slimefun_essentials:textures/gui/widgets_dark.png")),
                new Identifier(JsonUtils.getStringOrDefault(labelObject, "dark", "slimefun_essentials:textures/gui/widgets_book.png")),
                JsonUtils.getIntegerOrDefault(labelObject, "u", 0),
                JsonUtils.getIntegerOrDefault(labelObject, "v", 0)
        ));
    }
    
    /**
     * Returns an unmodifiable version of {@link SlimefunLabel#slimefunLabels}
     *
     * @return {@link Map}
     */
    @NonNull
    public static Map<String, SlimefunLabel> getSlimefunLabels() {
        return Collections.unmodifiableMap(slimefunLabels);
    }

    public static void clear() {
        slimefunLabels.clear();
    }

    public void draw(DrawContext graphics, int x, int y, int width, int height, int u, int v, int regionWidth, int regionHeight, DrawMode drawMode) {
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        graphics.drawTexture(drawMode.get(this), x, y, width, height, u, v, regionWidth, regionHeight, 256, 256);
    }

    public void draw(DrawContext graphics, int x, int y, DrawMode drawMode) {
        draw(graphics, x, y, this.width, this.height, this.u, this.v, this.width, this.height, drawMode);
    }

    public void draw(DrawContext graphics, int x, int y) {
        draw(graphics, x, y, DrawMode.LIGHT);
    }

    public Text text() {
        return Text.translatable("slimefun_essentials.recipes.label." + this.id);
    }

    public enum DrawMode {
        LIGHT, DARK, BOOK;
        public Identifier get(SlimefunLabel slimefunLabel) {
            return switch (this) {
                case LIGHT -> slimefunLabel.light();
                case DARK -> slimefunLabel.dark();
                case BOOK -> slimefunLabel.book();
            };
        }
    }
}
