package me.justahuman.slimefun_essentials.api;

import me.justahuman.slimefun_essentials.client.SlimefunLabel;
import me.justahuman.slimefun_essentials.client.SlimefunRecipe;
import me.justahuman.slimefun_essentials.utils.TextureUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public interface ManualRecipeRenderer extends RecipeRenderer {
    default void addEnergyWithCheck(DrawContext graphics, OffsetBuilder offsets, SlimefunRecipe recipe) {
        if (recipe.hasEnergy() && recipe.hasOutputs()) {
            addEnergy(graphics, offsets, recipe.energy() < 0);
        }
    }

    default void addEnergy(DrawContext graphics, OffsetBuilder offsets, boolean negative) {
        addEnergy(graphics, offsets.getX(), offsets.energy(), negative);
        offsets.x().addEnergy();
    }

    default void addEnergy(DrawContext graphics, int x, int y, boolean negative) {
        TextureUtils.ENERGY.draw(graphics, x, y);
        drawEnergyFill(graphics, x, y, negative);
    }
    
    void drawEnergyFill(DrawContext graphics, int x, int y, boolean negative);

    default void addArrow(DrawContext graphics, OffsetBuilder offsets, SlimefunRecipe recipe) {
        addArrow(graphics, recipe, offsets.getX(), offsets.arrow(),false);
        offsets.x().addArrow();
    }

    default void addArrow(DrawContext graphics, SlimefunRecipe recipe, int x, int y, boolean backwards) {
        if (recipe.hasTime()) {
            addFillingArrow(graphics, x, y, recipe.sfTicks(), backwards);
        } else {
            addArrow(graphics, x, y, backwards);
        }
    }

    default void addArrow(DrawContext graphics, int x, int y, boolean backwards) {
        (backwards ? TextureUtils.BACKWARDS_ARROW : TextureUtils.ARROW).draw(graphics, x, y);
    }

    default void addFillingArrow(DrawContext graphics, int x, int y, int sfTicks, boolean backwards) {
        addArrow(graphics, x, y, backwards);
        drawArrowFill(graphics, x, y, sfTicks, backwards);
    }
    
    void drawArrowFill(DrawContext graphics, int x, int y, int sfTicks, boolean backwards);

    default void addOutputsOrEnergy(DrawContext graphics, OffsetBuilder offsets, SlimefunRecipe recipe) {
        if (recipe.hasOutputs()) {
            addOutputs(graphics, offsets, recipe);
        } else {
            addEnergy(graphics, offsets, recipe.energy() < 0);
        }
    }

    default void addOutputs(DrawContext graphics, OffsetBuilder offsets, SlimefunRecipe recipe) {
        for (int i = 0; i < recipe.outputs().size(); i++) {
            TextureUtils.OUTPUT.draw(graphics, offsets.getX(), offsets.output());
            offsets.x().addOutput();
        }
    }

    default boolean tooltipActive(double mouseX, double mouseY, OffsetBuilder offsets, SlimefunLabel label) {
        return tooltipActive(mouseX, mouseY, offsets.getX(), offsets.getY(), label);
    }

    default boolean tooltipActive(double mouseX, double mouseY, int x, int y, SlimefunLabel label) {
        return mouseX >= x && mouseX <= x + label.width() && mouseY >= y && mouseY <= y + label.height();
    }

    default Text labelTooltip(SlimefunLabel label) {
        return Text.translatable("slimefun_essentials.recipes.label." + label.id());
    }

    default Text timeTooltip(SlimefunRecipe recipe) {
        return Text.translatable("slimefun_essentials.recipes.time", TextureUtils.numberFormat.format(recipe.sfTicks() / 2), TextureUtils.numberFormat.format(recipe.sfTicks() / 2 * 10L));
    }

    default Text energyTooltip(SlimefunRecipe recipe) {
        return Text.translatable("slimefun_essentials.recipes.energy." + (recipe.totalEnergy() >= 0 ? "generate" : "use"), TextureUtils.numberFormat.format(Math.abs(recipe.totalEnergy())));
    }
}
