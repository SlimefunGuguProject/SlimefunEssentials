package me.justahuman.slimefun_essentials.mixins.items;

import me.justahuman.slimefun_essentials.utils.Utils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Locale;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow @Nullable
    public abstract NbtCompound getNbt();

    @Shadow
    public abstract Item getItem();

    @Inject(method = "getTooltip", at = @At(value = "RETURN"))
    public void changeIdentifierLine(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir) {
        final String id = Utils.getSlimefunId(getNbt());
        if (id == null) {
            return;
        }

        final List<Text> lore = cir.getReturnValue();
        final String idLine = Registries.ITEM.getId(getItem()).toString();
        for (int i = 0; i < lore.size(); i++) {
            Text line = lore.get(i);
            if (line.getString().equals(idLine)) {
                lore.set(i, Text.literal("slimefun:" + id.toLowerCase(Locale.ROOT)).formatted(Formatting.DARK_GRAY));
            }
        }
    }
}
