package me.justahuman.slimefun_essentials.compat.patchouli.pages;

import me.justahuman.slimefun_essentials.api.OffsetBuilder;
import me.justahuman.slimefun_essentials.client.DrawMode;
import me.justahuman.slimefun_essentials.compat.patchouli.PatchouliWidget;
import me.justahuman.slimefun_essentials.utils.TextureUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.world.World;
import vazkii.patchouli.client.book.BookContentsBuilder;
import vazkii.patchouli.client.book.BookEntry;

public class AncientAltarPage extends SlimefunPage {
    @Override
    public void build(World level, BookEntry entry, BookContentsBuilder builder, int pageNum) {
        super.build(level, entry, builder, pageNum);
        while (this.inputWidgets.size() < 9) {
            this.inputWidgets.add(PatchouliWidget.EMPTY);
        }
    }

    @Override
    public void render(DrawContext graphics, int mouseX, int mouseY, float pTicks) {
        super.render(graphics, mouseX, mouseY, pTicks);
        final OffsetBuilder offsets = new OffsetBuilder(this, this.recipe);

        TextureUtils.PEDESTAL.draw(graphics, offsets.getX(), offsets.slot(), DrawMode.BOOK);
        this.inputWidgets.get(3).render(this.parent, graphics, offsets.getX() + 1, offsets.slot() + 1, mouseX, mouseY, pTicks);
        offsets.x().addSlot(false);
        TextureUtils.PEDESTAL.draw(graphics, offsets.getX(), offsets.slot() + TextureUtils.SLOT.size(DrawMode.BOOK), DrawMode.BOOK);
        this.inputWidgets.get(0).render(this.parent, graphics, offsets.getX() + 1, offsets.slot() + TextureUtils.SLOT.size(DrawMode.BOOK) + 1, mouseX, mouseY, pTicks);
        TextureUtils.PEDESTAL.draw(graphics, offsets.getX(), offsets.slot() - TextureUtils.SLOT.size(DrawMode.BOOK), DrawMode.BOOK);
        this.inputWidgets.get(6).render(this.parent, graphics, offsets.getX() + 1, offsets.slot() - TextureUtils.SLOT.size(DrawMode.BOOK) + 1, mouseX, mouseY, pTicks);
        offsets.x().addSlot(false);
        TextureUtils.PEDESTAL.draw(graphics, offsets.getX(), offsets.slot() + TextureUtils.SLOT.size(DrawMode.BOOK) * 2, DrawMode.BOOK);
        this.inputWidgets.get(1).render(this.parent, graphics, offsets.getX() + 1, offsets.slot() + TextureUtils.SLOT.size(DrawMode.BOOK) * 2 + 1, mouseX, mouseY, pTicks);
        TextureUtils.ALTAR.draw(graphics, offsets.getX(), offsets.slot(), DrawMode.BOOK);
        this.inputWidgets.get(4).render(this.parent, graphics, offsets.getX() + 1, offsets.slot() + 1, mouseX, mouseY, pTicks);
        TextureUtils.PEDESTAL.draw(graphics, offsets.getX(), offsets.slot() - TextureUtils.SLOT.size(DrawMode.BOOK) * 2, DrawMode.BOOK);
        this.inputWidgets.get(7).render(this.parent, graphics, offsets.getX() + 1, offsets.slot() - TextureUtils.SLOT.size(DrawMode.BOOK) * 2 + 1, mouseX, mouseY, pTicks);
        offsets.x().addSlot(false);
        TextureUtils.PEDESTAL.draw(graphics, offsets.getX(), offsets.slot() + TextureUtils.SLOT.size(DrawMode.BOOK), DrawMode.BOOK);
        this.inputWidgets.get(2).render(this.parent, graphics, offsets.getX() + 1, offsets.slot() + TextureUtils.SLOT.size(DrawMode.BOOK) + 1, mouseX, mouseY, pTicks);
        TextureUtils.PEDESTAL.draw(graphics, offsets.getX(), offsets.slot() - TextureUtils.SLOT.size(DrawMode.BOOK), DrawMode.BOOK);
        this.inputWidgets.get(8).render(this.parent, graphics, offsets.getX() + 1, offsets.slot() - TextureUtils.SLOT.size(DrawMode.BOOK) + 1, mouseX, mouseY, pTicks);
        offsets.x().addSlot(false);
        TextureUtils.PEDESTAL.draw(graphics, offsets.getX(), offsets.slot(), DrawMode.BOOK);
        this.inputWidgets.get(5).render(this.parent, graphics, offsets.getX() + 1, offsets.slot() + 1, mouseX, mouseY, pTicks);
    }

    @Override
    public Type getType() {
        return Type.ANCIENT_ALTAR;
    }
}
