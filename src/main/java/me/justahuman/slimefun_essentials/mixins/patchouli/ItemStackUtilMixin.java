package me.justahuman.slimefun_essentials.mixins.patchouli;

import me.justahuman.slimefun_essentials.utils.JsonUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.tuple.Triple;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.patchouli.common.util.ItemStackUtil;

@Mixin(ItemStackUtil.class)
public class ItemStackUtilMixin {
    @Inject(method = "parseItemStackString", at = @At("HEAD"), cancellable = true, remap = false)
    private static void deserializeIcon(String string, CallbackInfoReturnable<Triple<Identifier, Integer, NbtCompound>> cir) {
        if (string.startsWith("{")) {
            final ItemStack itemStack = JsonUtils.deserializeItem(string);
            cir.setReturnValue(Triple.of(Registries.ITEM.getId(itemStack.getItem()), itemStack.getCount(), itemStack.getNbt()));
        }
    }
}
