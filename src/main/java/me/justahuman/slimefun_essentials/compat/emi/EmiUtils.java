package me.justahuman.slimefun_essentials.compat.emi;

import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.TextureWidget;
import me.justahuman.slimefun_essentials.client.SlimefunLabel;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.text.Text;

import java.util.List;

public class EmiUtils {
    public static EmiTexture wrap(SlimefunLabel slimefunLabel) {
        return new EmiTexture(slimefunLabel.identifier(), slimefunLabel.u(), slimefunLabel.v(), slimefunLabel.width(), slimefunLabel.height());
    }

    public static TextureWidget wrap(SlimefunLabel slimefunLabel, int x, int y, boolean tooltip) {
        return new TextureWidget(slimefunLabel.identifier(), x, y,
                slimefunLabel.width(), slimefunLabel.height(), slimefunLabel.u(), slimefunLabel.v(),
                slimefunLabel.width(), slimefunLabel.height(), 256, 256)
                .tooltip((mx, my) -> List.of(TooltipComponent.of(Text.translatable("slimefun_essentials.recipes.label." + slimefunLabel.id()).asOrderedText())));
    }

    public static void fillInputs(List<EmiIngredient> list, int size) {
        if (list.size() >= size) {
            return;
        }

        for (int i = list.size(); i < size; i++) {
            list.add(EmiStack.EMPTY);
        }
    }

    public static void fillOutputs(List<EmiStack> list, int size) {
        if (list.size() >= size) {
            return;
        }

        for (int i = list.size(); i < size; i++) {
            list.add(EmiStack.EMPTY);
        }
    }
}
