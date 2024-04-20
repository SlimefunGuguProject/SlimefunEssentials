package me.justahuman.slimefun_essentials.compat.rei.categorys;

import me.justahuman.slimefun_essentials.client.SlimefunRecipeCategory;
import me.justahuman.slimefun_essentials.compat.rei.displays.SmelteryDisplay;
import net.minecraft.item.ItemStack;

public class SmelteryCategory extends SlimefunReiCategory<SmelteryDisplay> {
    public SmelteryCategory(SlimefunRecipeCategory slimefunRecipeCategory, ItemStack icon) {
        super(Type.SMELTERY, slimefunRecipeCategory, icon);
    }
}
