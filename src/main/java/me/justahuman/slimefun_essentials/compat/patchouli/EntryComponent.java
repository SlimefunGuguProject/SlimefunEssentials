package me.justahuman.slimefun_essentials.compat.patchouli;

import net.minecraft.client.gui.DrawContext;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.BookIcon;

import java.util.function.UnaryOperator;

public class EntryComponent implements ICustomComponent {
    private IVariable entry;
    private BookEntry bookEntry;
    private int x;
    private int y;

    @Override
    public void build(int componentX, int componentY, int pageNum) {
        this.x = componentX;
        this.y = componentY;
    }

    public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
        this.bookEntry = CustomGuide.getEntries().get(lookup.apply(entry).asString());
    }

    @Override
    public void render(DrawContext graphics, IComponentRenderContext context, float pTicks, int mouseX, int mouseY) {
        if (this.bookEntry != null && this.bookEntry.getIcon() instanceof BookIcon.StackIcon stackIcon) {
            context.renderItemStack(graphics, this.x, this.y, mouseX, mouseY, stackIcon.stack());
        }
    }

    @Override
    public boolean mouseClicked(IComponentRenderContext context, double mouseX, double mouseY, int mouseButton) {
        if (context.isAreaHovered((int) mouseX, (int) mouseY, this.x, this.y, 16, 16)) {
            context.navigateToEntry(bookEntry.getId(), 0, true);
            return true;
        }
        return false;
    }
}
