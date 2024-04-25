package me.justahuman.slimefun_essentials.compat.patchouli.pages;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import me.justahuman.slimefun_essentials.api.ManualRecipeRenderer;
import me.justahuman.slimefun_essentials.api.OffsetBuilder;
import me.justahuman.slimefun_essentials.client.SlimefunLabel;
import me.justahuman.slimefun_essentials.client.SlimefunRecipe;
import me.justahuman.slimefun_essentials.client.SlimefunRecipeCategory;
import me.justahuman.slimefun_essentials.client.SlimefunRecipeComponent;
import me.justahuman.slimefun_essentials.compat.patchouli.PatchouliIntegration;
import me.justahuman.slimefun_essentials.compat.patchouli.PatchouliWidget;
import me.justahuman.slimefun_essentials.utils.TextureUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.world.World;
import vazkii.patchouli.client.book.BookContentsBuilder;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.BookPage;

import java.util.ArrayList;
import java.util.List;

public class ProcessPage extends BookPage implements ManualRecipeRenderer {
    transient SlimefunRecipe recipe;
    transient List<PatchouliWidget> inputWidgets = new ArrayList<>();
    transient List<PatchouliWidget> outputWidgets = new ArrayList<>();

    @Override
    public void build(World level, BookEntry entry, BookContentsBuilder builder, int pageNum) {
        String id = this.sourceObject.get("id").getAsString();
        Integer sfTicks = null;
        final JsonElement ticksElement = this.sourceObject.get("sfTicks");
        if (ticksElement != null) {
            sfTicks = ticksElement.getAsInt();
        }

        Integer energy = null;
        final JsonElement energyElement = this.sourceObject.get("energy");
        if (energyElement != null) {
            energy = energyElement.getAsInt();
        }

        final List<SlimefunLabel> labels = new ArrayList<>();
        final JsonElement labelsElement = this.sourceObject.get("labels");
        if (labelsElement instanceof JsonArray labelsArray) {
            for (JsonElement label : labelsArray) {
                if (label instanceof JsonPrimitive labelPrimitive && labelPrimitive.isString()) {
                    final SlimefunLabel slimefunLabel = SlimefunLabel.getSlimefunLabels().get(labelPrimitive.getAsString());
                    if (slimefunLabel != null) {
                        labels.add(slimefunLabel);
                    }
                }
            }
        }

        final List<SlimefunRecipeComponent> inputs = new ArrayList<>();
        final JsonElement inputsElement = this.sourceObject.get("inputs");
        if (inputsElement instanceof JsonArray inputsArray) {
            for (JsonElement component : inputsArray) {
                final SlimefunRecipeComponent recipeComponent = SlimefunRecipeComponent.deserialize(component);
                if (recipeComponent != null) {
                    inputs.add(recipeComponent);
                    this.inputWidgets.add(PatchouliIntegration.INTERPRETER.fromRecipeComponent(recipeComponent));
                }
            }
        }

        final List<SlimefunRecipeComponent> outputs = new ArrayList<>();
        final JsonElement outputsElement = this.sourceObject.get("outputs");
        if (outputsElement instanceof JsonArray outputsArray) {
            for (JsonElement component : outputsArray) {
                final SlimefunRecipeComponent recipeComponent = SlimefunRecipeComponent.deserialize(component);
                if (recipeComponent != null) {
                    outputs.add(recipeComponent);
                    this.outputWidgets.add(PatchouliIntegration.INTERPRETER.fromRecipeComponent(recipeComponent));
                }
            }
        }

        this.recipe = new SlimefunRecipe(
                SlimefunRecipeCategory.getRecipeCategories().get(id),
                sfTicks,
                energy,
                inputs,
                outputs,
                labels);
    }

    @Override
    public void render(DrawContext graphics, int mouseX, int mouseY, float pTicks) {
        final OffsetBuilder offsets = new OffsetBuilder(this, this.recipe, calculateXOffset(this.recipe.parent(), this.recipe));

        // Display Labels
        if (this.recipe.hasLabels()) {
            for (SlimefunLabel slimefunLabel : this.recipe.labels()) {
                slimefunLabel.draw(graphics, offsets.getX(), offsets.label());
                offsets.x().addLabel();
            }
        }

        // Display Energy
        addEnergyWithCheck(graphics, offsets, this.recipe);

        // Display Inputs, only the slot icon
        if (this.recipe.hasInputs()) {
            for (int i = 0; i < this.recipe.inputs().size(); i++) {
                this.inputWidgets.get(i).render(this.parent, graphics, offsets, mouseX, mouseY, pTicks);
                TextureUtils.SLOT.draw(graphics, offsets.getX(), offsets.slot());
                offsets.x().addSlot();
            }
        } else {
            PatchouliWidget.wrap(this.recipe.parent().getItemFromId()).render(this.parent, graphics, offsets, mouseX, mouseY, pTicks);
            TextureUtils.SLOT.draw(graphics, offsets.getX(), offsets.slot());
            offsets.x().addSlot();
        }

        // Display Arrow
        addArrow(graphics, offsets, this.recipe);

        // Display Outputs
        addOutputsOrEnergy(graphics, offsets, this.recipe);
    }

    @Override
    public Type getType() {
        return Type.PROCESS;
    }

    @Override
    public void drawEnergyFill(DrawContext graphics, int x, int y, boolean negative) {

    }

    @Override
    public void drawArrowFill(DrawContext graphics, int x, int y, int sfTicks, boolean backwards) {

    }

    @Override
    public void addOutputs(DrawContext graphics, OffsetBuilder offsets, SlimefunRecipe recipe) {
        for (int i = 0; i < recipe.outputs().size(); i++) {
            this.outputWidgets.get(i).render(this.parent, graphics, offsets, 0, 0, 0);
            TextureUtils.OUTPUT.draw(graphics, offsets.getX(), offsets.output());
            offsets.x().addOutput();
        }
    }
}
