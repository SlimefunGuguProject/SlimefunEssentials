package me.justahuman.slimefun_essentials.compat.rei.categorys;

import me.justahuman.slimefun_essentials.client.SlimefunRecipeCategory;
import me.justahuman.slimefun_essentials.compat.rei.displays.AncientAltarDisplay;
import net.minecraft.item.ItemStack;

public class AncientAltarCategory extends SlimefunReiCategory<AncientAltarDisplay> {
    public AncientAltarCategory(SlimefunRecipeCategory slimefunRecipeCategory, ItemStack icon) {
        super(Type.ANCIENT_ALTAR, slimefunRecipeCategory, icon);
    }
}
