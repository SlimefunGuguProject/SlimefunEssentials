package me.justahuman.slimefun_essentials.compat.emi.recipes;

import dev.emi.emi.api.widget.WidgetHolder;
import me.justahuman.slimefun_essentials.api.OffsetBuilder;
import me.justahuman.slimefun_essentials.client.SlimefunRecipeCategory;
import me.justahuman.slimefun_essentials.client.SlimefunRecipe;
import me.justahuman.slimefun_essentials.compat.emi.EmiUtils;
import me.justahuman.slimefun_essentials.compat.emi.SlimefunEmiCategory;
import me.justahuman.slimefun_essentials.utils.TextureUtils;

public class AncientAltarRecipe extends ProcessRecipe {
    public AncientAltarRecipe(SlimefunRecipeCategory slimefunRecipeCategory, SlimefunRecipe slimefunRecipe, SlimefunEmiCategory emiRecipeCategory) {
        super(Type.ANCIENT_ALTAR, slimefunRecipeCategory, slimefunRecipe, emiRecipeCategory);

        EmiUtils.fillInputs(this.inputs, 9);
        EmiUtils.fillOutputs(this.outputs, 1);
    }
    
    @Override
    public void addWidgets(WidgetHolder widgets) {
        final OffsetBuilder offsets = new OffsetBuilder(this, this.slimefunRecipe);

        // Special Inputs
        widgets.addSlot(this.inputs.get(3), offsets.getX(), offsets.slot()).backgroundTexture(TextureUtils.WIDGETS, 0, 0);
        offsets.x().addSlot(false);
        widgets.addSlot(this.inputs.get(0), offsets.getX(), offsets.slot() + TextureUtils.SLOT_SIZE).backgroundTexture(TextureUtils.WIDGETS, 0, 0);
        widgets.addSlot(this.inputs.get(6), offsets.getX(), offsets.slot() - TextureUtils.SLOT_SIZE).backgroundTexture(TextureUtils.WIDGETS, 0, 0);
        offsets.x().addSlot(false);
        widgets.addSlot(this.inputs.get(1), offsets.getX(), offsets.slot() + TextureUtils.SLOT_SIZE * 2).backgroundTexture(TextureUtils.WIDGETS, 0, 0);
        widgets.addSlot(this.inputs.get(4), offsets.getX(), offsets.slot()).backgroundTexture(TextureUtils.WIDGETS, 18, 0);
        widgets.addSlot(this.inputs.get(7), offsets.getX(), offsets.slot() - TextureUtils.SLOT_SIZE * 2).backgroundTexture(TextureUtils.WIDGETS, 0, 0);
        offsets.x().addSlot(false);
        widgets.addSlot(this.inputs.get(2), offsets.getX(), offsets.slot() + TextureUtils.SLOT_SIZE).backgroundTexture(TextureUtils.WIDGETS, 0, 0);
        widgets.addSlot(this.inputs.get(8), offsets.getX(), offsets.slot() - TextureUtils.SLOT_SIZE).backgroundTexture(TextureUtils.WIDGETS, 0, 0);
        offsets.x().addSlot(false);
        widgets.addSlot(this.inputs.get(5), offsets.getX(), offsets.slot()).backgroundTexture(TextureUtils.WIDGETS, 0, 0);
        offsets.x().addSlot();

        // Add Arrow
        addArrowWithCheck(widgets, offsets);

        // Output
        widgets.addSlot(this.outputs.get(0), offsets.getX(), offsets.slot());
    }
}
