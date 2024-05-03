package me.justahuman.slimefun_essentials.client;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.systems.RenderSystem;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import me.justahuman.slimefun_essentials.utils.JsonUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SlimefunLabel {
    private static final Map<String, SlimefunLabel> slimefunLabels = new LinkedHashMap<>();

    private final String id;
    private final Map<DrawMode, LabelSettings> settings;

    public SlimefunLabel(String id, Map<DrawMode, LabelSettings> settings) {
        this.id = id;
        this.settings = settings;
    }

    public static SlimefunLabel of(String id, int u, int v, int width, int height) {
        return new LabelBuilder().id(id).mode(DrawMode.LIGHT, u, v, width, height).build();
    }

    public String id() {
        return this.id;
    }

    public Identifier identifier() {
        return identifier(DrawMode.LIGHT);
    }

    public Identifier identifier(DrawMode drawMode) {
        return this.settings.get(drawMode).identifier;
    }

    public int u() {
        return u(DrawMode.LIGHT);
    }

    public int u(DrawMode drawMode) {
        return this.settings.get(drawMode).u;
    }

    public int v() {
        return v(DrawMode.LIGHT);
    }

    public int v(DrawMode drawMode) {
        return this.settings.get(drawMode).v;
    }

    public int size() {
        return width(DrawMode.LIGHT);
    }

    public int size(boolean y) {
        return y ? height() : width();
    }

    public int size(DrawMode drawMode) {
        return width(drawMode);
    }

    public int size(DrawMode drawMode, boolean y) {
        return y ? height(drawMode) : width(drawMode);
    }

    public int width() {
        return width(DrawMode.LIGHT);
    }

    public int width(DrawMode drawMode) {
        return this.settings.get(drawMode).width;
    }

    public int height() {
        return height(DrawMode.LIGHT);
    }

    public int height(DrawMode drawMode) {
        return this.settings.get(drawMode).height;
    }

    public static void deserialize(String id, JsonObject labelObject) {
        final LabelBuilder builder = builder().id(id);
        for (String mode : labelObject.keySet()) {
            if (labelObject.get(mode) instanceof JsonObject settings) {
                builder.mode(DrawMode.valueOf(mode),
                        JsonUtils.getInt(settings, "u", 0),
                        JsonUtils.getInt(settings, "v", 0),
                        JsonUtils.getInt(settings, "width", 13),
                        JsonUtils.getInt(settings, "height", 13));
            }
        }
        slimefunLabels.put(id, builder.build());
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

    public void draw(DrawContext graphics, Identifier identifier, int x, int y, int width, int height, int u, int v, int regionWidth, int regionHeight) {
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        graphics.drawTexture(identifier, x, y, width, height, u, v, width, height, 256, 256);
    }

    public void draw(DrawContext graphics, int x, int y, DrawMode drawMode) {
        final LabelSettings options = this.settings.get(drawMode);
        draw(graphics, options.identifier, x, y, options.width, options.height, options.u, options.v, options.width, options.height);
    }


    public void draw(DrawContext graphics, int x, int y) {
        draw(graphics, x, y, DrawMode.LIGHT);
    }

    public Text text() {
        return Text.translatable("slimefun_essentials.recipes.label." + this.id);
    }

    public static LabelBuilder builder() {
        return new LabelBuilder();
    }

    public static class LabelBuilder {
        private LabelBuilder() {}

        private String id = "";
        private final Map<DrawMode, LabelSettings> settings = new EnumMap<>(DrawMode.class);

        public LabelBuilder id(String id) {
            this.id = id;
            return this;
        }

        public LabelBuilder mode(DrawMode mode, int u, int v, int width, int height) {
            this.settings.put(mode, new LabelSettings(mode.defaultIdentifier(), u, v, width, height));
            return this;
        }

        public SlimefunLabel build() {
            if (id.isBlank()) {
                throw new IllegalArgumentException("Id must be set!");
            }

            if (!settings.containsKey(DrawMode.LIGHT)) {
                throw new IllegalArgumentException("Options must have light mode!");
            }

            final LabelSettings lightMode = settings.get(DrawMode.LIGHT);
            for (DrawMode otherMode : DrawMode.values()) {
                if (!settings.containsKey(otherMode)) {
                    mode(otherMode, lightMode.u, lightMode.v, lightMode.width, lightMode.height);
                }
            }

            return new SlimefunLabel(this.id, this.settings);
        }
    }

    @AllArgsConstructor
    public static class LabelSettings {
        private final Identifier identifier;
        private final int u;
        private final int v;
        private final int width;
        private final int height;
    }

}
