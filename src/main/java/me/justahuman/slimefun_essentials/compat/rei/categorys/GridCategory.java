package me.justahuman.slimefun_essentials.compat.rei.categorys;

import me.justahuman.slimefun_essentials.client.SlimefunRecipeCategory;
import me.justahuman.slimefun_essentials.compat.rei.displays.GridDisplay;
import net.minecraft.item.ItemStack;

public class GridCategory extends SlimefunReiCategory<GridDisplay> {
    public GridCategory(SlimefunRecipeCategory slimefunRecipeCategory, ItemStack icon, int side) {
        super(Type.grid(side), slimefunRecipeCategory, icon);
    }
}
