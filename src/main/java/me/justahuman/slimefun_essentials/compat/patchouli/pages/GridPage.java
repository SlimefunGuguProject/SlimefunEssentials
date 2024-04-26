package me.justahuman.slimefun_essentials.compat.patchouli.pages;

import me.justahuman.slimefun_essentials.api.OffsetBuilder;
import me.justahuman.slimefun_essentials.client.DrawMode;
import me.justahuman.slimefun_essentials.compat.patchouli.PatchouliWidget;
import me.justahuman.slimefun_essentials.utils.TextureUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import vazkii.patchouli.client.book.BookContentsBuilder;
import vazkii.patchouli.client.book.BookEntry;

public class GridPage extends SlimefunPage {
    transient int side;

    @Override
    public void build(World level, BookEntry entry, BookContentsBuilder builder, int pageNum) {
        super.build(level, entry, builder, pageNum);
        this.side = this.sourceObject.get("side").getAsInt();
        while (this.inputWidgets.size() < this.side * this.side) {
            this.inputWidgets.add(PatchouliWidget.EMPTY);
        }
    }

    @Override
    public void render(DrawContext graphics, OffsetBuilder offsets, int mouseX, int mouseY, float pTicks) {
        addEnergyWithCheck(graphics, offsets, this.recipe);

        int i = 0;
        for (int y = 1; y <= this.side; y++) {
            for (int x = 1; x <= this.side; x++) {
                TextureUtils.SLOT.draw(graphics, offsets.getX(), offsets.getY(), DrawMode.BOOK);
                this.inputWidgets.get(i).render(this.parent, graphics, offsets.getX() + 1, offsets.getY() + 1, mouseX, mouseY, pTicks);
                offsets.x().addSlot(false);
                i++;
            }
            offsets.x().subtract(TextureUtils.SLOT.size(DrawMode.DARK) * this.side);
            offsets.y().addSlot(false);
        }
    }

    @Override
    public Type getType() {
        return Type.grid(this.side);
    }
}
