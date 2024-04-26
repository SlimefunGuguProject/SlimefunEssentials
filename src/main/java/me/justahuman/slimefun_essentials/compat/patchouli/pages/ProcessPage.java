package me.justahuman.slimefun_essentials.compat.patchouli.pages;

import me.justahuman.slimefun_essentials.api.OffsetBuilder;
import net.minecraft.client.gui.DrawContext;

public class ProcessPage extends SlimefunPage {
    @Override
    public void render(DrawContext graphics, OffsetBuilder offsets, int mouseX, int mouseY, float pTicks) {
        addLabels(graphics, offsets, this.recipe);
        addEnergyWithCheck(graphics, offsets, this.recipe);
        addInputsOrCatalyst(graphics, offsets, this.recipe);
    }

    @Override
    public Type getType() {
        return Type.PROCESS;
    }
}
