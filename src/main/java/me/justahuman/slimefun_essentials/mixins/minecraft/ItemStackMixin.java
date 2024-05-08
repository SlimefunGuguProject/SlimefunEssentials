package me.justahuman.slimefun_essentials.mixins.minecraft;

import me.justahuman.slimefun_essentials.utils.Utils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Locale;
import java.util.Set;

@Mixin(value = ItemStack.class, priority = 100000)
public abstract class ItemStackMixin {
    @Unique
    private static final Set<String> HIDDEN = Set.of("_UI_BACKGROUND", "_UI_INPUT_SLOT", "_UI_OUTPUT_SLOT");

    @Shadow @Nullable
    public abstract NbtCompound getNbt();

    @Shadow
    public abstract Item getItem();

    @Inject(method = "getTooltip", at = @At(value = "RETURN"))
    public void changeTooltip(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir) {
        final String guideMode = Utils.getGuideMode(getNbt());
        final String id = guideMode == null ? Utils.getSlimefunId(getNbt()) : guideMode + "_guide";
        if (id == null) {
            return;
        }

        final List<Text> lore = cir.getReturnValue();
        if (HIDDEN.contains(id)) {
            lore.clear();
            return;
        }

        final Identifier identifier = Registries.ITEM.getId(getItem());
        final String idLine = identifier.toString();
        for (int i = 0; i < lore.size(); i++) {
            String line = lore.get(i).getString();
            if (line.equals(idLine)) {
                lore.set(i, Text.literal("slimefun:" + id.toLowerCase(Locale.ROOT)).formatted(Formatting.DARK_GRAY));
            } else if (line.equals("Minecraft")) {
                lore.set(i, Text.literal("Slimefun").formatted(Formatting.BLUE).formatted(Formatting.ITALIC));
            }
        }
    }
}
