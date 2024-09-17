package me.justahuman.slimefun_essentials.mixins.patchouli;

import me.justahuman.slimefun_essentials.utils.JsonUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.patchouli.common.util.ItemStackUtil;

@Mixin(ItemStackUtil.class)
public class ItemStackUtilMixin {
    @Inject(method = "loadStackFromString", at = @At("HEAD"), cancellable = true, remap = false)
    private static void deserializeIcon(String res, RegistryWrapper.WrapperLookup registries, CallbackInfoReturnable<ItemStack> cir) {
        if (res.startsWith("{")) {
            cir.setReturnValue(JsonUtils.deserializeItem(res));
        }
    }
}
