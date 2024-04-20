package me.justahuman.slimefun_essentials.compat.rei.categorys;

import me.justahuman.slimefun_essentials.client.SlimefunRecipeCategory;
import me.justahuman.slimefun_essentials.compat.rei.displays.ReactorDisplay;
import net.minecraft.item.ItemStack;

public class ReactorCategory extends SlimefunReiCategory<ReactorDisplay> {
    public ReactorCategory(SlimefunRecipeCategory slimefunRecipeCategory, ItemStack icon) {
        super(Type.REACTOR, slimefunRecipeCategory, icon);
    }
}
