package me.justahuman.slimefun_essentials.compat.patchouli.pages;

import me.justahuman.slimefun_essentials.api.ManualRecipeRenderer;
import me.justahuman.slimefun_essentials.api.OffsetBuilder;
import me.justahuman.slimefun_essentials.client.SlimefunLabel;
import me.justahuman.slimefun_essentials.client.SlimefunRecipe;
import me.justahuman.slimefun_essentials.client.SlimefunRecipeCategory;
import me.justahuman.slimefun_essentials.compat.patchouli.PatchouliIntegration;
import me.justahuman.slimefun_essentials.compat.patchouli.PatchouliWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.world.World;
import vazkii.patchouli.client.book.BookContentsBuilder;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.BookPage;

import java.util.ArrayList;
import java.util.List;

public class ProcessPage extends BookPage implements ManualRecipeRenderer {
    transient SlimefunRecipeCategory recipeCategory;
    transient SlimefunRecipe recipe;
    transient List<PatchouliWidget> inputWidgets = new ArrayList<>();
    transient List<PatchouliWidget> outputWidgets = new ArrayList<>();
    transient int mouseX;
    transient int mouseY;
    transient float pTicks;

    @Override
    public void build(World level, BookEntry entry, BookContentsBuilder builder, int pageNum) {
        String id = this.sourceObject.get("id").getAsString();
        int recipeIndex = this.sourceObject.get("recipe_index").getAsInt();

        this.recipeCategory = SlimefunRecipeCategory.getAllCategories().get(id);
        this.recipe = this.recipeCategory.recipesFor().get(recipeIndex);
        this.inputWidgets.addAll(this.recipe.inputs().stream().map(PatchouliIntegration.INTERPRETER::fromRecipeComponent).toList());
        this.outputWidgets.addAll(this.recipe.outputs().stream().map(PatchouliIntegration.INTERPRETER::fromRecipeComponent).toList());
    }

    @Override
    public void render(DrawContext graphics, int mouseX, int mouseY, float pTicks) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.pTicks = pTicks;

        final OffsetBuilder offsets = new OffsetBuilder(this, recipe, 0, 0);
        addLabels(graphics, offsets, recipe);
        addEnergyWithCheck(graphics, offsets, recipe);
        addInputsOrCatalyst(graphics, offsets, recipe);
        addArrow(graphics, offsets, recipe);
        addOutputsOrEnergy(graphics, offsets, recipe);
    }

    @Override
    public Type getType() {
        return Type.PROCESS;
    }

    @Override
    public SlimefunLabel.DrawMode getDrawMode() {
        return SlimefunLabel.DrawMode.BOOK;
    }

    @Override
    public void drawEnergyFill(DrawContext graphics, int x, int y, boolean negative) {}

    @Override
    public void drawArrowFill(DrawContext graphics, int x, int y, int sfTicks, boolean backwards) {}

    @Override
    public void addInputs(DrawContext graphics, OffsetBuilder offsets, SlimefunRecipe recipe) {
        for (int i = 0; i < recipe.inputs().size(); i++) {
            addSlot(graphics, offsets.getX(), offsets.output(), true);
            this.inputWidgets.get(i).render(this.parent, graphics, offsets.getX() + 1, offsets.slot() + 1, this.mouseX, this.mouseY, this.pTicks);
            offsets.x().addSlot();
        }
    }

    @Override
    public void addCatalyst(DrawContext graphics, OffsetBuilder offsets, SlimefunRecipe recipe) {
        addSlot(graphics, offsets.getX(), offsets.slot(), false);
        PatchouliWidget.wrap(this.recipeCategory.getItemFromId()).render(this.parent, graphics, offsets.getX() + 1, offsets.slot() + 1, this.mouseX, this.mouseY, this.pTicks);
        offsets.x().addSlot();
    }

    @Override
    public void addOutputs(DrawContext graphics, OffsetBuilder offsets, SlimefunRecipe recipe) {
        for (int i = 0; i < recipe.outputs().size(); i++) {
            addSlot(graphics, offsets.getX(), offsets.output(), true);
            this.outputWidgets.get(i).render(this.parent, graphics, offsets.getX() + 5, offsets.slot() + 5, this.mouseX, this.mouseY, this.pTicks);
            offsets.x().addOutput();
        }
    }
}
