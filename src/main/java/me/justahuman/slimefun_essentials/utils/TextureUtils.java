package me.justahuman.slimefun_essentials.utils;

import me.justahuman.slimefun_essentials.client.DrawMode;
import me.justahuman.slimefun_essentials.client.SlimefunRecipeCategory;
import me.justahuman.slimefun_essentials.client.SlimefunLabel;
import me.justahuman.slimefun_essentials.client.SlimefunRecipe;
import net.minecraft.util.Identifier;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

public class TextureUtils {
    public static final int PAGE_WIDTH = 116;
    public static final int PAGE_HEIGHT = 156;
    public static final int PADDING = 4;
    public static final int REI_PADDING = 6;
    public static final Identifier WIDGETS = new Identifier(Utils.ID, "textures/gui/widgets.png");
    public static final Identifier WIDGETS_DARK = new Identifier(Utils.ID, "textures/gui/widgets_dark.png");
    public static final Identifier WIDGETS_BOOK = new Identifier(Utils.ID, "textures/gui/widgets_book.png");
    public static final SlimefunLabel ENERGY = SlimefunLabel.of("energy", 36, 0, 7, 9);
    public static final SlimefunLabel ENERGY_POSITIVE = SlimefunLabel.of("energy_positive", 43, 0, 7, 9);
    public static final SlimefunLabel ENERGY_NEGATIVE = SlimefunLabel.of("energy_negative", 50, 0, 7, 9);
    public static final SlimefunLabel SLOT = SlimefunLabel.of("slot", 0, 238, 18, 18);
    public static final SlimefunLabel OUTPUT = SlimefunLabel.of("output", 18, 230, 26, 26);
    public static final SlimefunLabel ARROW = SlimefunLabel.builder().id("arrow")
            .mode(DrawMode.LIGHT, 44, 222, 24, 17)
            .mode(DrawMode.BOOK, 44, 245, 14, 11).build();
    public static final SlimefunLabel FILLED_ARROW = SlimefunLabel.of("filled_arrow", 44, 239, 24, 17);
    public static final SlimefunLabel BACKWARDS_ARROW = SlimefunLabel.builder().id("backwards_arrow")
            .mode(DrawMode.LIGHT, 67, 222, 24, 17)
            .mode(DrawMode.BOOK, 58, 245, 14, 11).build();
    public static final SlimefunLabel FILLED_BACKWARDS_ARROW = SlimefunLabel.of("filled_backwards_arrow", 67, 239, 24, 17);
    public static final SlimefunLabel PEDESTAL = SlimefunLabel.of("pedestal", 0, 0, 18, 18);
    public static final SlimefunLabel ALTAR = SlimefunLabel.of("altar", 18, 0, 18, 18);
    public static final SlimefunLabel SPOTLIGHT = SlimefunLabel.of("spotlight", 72, 232, 64, 24);
    public static final Map<SlimefunRecipeCategory, Integer> CACHED_WIDTH = new HashMap<>();
    public static final Map<SlimefunRecipeCategory, Integer> CACHED_HEIGHT = new HashMap<>();
    public static final NumberFormat numberFormat = NumberFormat.getInstance();

    static {
        numberFormat.setGroupingUsed(true);
    }

    public static int getSideSafe(String type) {
        try {
            return Integer.parseInt(type.substring(type.length() - 1));
        } catch (NumberFormatException ignored) {
            return 3;
        }
    }

    public static int getGridWidth(DrawMode drawMode, SlimefunRecipeCategory slimefunRecipeCategory, int side) {
        return CACHED_WIDTH.computeIfAbsent(slimefunRecipeCategory, value -> {
            int width = 0;
            for (SlimefunRecipe slimefunRecipe : slimefunRecipeCategory.recipes()) {
                width = Math.max(width, getGridWidth(drawMode, slimefunRecipe, side));
            }
            return width;
        });
    }

    public static int getGridWidth(DrawMode drawMode, SlimefunRecipe slimefunRecipe, int side) {
        return (side * SLOT.size(drawMode) + TextureUtils.PADDING) + (slimefunRecipe.hasEnergy() ? ENERGY.width(drawMode) + PADDING : 0) + (ARROW.width(drawMode) + PADDING) + (slimefunRecipe.hasOutputs()? OUTPUT.size(drawMode) * slimefunRecipe.outputs().size() : 0);
    }

    public static int getGridHeight(DrawMode drawMode, int side) {
        return side * SLOT.size(drawMode);
    }

    public static int getProcessWidth(DrawMode drawMode, SlimefunRecipeCategory slimefunRecipeCategory) {
        return CACHED_WIDTH.computeIfAbsent(slimefunRecipeCategory, value -> {
            int width = 0;
            for (SlimefunRecipe slimefunRecipe : slimefunRecipeCategory.recipes()) {
                width = Math.max(width, getProcessWidth(drawMode, slimefunRecipe));
            }
            return width;
        });
    }
    
    public static int getProcessWidth(DrawMode drawMode, SlimefunRecipe slimefunRecipe) {
        return (slimefunRecipe.hasLabels() ? slimefunRecipe.labels().stream().mapToInt(label -> label.size(drawMode)).sum() : 0) + (slimefunRecipe.hasEnergy() ? ENERGY.width(drawMode) + PADDING : 0) + ((SLOT.size(drawMode) + PADDING) * (slimefunRecipe.hasInputs() ? slimefunRecipe.inputs().size() : 1)) + (ARROW.width(drawMode) + PADDING) + (slimefunRecipe.hasOutputs() ? OUTPUT.size(drawMode) * slimefunRecipe.outputs().size() + PADDING * (slimefunRecipe.outputs().size() - 1) : 0);
    }

    public static int getProcessHeight(DrawMode drawMode, SlimefunRecipeCategory slimefunRecipeCategory) {
        return CACHED_HEIGHT.computeIfAbsent(slimefunRecipeCategory, value -> {
            for (SlimefunRecipe slimefunRecipe : slimefunRecipeCategory.recipes()) {
                if (slimefunRecipe.hasOutputs()) {
                    return OUTPUT.size(drawMode);
                }
            }
            return SLOT.size(drawMode);
        });
    }

    public static int getProcessHeight(DrawMode drawMode, SlimefunRecipe slimefunRecipe) {
        return slimefunRecipe.hasOutputs() ? OUTPUT.size(drawMode) : SLOT.size(drawMode);
    }

    public static int getReactorWidth(DrawMode drawMode, SlimefunRecipeCategory slimefunRecipeCategory) {
        return CACHED_WIDTH.computeIfAbsent(slimefunRecipeCategory, value -> {
            int width = 0;
            for (SlimefunRecipe slimefunRecipe : slimefunRecipeCategory.recipes()) {
                width = Math.max(width, getReactorWidth(drawMode, slimefunRecipe));
            }
            return width;
        });
    }

    public static int getReactorWidth(DrawMode drawMode, SlimefunRecipe slimefunRecipe) {
        return (SLOT.size(drawMode) + ARROW.width(drawMode)) * 2 + PADDING * 4 + (slimefunRecipe.hasOutputs() ? OUTPUT.size(drawMode) : ENERGY.width(drawMode));
    }

    public static int getReactorHeight(DrawMode drawMode, SlimefunRecipeCategory slimefunRecipeCategory) {
        return CACHED_HEIGHT.computeIfAbsent(slimefunRecipeCategory, value -> {
            final int baseAmount = SLOT.size(drawMode) * 2;
            for (SlimefunRecipe slimefunRecipe : slimefunRecipeCategory.recipes()) {
                if (slimefunRecipe.hasOutputs()) {
                    return baseAmount + OUTPUT.size(drawMode);
                }
            }

            return baseAmount + SLOT.size(drawMode);
        });
    }

    public static int getReactorHeight(DrawMode drawMode, SlimefunRecipe slimefunRecipe) {
        return SLOT.size(drawMode) * 2 + (slimefunRecipe.hasOutputs() ? OUTPUT.size(drawMode) : SLOT.size(drawMode));
    }

    public static int getSmelteryWidth(DrawMode drawMode, SlimefunRecipeCategory slimefunRecipeCategory) {
        return CACHED_WIDTH.computeIfAbsent(slimefunRecipeCategory, value -> {
            int width = 0;
            for (SlimefunRecipe slimefunRecipe : slimefunRecipeCategory.recipes()) {
                width = Math.max(width, getSmelteryWidth(drawMode, slimefunRecipe));
            }
            return width;
        });
    }

    public static int getSmelteryWidth(DrawMode drawMode, SlimefunRecipe slimefunRecipe) {
        return (slimefunRecipe.hasEnergy() ? ENERGY.width(drawMode) + PADDING : 0) + (slimefunRecipe.hasInputs() ? SLOT.size(drawMode) * 2 + PADDING : SLOT.size(drawMode) + PADDING) + (ARROW.width(drawMode) + PADDING) + (slimefunRecipe.hasOutputs() ? OUTPUT.size(drawMode) * slimefunRecipe.outputs().size() + PADDING * (slimefunRecipe.outputs().size() - 1): 0);
    }
}
