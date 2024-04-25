package me.justahuman.slimefun_essentials.compat.patchouli.pages;

import me.justahuman.slimefun_essentials.api.ManualRecipeRenderer;
import me.justahuman.slimefun_essentials.api.OffsetBuilder;
import me.justahuman.slimefun_essentials.client.DrawMode;
import me.justahuman.slimefun_essentials.client.SlimefunRecipe;
import me.justahuman.slimefun_essentials.client.SlimefunRecipeCategory;
import me.justahuman.slimefun_essentials.compat.patchouli.PatchouliIntegration;
import me.justahuman.slimefun_essentials.compat.patchouli.PatchouliWidget;
import me.justahuman.slimefun_essentials.utils.TextureUtils;
import me.justahuman.slimefun_essentials.utils.Utils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import vazkii.patchouli.client.book.BookContentsBuilder;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.BookPage;
import vazkii.patchouli.client.book.gui.GuiBook;
import java.util.ArrayList;
import java.util.List;

public abstract class SlimefunPage extends BookPage implements ManualRecipeRenderer {
    transient SlimefunRecipeCategory recipeCategory;
    transient ItemStack itemStack;
    transient String name;
    transient SlimefunRecipe recipe;
    transient List<PatchouliWidget> inputWidgets = new ArrayList<>();
    transient int mouseX;
    transient int mouseY;
    transient float pTicks;

    @Override
    public void build(World level, BookEntry entry, BookContentsBuilder builder, int pageNum) {
        String id = this.sourceObject.get("id").getAsString();
        int recipeIndex = this.sourceObject.get("recipe_index").getAsInt();

        this.recipeCategory = SlimefunRecipeCategory.getAllCategories().get(id);
        this.itemStack = this.recipeCategory.getItemFromId();
        this.name = this.itemStack.getName().getString();
        this.recipe = this.recipeCategory.recipesFor().get(recipeIndex);

        if (this.recipe.hasInputs()) {
            this.inputWidgets.addAll(this.recipe.inputs().stream().map(PatchouliIntegration.INTERPRETER::fromRecipeComponent).toList());
        }
    }

    @Override
    @MustBeInvokedByOverriders
    public void render(DrawContext graphics, int mouseX, int mouseY, float pTicks) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.pTicks = pTicks;

        this.parent.drawCenteredStringNoShadow(graphics, this.name, GuiBook.PAGE_WIDTH / 2, 0, this.book.headerColor);
        TextureUtils.SPOTLIGHT.draw(graphics, GuiBook.PAGE_WIDTH / 2 - TextureUtils.SPOTLIGHT.width() / 2, 10, DrawMode.BOOK);
        this.parent.renderItemStack(graphics, 50, 15, mouseX, mouseY, this.itemStack);
    }

    @Override
    public DrawMode getDrawMode() {
        return DrawMode.BOOK;
    }

    @Override
    public void drawEnergyFill(DrawContext graphics, int x, int y, boolean negative) {}

    @Override
    public void drawArrowFill(DrawContext graphics, int x, int y, int sfTicks, boolean backwards) {}

    @Override
    public void addInputs(DrawContext graphics, OffsetBuilder offsets, SlimefunRecipe recipe) {
        Utils.warn("WHAT THE FUCK IS HAPPENING (inputs again)");
        for (int i = 0; i < recipe.inputs().size(); i++) {
            Utils.warn("WHAT THE FUCK IS HAPPENING (intput " + i + ")");
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
}
