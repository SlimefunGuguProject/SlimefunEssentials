package me.justahuman.slimefun_essentials.compat.patchouli.pages;

import me.justahuman.slimefun_essentials.api.OffsetBuilder;
import me.justahuman.slimefun_essentials.client.DrawMode;
import me.justahuman.slimefun_essentials.compat.patchouli.PatchouliWidget;
import me.justahuman.slimefun_essentials.utils.TextureUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.world.World;
import vazkii.patchouli.client.book.BookContentsBuilder;
import vazkii.patchouli.client.book.BookEntry;

public class ReactorPage extends SlimefunPage {
    @Override
    public void build(World level, BookEntry entry, BookContentsBuilder builder, int pageNum) {
        super.build(level, entry, builder, pageNum);
        while (this.inputWidgets.size() < 4) {
            this.inputWidgets.add(PatchouliWidget.EMPTY);
        }
    }

    @Override
    public void render(DrawContext graphics, OffsetBuilder offsets, int mouseX, int mouseY, float pTicks) {
        TextureUtils.SLOT.draw(graphics, offsets.getX(), offsets.getY(), DrawMode.BOOK);
        this.inputWidgets.get(0).render(this.parent, graphics, offsets.getX() + 1, offsets.getY() + 1, mouseX, mouseY, pTicks);
        offsets.y().addSlot(false);
        TextureUtils.SLOT.draw(graphics, offsets.getX(), offsets.getY(), DrawMode.BOOK);
        offsets.y().addSlot(false);
        TextureUtils.SLOT.draw(graphics, offsets.getX(), offsets.getY(), DrawMode.BOOK);
        offsets.x().addSlot();

        if (this.recipe.hasEnergy()) {
            addEnergy(graphics, offsets.getX(), offsets.getY() + TextureUtils.PADDING, this.recipe.energy() < 0);
            offsets.x().addEnergy();
        }

        offsets.y().subtract(TextureUtils.SLOT.size(DrawMode.BOOK) * 2);
        TextureUtils.SLOT.draw(graphics, offsets.getX(), offsets.getY(), DrawMode.BOOK);
        this.inputWidgets.get(1).render(this.parent, graphics, offsets.getX() + 1, offsets.getY() + 1, mouseX, mouseY, pTicks);
        offsets.y().addSlot(false);
        TextureUtils.SLOT.draw(graphics, offsets.getX(), offsets.getY(), DrawMode.BOOK);
        this.inputWidgets.get(2).render(this.parent, graphics, offsets.getX() + 1, offsets.getY() + 1, mouseX, mouseY, pTicks);
        offsets.y().addSlot(false);
        TextureUtils.SLOT.draw(graphics, offsets.getX(), offsets.getY(), DrawMode.BOOK);
        this.inputWidgets.get(3).render(this.parent, graphics, offsets.getX() + 1, offsets.getY() + 1, mouseX, mouseY, pTicks);
    }

    @Override
    public Type getType() {
        return Type.REACTOR;
    }
}
