package me.justahuman.slimefun_essentials.compat.rei.categorys;

import me.justahuman.slimefun_essentials.client.SlimefunRecipeCategory;
import me.justahuman.slimefun_essentials.compat.rei.displays.ProcessDisplay;
import net.minecraft.item.ItemStack;

public class ProcessCategory extends SlimefunReiCategory<ProcessDisplay> {
    public ProcessCategory(SlimefunRecipeCategory slimefunRecipeCategory, ItemStack icon) {
        super(Type.PROCESS, slimefunRecipeCategory, icon);
    }
}
