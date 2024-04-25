package me.justahuman.slimefun_essentials.api;

import me.justahuman.slimefun_essentials.client.DrawMode;
import me.justahuman.slimefun_essentials.client.SlimefunRecipe;
import me.justahuman.slimefun_essentials.utils.TextureUtils;

public class OffsetBuilder {
    protected final int labelOffset;
    protected final int energyOffset;
    protected final int slotOffset;
    protected final int arrowOffset;
    protected final int outputOffset;
    protected final int minY;
    protected DrawMode drawMode;
    protected Offset xOffset;
    protected Offset yOffset;

    public OffsetBuilder(RecipeRenderer recipeRenderer, SlimefunRecipe slimefunRecipe) {
        this(recipeRenderer, slimefunRecipe, recipeRenderer.calculateXOffset(slimefunRecipe), recipeRenderer.calculateYOffset(slimefunRecipe, 0));
    }

    public OffsetBuilder(RecipeRenderer recipeRenderer, SlimefunRecipe slimefunRecipe, int x) {
        this(recipeRenderer, slimefunRecipe, x, recipeRenderer.calculateYOffset(slimefunRecipe, 0));
    }

    public OffsetBuilder(RecipeRenderer recipeRenderer, SlimefunRecipe slimefunRecipe, int x, int y) {
        this(recipeRenderer, slimefunRecipe, x, y, 0);
    }

    public OffsetBuilder(RecipeRenderer recipeRenderer, SlimefunRecipe slimefunRecipe, int x, int y, int minY) {
        this.drawMode = recipeRenderer.getDrawMode();
        this.labelOffset = recipeRenderer.calculateYOffset(slimefunRecipe, 14) + minY;
        this.energyOffset = recipeRenderer.calculateYOffset(slimefunRecipe, TextureUtils.ENERGY.height(this.drawMode)) + minY;
        this.slotOffset = recipeRenderer.calculateYOffset(slimefunRecipe, TextureUtils.SLOT.size(this.drawMode)) + minY;
        this.arrowOffset = recipeRenderer.calculateYOffset(slimefunRecipe, TextureUtils.ARROW.height(this.drawMode)) + minY;
        this.outputOffset = recipeRenderer.calculateYOffset(slimefunRecipe, TextureUtils.OUTPUT.height(this.drawMode)) + minY;
        this.minY = minY;
        this.xOffset = new Offset(this.drawMode, x, false);
        this.yOffset = new Offset(this.drawMode, y, true);
    }

    public Offset x() {
        return xOffset;
    }

    public Offset setX(int x) {
        this.xOffset.set(x);
        return this.xOffset;
    }

    public int getX() {
        return this.xOffset.get();
    }

    public Offset y() {
        return yOffset;
    }

    public Offset setY(int y) {
        this.yOffset.set(y);
        return this.yOffset;
    }

    public int getY() {
        return this.yOffset.get();
    }

    public int minY() {
        return this.minY;
    }

    public int label() {
        return labelOffset;
    }

    public int energy() {
        return energyOffset;
    }

    public int slot() {
        return slotOffset;
    }

    public int arrow() {
        return arrowOffset;
    }

    public int output() {
        return outputOffset;
    }

    public static class Offset {
        DrawMode drawMode;
        int value;
        boolean y;

        public Offset(DrawMode drawMode, int value, boolean y) {
            this.drawMode = drawMode;
            this.value = value;
            this.y = y;
        }

        public int get() {
            return this.value;
        }

        public Offset set(int offset) {
            this.value = offset;
            return this;
        }

        public Offset add(int add) {
            this.value += add;
            return this;
        }

        public Offset subtract(int subtract) {
            this.value -= subtract;
            return this;
        }

        public Offset addLabel() {
            return addLabel(true);
        }

        public Offset addLabel(boolean padding) {
            this.value += 14 + (padding ? TextureUtils.PADDING : 0);
            return this;
        }

        public Offset addEnergy() {
            return addEnergy(true);
        }

        public Offset addEnergy(boolean padding) {
            this.value += TextureUtils.ENERGY.size(this.drawMode, this.y) + (padding ? TextureUtils.PADDING : 0);
            return this;
        }

        public Offset addSlot() {
            return addSlot(true);
        }

        public Offset addSlot(boolean padding) {
            this.value += TextureUtils.SLOT.size(this.drawMode, this.y) + (padding ? TextureUtils.PADDING : 0);
            return this;
        }

        public Offset addArrow() {
            return addArrow(true);
        }

        public Offset addArrow(boolean padding) {
            this.value += TextureUtils.ARROW.size(this.drawMode, this.y) + (padding ? TextureUtils.PADDING : 0);
            return this;
        }

        public Offset addOutput() {
            return addOutput(true);
        }

        public Offset addOutput(boolean padding) {
            this.value += TextureUtils.OUTPUT.size(this.drawMode) + (padding ? TextureUtils.PADDING : 0);
            return this;
        }

        public Offset addPadding() {
            this.value += TextureUtils.PADDING;
            return this;
        }
    }
}
