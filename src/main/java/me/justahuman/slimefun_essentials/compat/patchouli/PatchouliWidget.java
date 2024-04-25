package me.justahuman.slimefun_essentials.compat.patchouli;

import me.justahuman.slimefun_essentials.api.OffsetBuilder;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import vazkii.patchouli.client.base.ClientTicker;
import vazkii.patchouli.client.book.gui.GuiBookEntry;
import vazkii.patchouli.client.book.page.PageEntity;

import java.util.List;

@FunctionalInterface
public interface PatchouliWidget {
    PatchouliWidget EMPTY = (o1, o2, o3, o4, o5, o6) -> {};

    void render(GuiBookEntry gui, DrawContext graphics, OffsetBuilder offsets, int mouseX, int mouseY, float pTicks);

    static PatchouliWidget wrap(List<PatchouliWidget> widgets) {
        return (gui, graphics, offsets, mouseX, mouseY, pTicks) -> {
            // Picks widgets on an interval of 10 ticks or half a second
            int index = (int) (pTicks / (widgets.size() * 10)) % widgets.size();
            widgets.get(index).render(gui, graphics, offsets, mouseX, mouseY, pTicks);
        };
    }

    static PatchouliWidget wrap(ItemStack itemStack) {
        return (gui, graphics, offsets, mouseX, mouseY, pTicks) -> gui.renderItemStack(graphics, offsets.getX(), offsets.getY(), mouseX, mouseY, itemStack);
    }

    static PatchouliWidget wrap(Entity entity, int amount) {
        float width = entity.getWidth();
        float height = entity.getHeight();
        float entitySize = Math.max(1F, Math.max(width, height));
        float renderScale = 100F / entitySize * 0.8F;
        float offset = Math.max(height, entitySize) * 0.5F;

        return (gui, graphics, offsets, mouseX, mouseY, pTicks) -> {
            // TODO: render xAmount, render slot
            PageEntity.renderEntity(graphics, entity, offsets.getX(), offsets.getY(), ClientTicker.total, renderScale, offset);
        };
    }
}
