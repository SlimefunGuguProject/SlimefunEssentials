package me.justahuman.slimefun_essentials.client;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public record SlimefunItemStack(String id, ItemStack itemStack) {
    public void setCustomModelData(long customModelData) {
        final NbtCompound nbt = itemStack.getNbt();
        if (nbt != null) {
            nbt.putLong("CustomModelData", customModelData);
        }
    }

    public SlimefunItemStack setAmount(int amount) {
        itemStack.setCount(amount);
        return this;
    }
    
    public SlimefunItemStack copy() {
        return new SlimefunItemStack(id, itemStack.copy());
    }
}
