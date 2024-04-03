package me.justahuman.slimefun_essentials.compat.rei;

import me.justahuman.slimefun_essentials.utils.Utils;
import me.shedaniel.rei.api.common.entry.comparison.ComparisonContext;
import me.shedaniel.rei.api.common.entry.comparison.EntryComparator;
import net.minecraft.item.ItemStack;

public class SlimefunIdComparator implements EntryComparator<ItemStack> {
    @Override
    public long hash(ComparisonContext context, ItemStack stack) {
        final String sfId = Utils.getSlimefunId(stack);
        if (sfId != null) {
            return sfId.hashCode();
        }
        return 1;
    }
}
