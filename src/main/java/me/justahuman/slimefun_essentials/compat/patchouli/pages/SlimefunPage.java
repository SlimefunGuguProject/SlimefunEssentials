package me.justahuman.slimefun_essentials.compat.patchouli.pages;

import me.justahuman.slimefun_essentials.api.ManualRecipeRenderer;
import me.justahuman.slimefun_essentials.api.OffsetBuilder;
import me.justahuman.slimefun_essentials.client.DrawMode;
import me.justahuman.slimefun_essentials.client.SlimefunRecipe;
import me.justahuman.slimefun_essentials.client.SlimefunRecipeCategory;
import me.justahuman.slimefun_essentials.compat.patchouli.PatchouliIntegration;
import me.justahuman.slimefun_essentials.compat.patchouli.PatchouliWidget;
import me.justahuman.slimefun_essentials.utils.TextureUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import vazkii.patchouli.client.book.BookContentsBuilder;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.BookPage;
import vazkii.patchouli.client.book.gui.BookTextRenderer;
import vazkii.patchouli.client.book.gui.GuiBook;

import java.util.ArrayList;
import java.util.List;

public abstract class SlimefunPage extends BookPage implements ManualRecipeRenderer {
    transient SlimefunRecipeCategory recipeCategory;
    transient ItemStack itemStack;
    transient SlimefunRecipe recipe;
    transient String name;
    transient List<PatchouliWidget> inputWidgets = new ArrayList<>();
    transient BookTextRenderer textRenderer = null;
    transient int mouseX;
    transient int mouseY;
    transient float pTicks;

    @Override
    public void build(World level, BookEntry entry, BookContentsBuilder builder, int pageNum) {
        String id = this.sourceObject.get("id").getAsString();
        this.recipeCategory = SlimefunRecipeCategory.getAllCategories().get(id);
        this.itemStack = this.recipeCategory.itemStack();
        this.recipe = this.recipeCategory.recipe();
        this.name = this.itemStack.getName().getString();

        if (this.recipe.hasInputs()) {
            this.inputWidgets.addAll(this.recipe.inputs().stream().map(PatchouliIntegration.INTERPRETER::fromRecipeComponent).toList());
        }
    }

    @Override
    public final void render(DrawContext graphics, int mouseX, int mouseY, float pTicks) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.pTicks = pTicks;

        TextureUtils.SPOTLIGHT.draw(graphics, GuiBook.PAGE_WIDTH / 2 - TextureUtils.SPOTLIGHT.width() / 2, 0, DrawMode.BOOK);
        this.parent.renderItemStack(graphics, 50, 5, mouseX, mouseY, this.itemStack);
        this.parent.drawCenteredStringNoShadow(graphics, this.name, GuiBook.PAGE_WIDTH / 2, 26, this.book.headerColor);
        GuiBook.drawSeparator(graphics, this.book, 0, 36);

        final OffsetBuilder offsets = new OffsetBuilder(this, this.recipe, calculateXOffset(this.recipe), calculateYOffset(this.recipe, 0), pageNum == 0 ? 65 : 0);
        render(graphics, offsets, mouseX, mouseY, pTicks);
        offsets.y().add(getDisplayHeight(this.recipe) / 2);

        if (this.textRenderer == null) {
            this.textRenderer = new BookTextRenderer(parent, getText(), 0, offsets.getY());
        }
        this.textRenderer.render(graphics, mouseX, mouseY, pTicks);
    }

    public abstract void render(DrawContext graphics, OffsetBuilder offsets, int mouseX, int mouseY, float pTicks);

    public Text getText() {
        final MutableText text = Text.literal("Crafted in the [")
                .append(this.recipe.parent().itemStack().getName().getString()).formatted(Formatting.AQUA)
                .append("]").formatted(Formatting.RESET);

        if (this.recipe.hasTime()) {
            text.append("\n");
            text.append(Text.translatable("slimefun_essentials.recipes.detail.time", this.recipe.sfTicks() / 2));
        }

        if (this.recipe.hasEnergy()) {
            text.append("\n");
            text.append(Text.translatable("slimefun_essentials.recipes.detail.energy." + (this.recipe.energy() > 0 ? "generate" : "use"), this.recipe.totalEnergy()));
        }

        return text;
    }

    @Override
    public DrawMode getDrawMode() {
        return DrawMode.BOOK;
    }

    @Override
    public int getDisplayWidth(SlimefunRecipe slimefunRecipe) {
        return TextureUtils.PAGE_WIDTH;
    }

    @Override
    public void drawEnergyFill(DrawContext graphics, int x, int y, boolean negative) {}

    @Override
    public void drawArrowFill(DrawContext graphics, int x, int y, int sfTicks, boolean backwards) {}

    @Override
    public void addInputs(DrawContext graphics, OffsetBuilder offsets, SlimefunRecipe recipe) {
        for (int i = 0; i < recipe.inputs().size(); i++) {
            addSlot(graphics, offsets.getX(), offsets.largeSlot(), false);
            this.inputWidgets.get(i).render(this.parent, graphics, offsets.getX() + 1, offsets.slot() + 1, this.mouseX, this.mouseY, this.pTicks);
            offsets.x().addSlot();
        }
    }

    @Override
    public void addCatalyst(DrawContext graphics, OffsetBuilder offsets, SlimefunRecipe recipe) {
        addSlot(graphics, offsets.getX(), offsets.slot(), false);
        PatchouliWidget.wrap(this.recipeCategory.itemStack()).render(this.parent, graphics, offsets.getX() + 1, offsets.slot() + 1, this.mouseX, this.mouseY, this.pTicks);
        offsets.x().addSlot();
    }
}
